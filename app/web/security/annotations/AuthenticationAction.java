package web.security.annotations;

import application.auth.AuthenticationService;
import core.exceptions.AppException;
import core.play.utils.ResultBuilder;
import core.utils.http.HttpHeaders;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import web.security.objects.Attrs;
import web.security.objects.UserContext;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthenticationAction extends Action<Authenticated> {

    private static final Logger.ALogger logger = Logger.of(AuthenticationAction.class);

    private final AuthenticationService service;

    @Inject
    public AuthenticationAction(final AuthenticationService service) {
        this.service = service;
    }

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        final Http.Request req = ctx.request();
        try {
            final String accessToken = extractAccessToken(req, configuration.values());
            if (accessToken == null) {
                return CompletableFuture.completedFuture(ResultBuilder.of("Token is missing").unauthorized());
            }

            final UserContext context = service.validate(accessToken);

            // add user to request attributes (Not ThreadLocal)
            ctx.args.put(Attrs.USER_CONTEXT, context);

            return delegate.call(ctx);
        } catch (AppException e) {
            throw e.toCompletionException();
        } finally {
            // clear context
            ctx.args.remove(Attrs.USER_CONTEXT);
        }
    }

    private String extractAccessToken(final Http.Request request, final AuthSource[] sources) {
        for (AuthSource source : sources) {
            final String t = extractAccessToken(request, source);
            if (t != null && !t.isEmpty()) return t;
        }
        return null;
    }

    private String extractAccessToken(final Http.Request request, final AuthSource source) {
        if (source == null) return null;
        switch (source) {
            case BEARER:
                return extractBearer(request);
            case COOKIE:
                return extractCookie(request);
            default:
                return null;
        }
    }

    /* --- Headers --- */
    private String getHeader(final Http.Request req, final String name) {
        return req.getHeader(name);
    }

    private String getAuthorization(final Http.Request req) {
        return getHeader(req, HttpHeaders.AUTHORIZATION);
    }

    protected String extractBearer(final Http.Request request) {
        final String authorization = getAuthorization(request);
        if (authorization == null || authorization.isEmpty()) {
            logger.trace("Authorization header is missing");
            return null;
        }

        if (authorization.startsWith("Bearer ")) {
            return authorization.substring("Bearer ".length()).trim();
        } else {
            logger.trace("Authorization header is not 'Bearer'");
        }

        return null;
    }

    /* --- Cookies --- */
    protected String extractCookie(final Http.Request request) {
        final Http.Cookie cookie = service.captureCookie(request);
        if (cookie == null) {
            logger.trace("Cookie '{}' is missing", AuthenticationService.COOKIE_NAME);
            return null;
        }
        return cookie.value();
    }

}
