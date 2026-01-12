package services;

import controllers.health.HealthDto;
import core.config.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HealthService {

    private final Configuration config;

    @Inject
    public HealthService(final Configuration config) {
        this.config = config;
    }

    public HealthDto create() {
        return new HealthDto(
            config.name(),
            config.version(),
            config.mode()
        );
    }

}
