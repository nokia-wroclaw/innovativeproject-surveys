
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._


     object index1_Scope0 {
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

class index1 extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.4*/("""
"""),format.raw/*2.1*/("""<!doctype html>
<html lang="en" data-framework="angular2">
    <head>
        """),format.raw/*6.47*/("""
        """),format.raw/*7.9*/("""<meta charset="utf-8">
        <title>Angular2 • TodoMVC</title>
        <link rel="stylesheet" href=""""),_display_(/*9.39*/routes/*9.45*/.Assets.versioned("lib/todomvc-common/base.css")),format.raw/*9.93*/("""">
        <link rel="stylesheet" href="assets/stylesheets/todomvc-app.css">
        <script type='text/javascript' src='"""),_display_(/*11.46*/routes/*11.52*/.Assets.versioned("lib/systemjs/dist/system-polyfills.js")),format.raw/*11.110*/("""'></script>
        <script type='text/javascript' src='"""),_display_(/*12.46*/routes/*12.52*/.Assets.versioned("lib/core-js/client/shim.min.js")),format.raw/*12.103*/("""'></script>
        <script type='text/javascript' src='"""),_display_(/*13.46*/routes/*13.52*/.Assets.versioned("lib/zone.js/dist/zone.js")),format.raw/*13.97*/("""'></script>
        <script type='text/javascript' src='"""),_display_(/*14.46*/routes/*14.52*/.Assets.versioned("lib/reflect-metadata/Reflect.js")),format.raw/*14.104*/("""'></script>
        <script type='text/javascript' src='"""),_display_(/*15.46*/routes/*15.52*/.Assets.versioned("lib/systemjs/dist/system.src.js")),format.raw/*15.104*/("""'></script>

        <script>    """),format.raw/*18.59*/("""
            """),format.raw/*19.13*/("""var map = """),format.raw/*19.23*/("""{"""),format.raw/*19.24*/("""
                """),format.raw/*20.17*/("""'app': 'assets/app',
                '@angular':'assets/lib/@angular',
                'rxjs':'assets/lib/rxjs',
                'symbol-observable': 'assets/lib/symbol-observable'

            """),format.raw/*25.13*/("""}"""),format.raw/*25.14*/(""";


        var packages = """),format.raw/*28.24*/("""{"""),format.raw/*28.25*/("""
                """),format.raw/*29.17*/("""'app': """),format.raw/*29.24*/("""{"""),format.raw/*29.25*/("""main: 'main.js', defaultExtension: 'js'"""),format.raw/*29.64*/("""}"""),format.raw/*29.65*/(""",
                'rxjs': """),format.raw/*30.25*/("""{"""),format.raw/*30.26*/("""defaultExtension: 'js'"""),format.raw/*30.48*/("""}"""),format.raw/*30.49*/(""",
                'assets/lib': """),format.raw/*31.31*/("""{"""),format.raw/*31.32*/("""defaultExtension: 'js'"""),format.raw/*31.54*/("""}"""),format.raw/*31.55*/(""",
                'symbol-observable': """),format.raw/*32.38*/("""{"""),format.raw/*32.39*/("""defaultExtension: 'js', main: 'index.js'"""),format.raw/*32.79*/("""}"""),format.raw/*32.80*/("""
            """),format.raw/*33.13*/("""}"""),format.raw/*33.14*/(""";
        var ngPackageNames = [
                'common',
                'compiler',
                'core',
                'forms',
                'http',
                'platform-browser',
                'platform-browser-dynamic',
                'router'
            ];

        function packIndex(pkgName) """),format.raw/*45.37*/("""{"""),format.raw/*45.38*/("""
          """),format.raw/*46.11*/("""packages['@angular/'+pkgName] = """),format.raw/*46.44*/("""{"""),format.raw/*46.45*/(""" """),format.raw/*46.46*/("""main: 'index.js', defaultExtension: 'js' """),format.raw/*46.87*/("""}"""),format.raw/*46.88*/(""";
        """),format.raw/*47.9*/("""}"""),format.raw/*47.10*/("""
        """),format.raw/*48.9*/("""function packUmd(pkgName) """),format.raw/*48.35*/("""{"""),format.raw/*48.36*/("""
          """),format.raw/*49.11*/("""packages['@angular/'+pkgName] = """),format.raw/*49.44*/("""{"""),format.raw/*49.45*/(""" """),format.raw/*49.46*/("""main: '/bundles/' + pkgName + '.umd.js', defaultExtension: 'js' """),format.raw/*49.110*/("""}"""),format.raw/*49.111*/(""";
        """),format.raw/*50.9*/("""}"""),format.raw/*50.10*/("""
          """),format.raw/*51.11*/("""// Most environments should use UMD; some (Karma) need the individual index files
        function addAngularModulesToMap(pkgName) """),format.raw/*52.50*/("""{"""),format.raw/*52.51*/("""
          """),format.raw/*53.11*/("""map['@angular/'+pkgName] = 'assets/lib/angular__' + pkgName;
        """),format.raw/*54.9*/("""}"""),format.raw/*54.10*/("""

          """),format.raw/*56.11*/("""// Add package entries for angular packages
        var setPackageConfig = System.packageWithIndex ? packIndex : packUmd;
        ngPackageNames.forEach(setPackageConfig)

          // Add map entries for angular packages
        ngPackageNames.forEach(function(pkgName)"""),format.raw/*61.49*/("""{"""),format.raw/*61.50*/("""
           """),format.raw/*62.12*/("""addAngularModulesToMap(pkgName)
        """),format.raw/*63.9*/("""}"""),format.raw/*63.10*/(""");

        System.config("""),format.raw/*65.23*/("""{"""),format.raw/*65.24*/("""
            """),format.raw/*66.13*/("""map : map,
            packages: packages,
        """),format.raw/*68.9*/("""}"""),format.raw/*68.10*/(""");
        """),format.raw/*69.110*/("""
        """),format.raw/*70.9*/("""System.import('app')
            .catch(console.error.bind(console));

		</script>
    </head>
    <body>
        <todo-app></todo-app>
        <footer class="info">
            <p>Double-click to edit a todo</p>
            <p>
				Created by <a href="http://github.com/samccone">Sam Saccone</a> and <a href="http://github.com/colineberhardt">Colin Eberhardt</a>
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
object index1 extends index1_Scope0.index1
              /*
                  -- GENERATED --
                  DATE: Mon Nov 07 17:27:26 CET 2016
                  SOURCE: D:/angular-play-tmp/play-angular2-typescript-java/app/views/index1.scala.html
                  HASH: f90d3d8fdb4c6d9bb8c10ed811c108d47191293f
                  MATRIX: 740->1|836->3|863->4|968->224|1003->233|1132->336|1146->342|1214->390|1363->512|1378->518|1458->576|1542->633|1557->639|1630->690|1714->747|1729->753|1795->798|1879->855|1894->861|1968->913|2052->970|2067->976|2141->1028|2202->1187|2243->1200|2281->1210|2310->1211|2355->1228|2577->1424|2606->1425|2661->1452|2690->1453|2735->1470|2770->1477|2799->1478|2866->1517|2895->1518|2949->1544|2978->1545|3028->1567|3057->1568|3117->1600|3146->1601|3196->1623|3225->1624|3292->1663|3321->1664|3389->1704|3418->1705|3459->1718|3488->1719|3833->2036|3862->2037|3901->2048|3961->2081|3990->2082|4019->2083|4088->2124|4117->2125|4154->2135|4183->2136|4219->2145|4273->2171|4302->2172|4341->2183|4401->2216|4430->2217|4459->2218|4552->2282|4582->2283|4619->2293|4648->2294|4687->2305|4846->2436|4875->2437|4914->2448|5010->2518|5039->2519|5079->2531|5377->2801|5406->2802|5446->2814|5513->2854|5542->2855|5596->2881|5625->2882|5666->2895|5744->2946|5773->2947|5813->3059|5849->3068
                  LINES: 27->1|32->1|33->2|36->6|37->7|39->9|39->9|39->9|41->11|41->11|41->11|42->12|42->12|42->12|43->13|43->13|43->13|44->14|44->14|44->14|45->15|45->15|45->15|47->18|48->19|48->19|48->19|49->20|54->25|54->25|57->28|57->28|58->29|58->29|58->29|58->29|58->29|59->30|59->30|59->30|59->30|60->31|60->31|60->31|60->31|61->32|61->32|61->32|61->32|62->33|62->33|74->45|74->45|75->46|75->46|75->46|75->46|75->46|75->46|76->47|76->47|77->48|77->48|77->48|78->49|78->49|78->49|78->49|78->49|78->49|79->50|79->50|80->51|81->52|81->52|82->53|83->54|83->54|85->56|90->61|90->61|91->62|92->63|92->63|94->65|94->65|95->66|97->68|97->68|98->69|99->70
                  -- GENERATED --
              */
          