package core.play.filters;

import play.http.DefaultHttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class Filters extends DefaultHttpFilters {

    private final LogginFilter logginFilter;

    @Inject
    public Filters(final LogginFilter logginFilter) {
        this.logginFilter = logginFilter;
    }

    @Override
    public List<EssentialFilter> getFilters() {
        final List<EssentialFilter> filters = new ArrayList<>();
        filters.add(logginFilter.asJava()); // add logging filter as first in the chain
        filters.addAll(super.getFilters());
        return filters;
    }

}
