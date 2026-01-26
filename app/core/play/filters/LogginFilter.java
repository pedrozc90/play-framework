package core.play.filters;

import akka.stream.Materializer;
import play.Logger;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@Singleton
public class LogginFilter extends Filter {

    private static final long SLOW_REQUEST = 1_000;

    private final Logger.ALogger logger = Logger.of(LogginFilter.class);

    @Inject
    public LogginFilter(final Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(final Function<Http.RequestHeader, CompletionStage<Result>> next, final Http.RequestHeader req) {
        final long start = System.currentTimeMillis();

        final String method = req.method();
        final String path = req.path();

        return next.apply(req)
            .whenComplete((result, cause) -> {
                final long elapsed = System.currentTimeMillis() - start;

                if (cause != null) {
                    logger.error("Request failed: {} {} -> {} ({} ms)", method, path, 500, elapsed);
                } else if (result != null) {
                    final int status = result.status();
                    if (status >= 400) {
                        logger.error("Request error: {} {} -> {} ({} ms)", method, path, status, elapsed);
                    } else {
                        if (elapsed >= SLOW_REQUEST) {
                            logger.warn("Request completed: {} {} -> {} ({} ms)", method, path, status, elapsed);
                        } else {
                            logger.info("Request completed: {} {} -> {} ({} ms)", method, path, status, elapsed);
                        }
                    }
                }
            }).thenApply((result) -> {
                final long elapsed = System.currentTimeMillis() - start;
                return result.withHeaders("X-Response-Time", elapsed + "ms");
            });
    }

}
