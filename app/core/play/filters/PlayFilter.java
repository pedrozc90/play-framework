package core.play.filters;

import play.api.libs.concurrent.Execution;
import play.api.mvc.*;
import scala.Function1;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.runtime.AbstractFunction1;

import java.util.Arrays;

public abstract class PlayFilter implements Filter {

    private final ExecutionContext executor = Execution.defaultContext();

    @Override
    public Future<Result> apply(final Function1<RequestHeader, Future<Result>> future, final RequestHeader requestHeader) {
        PlayFilter self = this;
        return future.apply(requestHeader).map(new AbstractFunction1<Result, Result>() {
            @Override
            public Result apply(final Result v1) {
                return self.apply(v1);
            }
        }, executor);
    }

    @Override
    public EssentialAction apply(final EssentialAction next) {
        return Filter$class.apply(this, next);
    }

    public abstract Result apply(final Result result);

    @SafeVarargs
    public final <T> Seq<T> toSeq(final T... args) {
        return JavaConversions.asScalaBuffer(Arrays.asList(args)).toSeq();
    }

}
