package controllers;

import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    public Result indexWithView(Integer param) {
        return index();
    }

    public Result indexWithResult(Integer param) {
        return index();
    }

    public Result indexWithString(String param) {
        return index();
    }

    /**
     *
     * @return Page with front-end app.
     */
    public Result index() {
        return ok(index.render("Surveys"));
    }

}
