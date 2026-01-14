package core.auth;

import core.exceptions.AppException;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthenticationService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class AuthenticationAction extends Action<Authenticated> {

    private final AuthenticationService service;

    @Inject
    public AuthenticationAction(final AuthenticationService service) {
        this.service = service;
    }

    @Override
    public CompletionStage<Result> call(final Http.Request req) {
        final Optional<String> optAuthorization = req.header("Authorization");
        if (!optAuthorization.isPresent()) {
            throw AppException.of(401, "Authentication header is missing").toCompletionException();
        }

        final String authorization = optAuthorization.get();
        final String token = authorization.replace("Bearer ", "");
        if (token.isEmpty()) {
            throw AppException.of(401, "Bearer token is required").toCompletionException();
        }

        return service.validate(token).thenCompose((context) -> {
            // add user to request attributes (Not ThreadLocal)
            final Http.Request request = req.addAttr(Attrs.USER_CONTEXT, context);
            return delegate.call(request);
        });
    }

}
