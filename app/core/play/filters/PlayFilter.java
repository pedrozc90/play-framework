package core.play.filters;

import play.api.libs.concurrent.Execution;
import play.api.mvc.EssentialAction;
import play.api.mvc.Filter;
import play.api.mvc.Filter$class;
import scala.concurrent.ExecutionContext;

public abstract class PlayFilter implements Filter {

    /**
     * Single shared execution context for all filters.
     * Play 2.3 uses the global EC by default.
     */
    protected static final ExecutionContext executor = Execution.defaultContext();

    /**
     * Required Scala bridge — keep it final.
     */
    @Override
    public EssentialAction apply(final EssentialAction next) {
        return Filter$class.apply(this, next);
    }

}
