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

    private static final Logger.ALogger logger = Logger.of(LogginFilter.class);

    @Inject
    public LogginFilter(final Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(final Function<Http.RequestHeader, CompletionStage<Result>> next, final Http.RequestHeader request) {
        final long start = System.currentTimeMillis();

        final String method = request.method();
        final String uri = request.uri();
        logger.info("Incoming request: {} {}", method, uri);

        return next.apply(request).thenApply(result -> {
            final long end = System.currentTimeMillis();
            final long elapsed = end - start;
            final int status = result.status();

            logger.info("Complete request: {} {} -> {} ({} ms)", method, uri, status, elapsed);

            return result.withHeader("X-Response-Time", elapsed + "ms");
        });
    }

}
