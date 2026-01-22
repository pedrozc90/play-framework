package application.health;

import config.Configuration;
import web.controllers.health.objects.HealthDto;

public class HealthService {

    private final Configuration config = Configuration.getInstance();

    private static HealthService instance;

    public static HealthService getInstance() {
        if (instance == null) {
            instance = new HealthService();
        }
        return instance;
    }

    public HealthDto create() {
        return new HealthDto(
            config.name(),
            config.version(),
            config.mode()
        );
    }

}
