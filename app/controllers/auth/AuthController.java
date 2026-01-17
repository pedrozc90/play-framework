package controllers.auth;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.auth.objects.LoginResponse;
import controllers.users.objects.UserCredentials;
import core.auth.Attrs;
import core.auth.Authenticated;
import core.auth.RequiresRole;
import core.auth.UserContext;
import core.play.utils.ResultBuilder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthenticationService;

import javax.inject.Singleton;

@Singleton
public class AuthController extends Controller {

    private static final AuthenticationService service = AuthenticationService.getInstance();

    public static Result login() {
        final JsonNode body = request().body().asJson();
        final UserCredentials data = Json.fromJson(body, UserCredentials.class);

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
