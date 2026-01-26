package web.controllers.health;

import application.health.HealthService;
import core.play.utils.ResultBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import web.controllers.health.objects.HealthDto;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HealthController extends Controller {

    private final HealthService service;

    @Inject
    public HealthController(final HealthService service) {
        this.service = service;
    }

    public Result health() {
        final HealthDto dto = service.create();
        return ResultBuilder.of(dto).ok();
    }

}
