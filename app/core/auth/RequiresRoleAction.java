package core.auth;

import core.exceptions.AppException;
import core.utils.http.HttpStatus;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Arrays;

public class RequiresRoleAction extends Action<RequiresRole> {

    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final Http.Request req = ctx.request();
        final String method = req.method();
        final String path = req.path();

        final UserContext context = (UserContext) ctx.args.get(Attrs.USER_CONTEXT);
        if (context == null) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        final String[] requiredRoles = configuration.value();

        boolean isOk = Arrays.stream(requiredRoles).anyMatch(context::hasRole);
        if (!isOk) {
            throw AppException.of(HttpStatus.FORBIDDEN, "Ops... You are forbidden to use '%s %s'", method, path);
        }

        return delegate.call(ctx);
    }

}
