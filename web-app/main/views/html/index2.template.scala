
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index2_Scope0 {
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._

class index2 extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/("""
"""),format.raw/*2.1*/("""<!doctype html>
<html lang="en" data-framework="angular2">
    <head>           """),format.raw/*4.95*/("""
        """),format.raw/*5.9*/("""<meta charset="utf-8">
        <title>Angular2 • TodoMVC</title>
        <link rel="stylesheet" href=""""),_display_(/*7.39*/routes/*7.45*/.Assets.versioned("lib/todomvc-common/base.css")),format.raw/*7.93*/("""">
        <link rel="stylesheet" href="assets/stylesheets/todomvc-app.css">
        <script src='"""),_display_(/*9.23*/routes/*9.29*/.Assets.versioned("lib/angular2/bundles/angular2-polyfills.js")),format.raw/*9.92*/("""'></script>
        <script src='"""),_display_(/*10.23*/routes/*10.29*/.Assets.versioned("lib/systemjs/dist/system.js")),format.raw/*10.77*/("""'></script>
        <script src='"""),_display_(/*11.23*/routes/*11.29*/.Assets.versioned("lib/rxjs/bundles/Rx.js")),format.raw/*11.72*/("""'></script>
        <script src='"""),_display_(/*12.23*/routes/*12.29*/.Assets.versioned("lib/angular2/bundles/angular2.dev.js")),format.raw/*12.86*/("""'></script>

        """),format.raw/*15.108*/("""
        """),format.raw/*16.9*/("""<script src='"""),_display_(/*16.23*/routes/*16.29*/.Assets.versioned("main.js")),format.raw/*16.57*/("""'></script>

        <script>

            """),format.raw/*20.93*/("""
                """),format.raw/*21.17*/("""System.import('app/bootstrap')
                        .catch(console.error.bind(console));

        </script>
    </head>
    <body>
        <todo-app></todo-app>
        <footer class="info">
            <p>Double-click to edit a todo</p>
            <p>
                Created by <a href="http://github.com/samccone">Sam Saccone</a>
                and <a href="http://github.com/colineberhardt">Colin Eberhardt</a>
                using <a href="http://angular.io">Angular2</a>
            </p>
            <p>Part of <a href="http://todomvc.com">TodoMVC</a></p>
        </footer>
    </body>
</html>
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


}

/**/
object index2 extends index2_Scope0.index2
              /*
                  -- GENERATED --
                  DATE: Mon Nov 07 17:27:26 CET 2016
                  SOURCE: D:/angular-play-tmp/play-angular2-typescript-java/app/views/index2.scala.html
                  HASH: bfc9a8a4f0c73bad7accd5edd46c48b4b72eecb6
                  MATRIX: 740->1|836->3|863->4|970->157|1005->166|1134->269|1148->275|1216->323|1341->422|1355->428|1438->491|1499->525|1514->531|1583->579|1644->613|1659->619|1723->662|1784->696|1799->702|1877->759|1927->961|1963->970|2004->984|2019->990|2068->1018|2139->1141|2184->1158
                  LINES: 27->1|32->1|33->2|35->4|36->5|38->7|38->7|38->7|40->9|40->9|40->9|41->10|41->10|41->10|42->11|42->11|42->11|43->12|43->12|43->12|45->15|46->16|46->16|46->16|46->16|50->20|51->21
                  -- GENERATED --
              */
          