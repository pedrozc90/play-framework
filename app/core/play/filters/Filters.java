package core.play.filters;

import play.api.mvc.EssentialFilter;
import play.http.HttpFilters;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Filters implements HttpFilters {

    private final CorsFilter corsFilter;
    private final LogginFilter logginFilter;

    @Inject
    public Filters(
        final CorsFilter corsFilter,
        final LogginFilter logginFilter
    ) {
        this.corsFilter = corsFilter;
        this.logginFilter = logginFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[]{ corsFilter, logginFilter };
    }

}
