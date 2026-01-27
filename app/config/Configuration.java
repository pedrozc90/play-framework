package config;

import com.typesafe.config.Config;
import play.Environment;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Singleton
public class Configuration {

    private final Config config;
    private final Environment environment;

    @Inject
    public Configuration(
        final Config config,
        final Environment environment
    ) {
        this.config = config;
        this.environment = environment;
    }

    public boolean isProduction() {
        return environment.isProd();
    }

    public boolean isDevelopment() {
        return environment.isDev();
    }

    public boolean isTest() {
        return environment.isTest();
    }

    public String mode() {
        return environment.mode().name().toLowerCase();
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
        return config.getString(key);
    }

    private Integer getAsInteger(final String key) {
        return config.getInt(key);
    }

    private boolean getAsBoolean(final String key) {
        return config.getBoolean(key);
    }

    private Duration getAsDuration(final String key) {
        return config.getDuration(key);
    }

}
