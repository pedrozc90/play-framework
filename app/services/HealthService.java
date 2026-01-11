package services;

import controllers.health.HealthDto;
import core.config.Configuration;

public class HealthService {

    private static HealthService instance;

    private final Configuration config = Configuration.getInstance();

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
