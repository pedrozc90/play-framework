package core.auth;

import core.exceptions.AppException;
import core.utils.http.HttpStatus;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.AuthenticationService;

import javax.inject.Inject;

public class AuthenticationAction extends Action<Authenticated> {

    private final AuthenticationService service;

    @Inject
    public AuthenticationAction(final AuthenticationService service) {
        this.service = service;
    }

    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final Http.Request req = ctx.request();

        final Http.Cookie cookie = req.cookie("TOKEN");

        final String authorization = req.getHeader("Authorization");
        if (authorization == null) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, "Authentication header is missing");
        }

        final String token = authorization.replace("Bearer ", "");
        if (token.isEmpty()) {
            throw AppException.of(HttpStatus.UNAUTHORIZED, "Bearer token is required");
        }

        final UserContext context = service.validate(token);

        // add user to request attributes (Not ThreadLocal)
        ctx.args.put(Attrs.USER_CONTEXT, context);

        return delegate.call(ctx);
    }

}
