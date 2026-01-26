package core.play.utils;

import play.mvc.Http;

public class CookieUtils {

    private static final String ACCESS_TOKEN = "AccessToken";

    public static Http.Cookie getCookie(final Http.Request req, final String name) {
        return req.cookie(name);
    }

    public static Http.Cookie getAccessToken(final Http.Request req) {
        return getCookie(req, ACCESS_TOKEN);
    }

    public Http.Cookie createAccessToken(final String name, final String value, final String domain, final int maxAge, final boolean secure, final boolean httpOnly) {
        return Http.Cookie.builder(name, value)
            .withDomain(domain)
            .withMaxAge(maxAge)
            .withPath("/")
            .withSecure(secure)
            .withHttpOnly(httpOnly)
            .build();
    }

}
