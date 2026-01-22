package web.controllers.auth;

import application.auth.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import web.controllers.auth.objects.LoginRequest;
import web.controllers.auth.objects.LoginResponse;
import web.security.annotations.Authenticated;
import web.security.annotations.RequiresRole;
import web.security.objects.Attrs;
import web.security.objects.UserContext;

public class AuthController extends Controller {

    private static final AuthenticationService service = AuthenticationService.getInstance();

    @Transactional
    public static Result login() throws AppException {
        final JsonNode body = request().body().asJson();
        final LoginRequest data = Json.fromJson(body, LoginRequest.class);

        final LoginResponse result = service.authenticate(data.getEmail(), data.getPassword());

        response().setCookie("TOKEN", result.getToken(), 3_600, "/", null, false, true);

        return ResultBuilder.of(result).ok();
    }

    @Authenticated
    public static Result logout() {
        final Http.Cookie cookie = request().cookie("TOKEN");
        if (cookie != null) {
            response().discardCookie("TOKEN");
        }
        return ok();
    }

    @Authenticated
    public static Result context() {
        final Http.Context ctx = Http.Context.current();
        final UserContext context = (UserContext) ctx.args.get(Attrs.USER_CONTEXT);
        return ResultBuilder.of(context).ok();
    }

    @Authenticated
    @RequiresRole({ "admin" })
    public static Result permissions() {
        return ResultBuilder.of("You shouldn't be seeing this message").ok();
    }

}
