package externals.v1.controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Singleton;

@Singleton
public class ExternalController extends Controller {

    public Result get() {
        return ok();
    }

    public Result post() {
        return ok();
    }

    public Result put() {
        return ok();
    }

    public Result delete(final Http.Request request) {
        // throw new UnsupportedOperationException("Method Not Implemented Yet.");
        return TODO(request);
    }

}
