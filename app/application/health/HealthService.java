package application.health;

import config.Configuration;
import web.controllers.health.objects.HealthDto;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HealthService {

    @Inject
    private Configuration config;

    public HealthDto create() {
        return new HealthDto(
            config.name(),
            config.version(),
            config.mode()
        );
    }

}
