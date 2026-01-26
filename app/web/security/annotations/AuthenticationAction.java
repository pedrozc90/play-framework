package web.security.annotations;

import application.auth.AuthenticationService;
import core.play.utils.CookieUtils;
import core.play.utils.ResultBuilder;
import core.utils.http.HttpHeaders;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import web.security.objects.Attrs;
import web.security.objects.UserContext;

import javax.inject.Inject;

public class AuthenticationAction extends Action<Authenticated> {

    private final AuthenticationService service;

    @Inject
    public AuthenticationAction(final AuthenticationService service) {
        this.service = service;
    }

    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        final String type = configuration.type().toLowerCase();

        final Http.Request req = ctx.request();
        try {
            final String accessToken = getAccessToken(type, req);
            if (accessToken == null) {
                return F.Promise.pure(ResultBuilder.of("Token is missing").unauthorized());
            }

            final UserContext context = service.validate(accessToken);

            // add user to request attributes (Not ThreadLocal)
            ctx.args.put(Attrs.USER_CONTEXT, context);

            return delegate.call(ctx);
        } finally {
            // clear context
            ctx.args.remove(Attrs.USER_CONTEXT);
        }
    }

    /* HELPERS */
    private String getAccessToken(final String type, final Http.Request req) {
        if (type != null) {
            if (type.equals("cookie")) {
                return getAccessTokenCookie(req);
            }
        }
        return getBearerToken(req);
    }

    private String getHeader(final Http.Request req, final String name) {
        return req.getHeader(name);
    }

    private String getAuthorization(final Http.Request req) {
        return getHeader(req, HttpHeaders.AUTHORIZATION);
    }

    private String getBearerToken(final Http.Request req) {
        final String authorization = getAuthorization(req);
        if (authorization == null) return null;
        return authorization.replace("Bearer", "").trim();
    }

    private String getAccessTokenCookie(final Http.Request req) {
        final Http.Cookie cookie = CookieUtils.getAccessToken(req);
        if (cookie == null) return null;
        return cookie.value();
    }

}
