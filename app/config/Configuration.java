package config;

import core.utils.StringUtils;
import play.Application;
import play.Play;

import javax.inject.Singleton;
import java.time.Duration;

@Singleton
public class Configuration {

    private final Application application = Play.application();

    public boolean isProduction() {
        return application.isProd();
    }

    public boolean isDevelopment() {
        return application.isDev();
    }

    public boolean isTest() {
        return application.isTest();
    }

    public String mode() {
        if (isProduction()) return "production";
        else if (isDevelopment()) return "development";
        else if (isTest()) return "test";
        return "none";
    }

    public String name() {
        return getAsString("app.name");
    }

    public String version() {
        return getAsString("app.version");
    }

    public String getJwtSecret() {
        return getAsString("app.jwt.secret");
    }

    public String getJwtIssuer() {
        return getAsString("app.jwt.issuer");
    }

    public Duration getJwtExpiration() {
        try {
            final String value = getAsString("app.jwt.expiration");
            return StringUtils.parseDuration(value);
        } catch (ArithmeticException e) {
            return null;
        }
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
