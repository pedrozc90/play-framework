package core.play.utils;

import play.mvc.Http;

public class CookieUtils {

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    public static Http.Cookie getCookie(final Http.Request req, final String name) {
        return req.cookie(name);
    }

    public static Http.Cookie getAccessToken(final Http.Request req) {
        return getCookie(req, ACCESS_TOKEN);
    }

    public static void setAccessToken(final Http.Response res, final String token) {
        res.setCookie(ACCESS_TOKEN, token);
    }

}
