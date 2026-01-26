package web.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Singleton;

@Singleton
public class Application extends Controller {

    public Result index() {
        return ok(index.render("Play Framework Boilerplate", "Your application is ready."));
    }

}
