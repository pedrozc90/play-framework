package web.controllers.health;

import application.health.HealthService;
import core.play.utils.ResultBuilder;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

@Singleton
public class HealthController extends Controller {

    private final HealthService service;

    @Inject
    public HealthController(final HealthService service) {
        this.service = service;
    }

    public CompletionStage<Result> health() {
        return service.create().thenApply((dto) -> ResultBuilder.of(dto).ok());
    }

}
