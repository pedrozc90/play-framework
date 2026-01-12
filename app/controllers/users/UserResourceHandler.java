package controllers.users;

import controllers.users.objects.UserCredentials;
import models.users.User;
import play.libs.concurrent.HttpExecutionContext;
import services.UserService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserResourceHandler {

    private final UserService service;
    private final HttpExecutionContext context;

    @Inject
    public UserResourceHandler(final HttpExecutionContext context,
                               final UserService repository) {
        this.context = context;
        this.service = repository;
    }

    public CompletionStage<User> register(final UserCredentials data) {
        return CompletableFuture.supplyAsync(
            () -> service.register(data.getEmail(), data.getPassword()),
            context.current()
        );
    }

}
