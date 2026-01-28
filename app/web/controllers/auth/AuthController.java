package web.controllers.auth;

import application.auth.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import core.utils.validation.Validator;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.*;
import web.controllers.auth.objects.LoginRequest;
import web.security.annotations.Authenticated;
import web.security.annotations.RequiresRole;
import web.security.objects.Attrs;
import web.security.objects.UserContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class AuthController extends Controller {

    private final AuthenticationService service;
    private final Validator validator;

    @Inject
    public AuthController(
        final AuthenticationService service,
        final Validator validator
    ) {
        this.service = service;
        this.validator = validator;
    }

    @Transactional
    public CompletionStage<Result> login(final Http.Request request) throws AppException {
        final JsonNode body = request.body().asJson();
        final LoginRequest data = Json.fromJson(body, LoginRequest.class);
        validator.validate(data);
        return service.authenticate(data.getEmail(), data.getPassword()).thenApply((res) -> {
            final Http.Cookie cookie = service.createCookie(res.getToken());
            return ResultBuilder.of(res).cookie(cookie).ok();
        });
    }

    @Authenticated
    public CompletionStage<Result> logout(final Http.Request request) {
        return CompletableFuture.supplyAsync(() -> {
            final Http.Cookie cookie = request.cookie("TOKEN");
            final StatusHeader result = Results.ok();
            if (cookie != null) {
                result.discardingCookie("TOKEN");
            }
            return result;
        });
    }

    @Authenticated
    public CompletionStage<Result> context(final Http.Request request) {
        final UserContext context = request.attrs().get(Attrs.USER_CONTEXT);
        return CompletableFuture.supplyAsync(() -> ResultBuilder.of(context).ok());
    }

    @Authenticated
    @RequiresRole({ "admin" })
    public CompletionStage<Result> permissions() {
        return CompletableFuture.completedFuture(ResultBuilder.of("You shouldn't be seeing this message").ok());
    }

}
