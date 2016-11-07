package controllers;

import views.html.index1;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public Result index() {
        /** change the template here to use a different way of compilation and loading of the ts ng2 app.
         * index()  :    does no ts compilation in advance. the ts files are download by the browser and compiled there to js.
         * index1() :    compiles the ts files to individual js files. Systemjs loads the individual files.
         * index2() :    add the option -DtsCompileMode=stage to your sbt task . F.i. 'sbt ~run -DtsCompileMode=stage' this will produce the app as one single js file.
         */
        return ok(index1.render());
    }

}
