package core.play.filters;

import akka.stream.Materializer;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@Singleton
public class CorsFilter extends Filter {

    private static final Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Origin,X-Response-Time,X-Requested-With,Content-Type,Accept,Referer,User-Agent,Authorization");
        headers.put("Access-Control-Expose-Headers", "Content-Disposition");
        headers.put("Access-Control-Max-Age", "1");
    }

    @Inject
    public CorsFilter(final Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(final Function<Http.RequestHeader, CompletionStage<Result>> next, final Http.RequestHeader request) {
        return next.apply(request).thenApply((result) -> {
            headers.forEach((k, v) -> {
                result.withHeaders(k, v);
            });
            return result;
        });
    }

}
