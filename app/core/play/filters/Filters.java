package core.play.filters;

import play.http.DefaultHttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Filters extends DefaultHttpFilters {

    private final CorsFilter corsFilter;
    private final LogginFilter logginFilter;

    @Inject
    public Filters(final CorsFilter corsFilter,
                   final LogginFilter logginFilter) {
        this.corsFilter = corsFilter;
        this.logginFilter = logginFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[]{ corsFilter.asJava(), logginFilter.asJava() };
    }

}
