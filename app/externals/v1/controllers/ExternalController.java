package externals.v1.controllers;

import core.auth.Authenticated;
import core.auth.RequiresRole;
import core.utils.objects.ResultBuilder;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Singleton;

@Singleton
public class ExternalController extends Controller {

    @Authenticated
    public Result get() {
        return ResultBuilder.of("Sanity Check").ok();
    }

    @Authenticated
    public Result post() {
        return ResultBuilder.of("Sanity Check").ok();
    }

    @Authenticated
    public Result put() {
        return ResultBuilder.of("Sanity Check").ok();
    }

    @Authenticated
    @RequiresRole(value = { "externals" })
    public Result delete(final Http.Request request) {
        return ResultBuilder.of("Sanity Check").status(501);
    }

}
