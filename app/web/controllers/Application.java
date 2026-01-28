package web.controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class Application extends Controller {

    public CompletionStage<Result> index() {
        return CompletableFuture.completedFuture(
            ok(index.render("Play Framework Boilerplate", "Your application is ready."))
        );
    }

}
