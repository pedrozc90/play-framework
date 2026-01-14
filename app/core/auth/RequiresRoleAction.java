package core.auth;

import core.exceptions.AppException;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class RequiresRoleAction extends Action<RequiresRole> {

    @Override
    public CompletionStage<Result> call(final Http.Request req) {
        final String method = req.method();
        final String path = req.path();

        final Optional<UserContext> optContext = req.attrs().getOptional(Attrs.USER_CONTEXT);
        if (!optContext.isPresent()) {
            throw AppException.of(401, "Authentication required").toCompletionException();
        }

        final UserContext context = optContext.get();
        final String[] requiredRoles = configuration.value();

        boolean isOk = Arrays.stream(requiredRoles).anyMatch(context::hasRole);
        if (!isOk) {
            throw AppException.of(403, "Ops... You are forbidden to use '%s %s'", method, path).toCompletionException();
        }

        return delegate.call(req);
    }

}
