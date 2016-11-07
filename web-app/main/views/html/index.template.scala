
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index_Scope0 {
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

class index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/("""
"""),format.raw/*2.1*/("""<!doctype html>
<html lang="en" data-framework="angular2">
    <head>
        """),format.raw/*5.99*/("""
    """),format.raw/*6.5*/("""<meta charset="utf-8">
    <title>Angular2 • TodoMVC</title>
    <link rel="stylesheet" href=""""),_display_(/*8.35*/routes/*8.41*/.Assets.versioned("lib/todomvc-common/base.css")),format.raw/*8.89*/("""">
    <link rel="stylesheet" href="assets/stylesheets/todomvc-app.css">
    <script src='"""),_display_(/*10.19*/routes/*10.25*/.Assets.versioned("lib/angular2/bundles/angular2-polyfills.js")),format.raw/*10.88*/("""'></script>
    <script src='"""),_display_(/*11.19*/routes/*11.25*/.Assets.versioned("lib/systemjs/dist/system.js")),format.raw/*11.73*/("""'></script>
    <script src='"""),_display_(/*12.19*/routes/*12.25*/.Assets.versioned("lib/typescript/lib/typescript.js")),format.raw/*12.78*/("""'></script>
    <script src='"""),_display_(/*13.19*/routes/*13.25*/.Assets.versioned("lib/rxjs/bundles/Rx.js")),format.raw/*13.68*/("""'></script>
    <script src='"""),_display_(/*14.19*/routes/*14.25*/.Assets.versioned("lib/angular2/bundles/angular2.dev.js")),format.raw/*14.82*/("""'></script>

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
        <script>
        """),format.raw/*29.101*/("""
			"""),format.raw/*30.4*/("""System.config("""),format.raw/*30.18*/("""{"""),format.raw/*30.19*/("""
                """),format.raw/*31.17*/("""transpiler: 'typescript',
                typescriptOptions: """),format.raw/*32.36*/("""{"""),format.raw/*32.37*/("""
                               """),format.raw/*33.105*/("""
                                """),format.raw/*34.33*/("""emitDecoratorMetadata: true,
                                experimentalDecorators: true
                            """),format.raw/*36.29*/("""}"""),format.raw/*36.30*/(""",
                            packages: """),format.raw/*37.39*/("""{"""),format.raw/*37.40*/("""
                                """),format.raw/*38.33*/("""'assets/app': """),format.raw/*38.47*/("""{"""),format.raw/*38.48*/("""
                                    """),format.raw/*39.37*/("""defaultExtension: 'ts' """),format.raw/*39.123*/("""
                                """),format.raw/*40.33*/("""}"""),format.raw/*40.34*/(""",
                                'assets/lib': """),format.raw/*41.47*/("""{"""),format.raw/*41.48*/("""
                                    """),format.raw/*42.37*/("""defaultExtension: 'js' """),format.raw/*42.102*/("""
                                """),format.raw/*43.33*/("""}"""),format.raw/*43.34*/("""
                            """),format.raw/*44.29*/("""}"""),format.raw/*44.30*/(""",
                            map: """),format.raw/*45.34*/("""{"""),format.raw/*45.35*/("""
                                """),format.raw/*46.33*/("""'app' : 'assets/app' """),format.raw/*46.115*/("""
                            """),format.raw/*47.29*/("""}"""),format.raw/*47.30*/("""
                        """),format.raw/*48.25*/("""}"""),format.raw/*48.26*/(""");
                        System.import('app/bootstrap')
                                .then(null, console.error.bind(console));
                    </script>
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
object index extends index_Scope0.index
              /*
                  -- GENERATED --
                  DATE: Mon Nov 07 17:27:26 CET 2016
                  SOURCE: D:/angular-play-tmp/play-angular2-typescript-java/app/views/index.scala.html
                  HASH: 593762645a4ff9b8b68a7484fac2e5eabef0809e
                  MATRIX: 738->1|834->3|861->4|966->172|997->177|1118->272|1132->278|1200->326|1318->417|1333->423|1417->486|1474->516|1489->522|1558->570|1615->600|1630->606|1704->659|1761->689|1776->695|1840->738|1897->768|1912->774|1990->831|2511->1415|2542->1419|2584->1433|2613->1434|2658->1451|2747->1512|2776->1513|2837->1618|2898->1651|3044->1769|3073->1770|3141->1810|3170->1811|3231->1844|3273->1858|3302->1859|3367->1896|3419->1982|3480->2015|3509->2016|3585->2064|3614->2065|3679->2102|3731->2167|3792->2200|3821->2201|3878->2230|3907->2231|3970->2266|3999->2267|4060->2300|4110->2382|4167->2411|4196->2412|4249->2437|4278->2438
                  LINES: 27->1|32->1|33->2|36->5|37->6|39->8|39->8|39->8|41->10|41->10|41->10|42->11|42->11|42->11|43->12|43->12|43->12|44->13|44->13|44->13|45->14|45->14|45->14|60->29|61->30|61->30|61->30|62->31|63->32|63->32|64->33|65->34|67->36|67->36|68->37|68->37|69->38|69->38|69->38|70->39|70->39|71->40|71->40|72->41|72->41|73->42|73->42|74->43|74->43|75->44|75->44|76->45|76->45|77->46|77->46|78->47|78->47|79->48|79->48
                  -- GENERATED --
              */
          