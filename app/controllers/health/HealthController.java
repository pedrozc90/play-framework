package controllers.health;

import core.utils.objects.ResultBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import services.HealthService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HealthController extends Controller {

    @Inject
    private HealthService service;

    public Result health() {
        final HealthDto dto = service.create();
        return ResultBuilder.of(dto).ok();
    }

}
