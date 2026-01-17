package core.config;

import play.Application;
import play.Play;

public class Configuration {

    private static Configuration instance;

    private final Application application = Play.application();

    public static Configuration getInstance() {
        if (instance == null) {
            synchronized (Configuration.class) {
                if (instance == null) {
                    instance = new Configuration();
                }
            }
        }
        return instance;
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

    public String getJwtSecret() {
        return getAsString("app.jwt.secret");
    }

    public String getJwtIssuer() {
        return getAsString("app.jwt.issuer");
    }

    public long getJwtExpiration() {
        final Integer value = getAsInteger("app.jwt.expiration");
        return value.longValue();
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
