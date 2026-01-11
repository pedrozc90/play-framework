package controllers.health;

import core.utils.objects.ResultBuilder;
import play.mvc.Controller;
import play.mvc.Result;
import services.HealthService;

public class HealthController extends Controller {

    private static final HealthService service = HealthService.getInstance();

    public static Result health() {
        final HealthDto dto = service.create();
        return ResultBuilder.of(dto).ok();
    }

}
