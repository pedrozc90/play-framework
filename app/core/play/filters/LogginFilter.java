package core.play.filters;

import core.play.utils.ScalaUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import play.Logger;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import scala.Function1;
import scala.Tuple2;
import scala.concurrent.Future;
import scala.runtime.AbstractFunction1;

public class LogginFilter extends PlayFilter {

    private static final Logger.ALogger logger = Logger.of(LogginFilter.class);

    @Override
    public Future<Result> apply(final Function1<RequestHeader, Future<Result>> next, final RequestHeader req) {
        final long start = System.currentTimeMillis();
        final String method = req.method();
        final String path = req.path();

        return next.apply(req).map(new LogFunction(start, method, path), executor);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = false)
    private static class LogFunction extends AbstractFunction1<Result, Result> {

        private final long start;
        private final String method;
        private final String path;

        @Override
        public Result apply(final Result result) {
            final long elapsed = System.currentTimeMillis() - start;

            final int status = result.header().status();

            logger.info("Complete request: {} {} -> {} ({} ms)", method, path, status, elapsed);

            final Tuple2<String, String> tuple = ScalaUtils.toTuple("X-Response-Time", elapsed + "ms");
            return result.withHeaders(ScalaUtils.toSeq(tuple));
        }

    }

}
