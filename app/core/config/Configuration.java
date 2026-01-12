package core.config;

import com.typesafe.config.Config;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Configuration {

    private final Logger.ALogger logger = Logger.of(Configuration.class);

    private final Config config;

    @Inject
    public Configuration(final Config config) {
        this.config = config;
    }

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
        if (!config.hasPath(key)) return null;
        return config.getString(key);
    }

    private Integer getAsInteger(final String key) {
        if (!config.hasPath(key)) return null;
        return config.getInt(key);
    }

    private boolean getAsBoolean(final String key) {
        if (!config.hasPath(key)) return false;
        return config.getBoolean(key);
    }

}
