package controllers.users;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.users.objects.UserCredentials;
import controllers.users.objects.UserDto;
import core.auth.Authenticated;
import core.mappers.UserMapper;
import core.utils.objects.ResultBuilder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class UserController extends Controller {

    private final UserResourceHandler handler;
    private final UserMapper mapper;

    @Inject
    public UserController(final UserResourceHandler handler,
                          final UserMapper mapper) {
        this.handler = handler;
        this.mapper = mapper;
    }

    public CompletionStage<Result> register(final Http.Request request) {
        final JsonNode body = request.body().asJson();
        final UserCredentials data = Json.fromJson(body, UserCredentials.class);
        return handler.register(data).thenApply((user) -> {
            final UserDto dto = mapper.toDto(user);
            return ResultBuilder.of(dto).created();
        });
    }

    @Authenticated
    public CompletionStage<Result> get(final Http.Request request, final Long id) {
        return CompletableFuture.supplyAsync(() -> ok());
    }

}
