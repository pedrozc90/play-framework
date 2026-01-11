package core.play.filters;

import play.http.DefaultHttpFilters;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Filters extends DefaultHttpFilters {

    @Inject
    public Filters(final CorsFilter corsFilter,
                   final LogginFilter logginFilter) {
        super(corsFilter, logginFilter);
    }

}
