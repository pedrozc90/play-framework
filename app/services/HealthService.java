package services;

import controllers.health.HealthDto;

public class HealthService {

    private static  HealthService instance;

    public static HealthService getInstance() {
        if (instance == null) {
            instance = new HealthService();
        }
        return instance;
    }

    public HealthDto create() {
        return new HealthDto();
    }

}
