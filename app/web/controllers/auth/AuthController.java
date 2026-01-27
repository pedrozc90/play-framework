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

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthController extends Controller {

    private final AuthenticationService service;

    @Inject
    public AuthController(final AuthenticationService service) {
        this.service = service;
    }

    @Transactional
    public Result login() throws AppException {
        final JsonNode body = request().body().asJson();
        final LoginRequest data = Json.fromJson(body, LoginRequest.class);

        final LoginResponse result = service.authenticate(data.getEmail(), data.getPassword());

        final Http.Cookie cookie = service.createCookie(result.getToken());

        response().setCookie(cookie);

        return ResultBuilder.of(result).ok();
    }

    @Authenticated
    public Result logout() {
        final Http.Cookie cookie = request().cookie("TOKEN");
        if (cookie != null) {
            response().discardCookie("TOKEN");
        }
        return ok();
    }

    @Authenticated
    public Result context() {
        final Http.Context ctx = Http.Context.current();
        final UserContext context = (UserContext) ctx.args.get(Attrs.USER_CONTEXT);
        return ResultBuilder.of(context).ok();
    }

    @Authenticated
    @RequiresRole({ "admin" })
    public Result permissions() {
        return ResultBuilder.of("You shouldn't be seeing this message").ok();
    }

}
