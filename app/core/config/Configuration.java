package core.config;

import play.Application;
import play.Play;

import javax.inject.Singleton;

@Singleton
public class Configuration {

    private final Application application = Play.application();

    public String mode() {
        return getAsString("application.mode");
    }

    public String name() {
        return getAsString("application.name");
    }

    public String version() {
        return getAsString("application.version");
    }

    // HELPERS
    private String getAsString(final String key) {
        return application.configuration().getString(key);
    }

    private Integer getAsInteger(final String key) {
        return application.configuration().getInt(key);
    }

    private boolean getAsBoolean(final String key) {
        return application.configuration().getBoolean(key) == Boolean.TRUE;
    }

}
