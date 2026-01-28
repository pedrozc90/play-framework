package application.health;

import config.Configuration;
import web.controllers.health.objects.HealthDto;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class HealthService {

    @Inject
    private Configuration config;

    public CompletionStage<HealthDto> create() {
        return CompletableFuture.supplyAsync(() -> new HealthDto(
            config.name(),
            config.version(),
            config.mode()
        ));
    }

}
