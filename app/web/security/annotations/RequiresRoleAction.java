package web.security.annotations;

import core.play.utils.ResultBuilder;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import web.security.objects.Attrs;
import web.security.objects.UserContext;

import java.util.Arrays;

public class RequiresRoleAction extends Action<RequiresRole> {

    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final Http.Request req = ctx.request();
        final String method = req.method();
        final String path = req.path();

        final UserContext context = (UserContext) ctx.args.get(Attrs.USER_CONTEXT);
        if (context == null) {
            return F.Promise.pure(ResultBuilder.of("Authentication required").unauthorized());
        }

        final String[] requiredRoles = configuration.value();

        boolean isOk = Arrays.stream(requiredRoles).anyMatch(context::hasRole);
        if (!isOk) {
            return F.Promise.pure(
                ResultBuilder.of()
                    .message("Ops... You are forbidden to use '%s %s'", method, path)
                    .forbidden()
            );
        }

        return delegate.call(ctx);
    }

}
