package core.play.filters;

import play.Logger;
import play.api.libs.concurrent.Execution;
import play.api.mvc.*;
import scala.Function1;
import scala.Tuple2;
import scala.collection.JavaConversions;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.runtime.AbstractFunction1;

import java.util.Collections;
import java.util.List;

public class LogginFilter implements Filter {

    private static final Logger.ALogger logger = Logger.of(LogginFilter.class);

    private final ExecutionContext executor = Execution.defaultContext();

    @Override
    public Future<Result> apply(final Function1<RequestHeader, Future<Result>> future, final RequestHeader header) {
        final long start = System.currentTimeMillis();

        logger.info("Incoming request: {} {}", header.method(), header.uri());

        return future.apply(header).map(new AbstractFunction1<Result, Result>() {
            @Override
            public Result apply(final Result result) {
                final long end = System.currentTimeMillis();
                final long elapsed = end - start;
                final int status = result.header().status();

                logger.info("Complete request: {} {} -> {} ({} ms)", header.method(), header.uri(), status, elapsed);

                final List<Tuple2<String, String>> headers = Collections.singletonList(new Tuple2<>("X-Response-Time", elapsed + "ms"));

                return result.withHeaders(JavaConversions.asScalaBuffer(headers).toSeq());
            }
        }, executor);
    }

    @Override
    public EssentialAction apply(final EssentialAction next) {
        return Filter$class.apply(this, next);
    }

}
