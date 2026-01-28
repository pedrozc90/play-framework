package web.controllers.users;

import application.users.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import core.utils.validation.Validator;
import domain.users.User;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import web.controllers.users.objects.UserRegistrationCmd;
import web.controllers.users.objects.UserUpdateCmd;
import web.dtos.UserDto;
import web.mappers.UserMapper;
import web.security.annotations.Authenticated;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class UserController extends Controller {

    private final UserService userService;
    private final UserMapper mapper;
    private final Validator validator;

    @Inject
    public UserController(
        final UserService userService,
        final UserMapper mapper,
        final Validator validator
    ) {
        this.userService = userService;
        this.mapper = mapper;
        this.validator = validator;
    }

    // @Authenticated
    @Transactional(readOnly = true)
    public CompletionStage<Result> fetch(final int page, final int rows, final String q, final Boolean active) throws AppException {
        return userService.fetch(page, rows, q, active).thenApply((res) -> {
            return ResultBuilder.of(res.map(mapper::toDto)).ok();
        });
    }

    @Authenticated
    @Transactional
    public CompletionStage<Result> save(final Http.Request request) throws AppException {
        final JsonNode body = request.body().asJson();

        return CompletableFuture.supplyAsync(() -> {
            try {
                final UserRegistrationCmd data = Json.fromJson(body, UserRegistrationCmd.class);
                validator.validate(data);
                final User user = userService.register(null, data.getEmail(), data.getPassword());
                final UserDto dto = mapper.toDto(user);
                return ResultBuilder.of(dto).created();
            } catch (AppException e) {
                throw e.toCompletionException();
            }
        });
    }

    @Authenticated
    @Transactional(readOnly = true)
    public CompletionStage<Result> get(final Long id) throws AppException {
        return userService.getAsync(id).thenApply((user) -> {
            final UserDto dto = mapper.toDto(user);
            return ResultBuilder.of(dto).created();
        });
    }

    @Authenticated
    @Transactional
    public CompletionStage<Result> update(final Http.Request request, final Long id) throws AppException {
        final JsonNode body = request.body().asJson();
        return CompletableFuture.supplyAsync(() -> {
            try {
                final UserUpdateCmd cmd = Json.fromJson(body, UserUpdateCmd.class);
                validator.validate(cmd); // throws ConstraintViolationException
                final User user = userService.update(null, id, cmd);
                final UserDto dto = mapper.toDto(user);
                return ResultBuilder.of(dto).ok();
            } catch (AppException e) {
                throw e.toCompletionException();
            }
        });
    }

    @Authenticated
    @Transactional
    public CompletionStage<Result> delete(final Long id) throws AppException {
        return userService.getAsync(id).thenApply((user) -> {
            userService.remove(user);
            return ResultBuilder.of().ok();
        });
    }

}
