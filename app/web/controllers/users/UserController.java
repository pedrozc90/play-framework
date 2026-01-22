package web.controllers.users;

import application.users.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import core.exceptions.AppException;
import core.objects.Page;
import core.play.utils.ResultBuilder;
import core.utils.validation.Validator;
import domain.users.User;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import web.controllers.users.objects.UserRegistrationCmd;
import web.controllers.users.objects.UserUpdateCmd;
import web.dtos.UserDto;
import web.mappers.UserMapper;
import web.security.annotations.Authenticated;

public class UserController extends Controller {

    private static final UserService userService = UserService.getInstance();
    private static final UserMapper mapper = UserMapper.getInstance();
    private static final Validator validator = Validator.getInstance();

    // @Authenticated
    @Transactional
    public static Result fetch(final int page, final int rows, final String q, final Boolean active) throws AppException {
        final Page<User> result = userService.fetch(page, rows, q, active);
        return ResultBuilder.of(result.map(mapper::toDto)).ok();
    }

    @Authenticated
    @Transactional
    public static Result save() throws AppException {
        final JsonNode body = request().body().asJson();
        final UserRegistrationCmd data = Json.fromJson(body, UserRegistrationCmd.class);
        validator.validate(data);
        final User user = userService.register(data.getEmail(), data.getPassword());
        final UserDto dto = mapper.toDto(user);
        return ResultBuilder.of(dto).created();
    }

    @Authenticated
    @Transactional
    public static Result get(final Long id) throws AppException {
        final User user = userService.get(id);
        final UserDto dto = mapper.toDto(user);
        return ResultBuilder.of(dto).created();
    }

    @Authenticated
    @Transactional
    public static Result update(final Long id) throws AppException {
        final JsonNode body = request().body().asJson();
        final UserUpdateCmd cmd = Json.fromJson(body, UserUpdateCmd.class);
        validator.validate(cmd);
        final User user = userService.update(id, cmd);
        final UserDto dto = mapper.toDto(user);
        return ResultBuilder.of(dto).ok();
    }

    @Authenticated
    @Transactional
    public static Result delete(final Long id) throws AppException {
        final User user = userService.get(id);
        userService.remove(user);
        return ResultBuilder.of().ok();
    }

}
