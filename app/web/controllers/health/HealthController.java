package web.controllers.health;

import application.health.HealthService;
import core.play.utils.ResultBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import web.controllers.health.objects.HealthDto;

public class HealthController extends Controller {

    private static final HealthService service = HealthService.getInstance();

    public static Result health() {
        final HealthDto dto = service.create();
        return ResultBuilder.of(dto).ok();
    }

}
