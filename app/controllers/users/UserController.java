package controllers.users;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.users.objects.UserCredentials;
import controllers.users.objects.UserDto;
import core.auth.Authenticated;
import core.exceptions.AppException;
import core.mappers.UserMapper;
import core.play.utils.ResultBuilder;
import core.utils.validation.Validator;
import models.users.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import javax.inject.Singleton;

@Singleton
public class UserController extends Controller {

    private static final UserService userService = UserService.getInstance();
    private static final UserMapper mapper = UserMapper.getInstance();
    private static final Validator validator = Validator.getInstance();


    @Authenticated
    public static Result fetch(final int page, final int rows, final String q) throws AppException {
        return ResultBuilder.of().notImplemented();
    }

    @Authenticated
    public static Result save() throws AppException {
        final JsonNode body = request().body().asJson();
        final UserCredentials data = Json.fromJson(body, UserCredentials.class);
        validator.validate(data);
        final User user = userService.register(data.getEmail(), data.getPassword());
        final UserDto dto = mapper.toDto(user);
        return ResultBuilder.of(dto).created();
    }

    @Authenticated
    public static Result get(final Long id) throws AppException {
        final User user = userService.get(id);
        final UserDto dto = mapper.toDto(user);
        return ResultBuilder.of(dto).created();
    }

    @Authenticated
    public static Result update(final Long id) throws AppException {
        final JsonNode body = request().body().asJson();
        return ResultBuilder.of().notImplemented();
    }

    @Authenticated
    public static Result delete(final Long id) throws AppException {
        final JsonNode body = request().body().asJson();
        return ResultBuilder.of().notImplemented();
    }

}
