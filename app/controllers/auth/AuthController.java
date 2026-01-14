package controllers.auth;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.users.objects.UserCredentials;
import core.auth.Attrs;
import core.auth.Authenticated;
import core.auth.UserContext;
import core.utils.objects.ResultBuilder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthenticationService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class AuthController extends Controller {

    private final AuthenticationService service;

    @Inject
    public AuthController(final AuthenticationService service) {
        this.service = service;
    }

    public CompletionStage<Result> login(final Http.Request request) {
        final JsonNode body = request.body().asJson();
        final UserCredentials data = Json.fromJson(body, UserCredentials.class);
        return service.authenticate(data.getEmail(), data.getPassword())
            .thenApply((obj) -> ResultBuilder.of(obj).ok());
    }

    @Authenticated
    public CompletionStage<Result> context(final Http.Request request) {
        final UserContext context = request.attrs().get(Attrs.USER_CONTEXT);
        return CompletableFuture.supplyAsync(() -> ResultBuilder.of(context).ok());
    }

}
