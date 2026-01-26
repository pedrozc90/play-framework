package config;

import core.utils.StringUtils;
import play.Application;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Singleton
public class Configuration {

    private final Application application;
    private final play.Configuration configuration;

    @Inject
    public Configuration(
        final Application application,
        final play.Configuration configuration
    ) {
        this.application = application;
        this.configuration = configuration;
    }

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
        return configuration.getString("app.jwt.issuer");
    }

    public Duration getJwtExpiration() {
        return getAsDuration("app.jwt.expiration");
    }

    public String getCookiesDomain() {
        return getAsString("app.jwt.cookies.domain");
    }

    public boolean getCookiesSecure() {
        return getAsBoolean("app.jwt.cookies.secure");
    }

    public Duration getCookiesMaxAge() {
        return getAsDuration("app.jwt.cookies.max-age");
    }

    // HELPERS
    private String getAsString(final String key) {
        return configuration.getString(key);
    }

    private Integer getAsInteger(final String key) {
        return configuration.getInt(key);
    }

    private boolean getAsBoolean(final String key) {
        return configuration.getBoolean(key) == Boolean.TRUE;
    }

    private Duration getAsDuration(final String key) {
        try {
            final String value = getAsString(key);
            return StringUtils.parseDuration(value);
        } catch (ArithmeticException e) {
            return null;
        }
    }

}
