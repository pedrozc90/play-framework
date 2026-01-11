package core.play.filters;

import play.api.mvc.Result;
import scala.Tuple2;

public class CorsFilter extends PlayFilter {

    @Override
    public Result apply(final Result result) {
        return result.withHeaders(
            toSeq(
                new Tuple2<>("Access-Control-Allow-Origin", "*"),
                new Tuple2<>("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS"),
                new Tuple2<>("Access-Control-Allow-Headers", "Origin,X-Response-Time,X-Requested-With,Content-Type,Accept,Referer,User-Agent,Authorization"),
                new Tuple2<>("Access-Control-Expose-Headers", "Content-Disposition"),
                new Tuple2<>("Access-Control-Max-Age", "1")
            )
        );
    }

}
