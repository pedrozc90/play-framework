package core.play.filters;

import core.play.utils.ScalaUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import play.api.mvc.RequestHeader;
import play.api.mvc.Result;
import scala.Function1;
import scala.Option;
import scala.concurrent.Future;
import scala.runtime.AbstractFunction1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CorsFilter extends PlayFilter {

    // Optional: restrict allowed origins
    private static final Set<String> ALLOWED_ORIGINS = new HashSet<>(Arrays.asList(
        "http://localhost:4200",
        "http://127.0.0.1:4200"
        // add prod domains here
    ));

    @Override
    public Future<Result> apply(final Function1<RequestHeader, Future<Result>> next, final RequestHeader req) {
        return next.apply(req).map(new CorsFunction(req), executor);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = false)
    private static class CorsFunction extends AbstractFunction1<Result, Result> {

        private final RequestHeader req;

        @Override
        public Result apply(final Result result) {
            final String method = req.method();
            final Option<String> optOrigin = req.headers().get("Origin");

            // If no Origin header → not a CORS request
            if (optOrigin.isEmpty()) {
                return result;
            }

            final String origin = optOrigin.get();

            // Optional whitelist check
            if (!ALLOWED_ORIGINS.isEmpty() && !ALLOWED_ORIGINS.contains(origin)) {
                return result; // or Results.forbidden()
            }

            // Always add these headers for CORS responses
            final Result withCors = result.withHeaders(
                ScalaUtils.toSeq(
                    ScalaUtils.toTuple("Access-Control-Allow-Origin", origin),
                    ScalaUtils.toTuple("Access-Control-Allow-Credentials", "true"),
                    ScalaUtils.toTuple("Vary", "Origin")
                )
            );

            // Preflight response (OPTIONS)
            if (Objects.equals(method, "OPTIONS")) {
                return result.withHeaders(
                    ScalaUtils.toSeq(
                        ScalaUtils.toTuple("Access-Control-Allow-Origin", origin),
                        ScalaUtils.toTuple("Access-Control-Allow-Credentials", "true"),
                        ScalaUtils.toTuple("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS"),
                        ScalaUtils.toTuple("Access-Control-Allow-Headers", "Origin,Content-Type,Accept,Authorization,X-Requested-With"),
                        ScalaUtils.toTuple("Access-Control-Max-Age", "3600"),
                        ScalaUtils.toTuple("Vary", "Origin")
                    )
                );
            }

            // Normal request
            return withCors.withHeaders(
                ScalaUtils.toSeq(ScalaUtils.toTuple("Access-Control-Expose-Headers", "Content-Disposition"))
            );
        }

    }

}
