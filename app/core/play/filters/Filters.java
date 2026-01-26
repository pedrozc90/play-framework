package core.play.filters;

import play.http.HttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Filters implements HttpFilters {

    private final LogginFilter logginFilter;

    @Inject
    public Filters(final LogginFilter logginFilter) {
        this.logginFilter = logginFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[]{ logginFilter.asJava() };
    }

}
