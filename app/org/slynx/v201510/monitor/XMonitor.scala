package org.slynx.v201510.monitor

import net.liftweb.http.ParsePath
import org.slynx.v201510.XSessionSupport
import org.slynx.v201510.pages._
import play.api.mvc._
import scala.concurrent.Future
import scala.io.Source
import scala.xml.{NodeSeq, Xhtml}

trait XMonitor extends XSessionSupport
                       with XMonitorStandardPage
                       with XMonitorMenu
                       with XMonitorDashboardPage
                       with XMonitorMenuPage
                       with XMonitorSessionsPage {

  val MonitorPath = "xadmin"

  override def handleAdminArea(header: RequestHeader): Action[AnyContent] = {

    import play.api.libs.concurrent.Execution.Implicits._

    //    println("GET > " + header.path)

    val start = System.currentTimeMillis()

    def serve(page: NodeSeq) = Some(Future(Ok("<!DOCTYPE html>\n" + Xhtml.toXhtml(page)).as("text/html")))
    def serveJs(file: String) = Some(Future(Ok(Source.fromInputStream(this.getClass.getResourceAsStream(file)).getLines().mkString("\n")).as("application/javascript")))
    def serveCss(file: String) = Some(Future(Ok(Source.fromInputStream(this.getClass.getResourceAsStream(file)).getLines().mkString("\n")).as("text/css")))


    try {
      net.liftweb.http.Req.parsePath(header.path) match {

        case path => Action.async(implicit req => {

          val xs = sessionFor(req)
          implicit lazy val xsh = xs.createHandle(req)

          (path match {

            // Resources:
            //                        case p@ParsePath("static" :: _, _, _, _) => serveFile(p)

            // Unauthenticated:
            //            case ParsePath("login" :: Nil, _, _, _) => if (xsh.s.user.isEmpty) @@("pages", "login")(new Login()) else Some(Future[Result](TemporaryRedirect("/")))
            //            case ParsePath("register" :: Nil, _, _, _) => if (xsh.s.user.isEmpty) @@("pages", "register")(new Register()) else Some(Future[Result](TemporaryRedirect("/")))
            //            case ParsePath("passwordreset" :: Nil, _, _, _) => if (xsh.s.user.isEmpty) @@("pages", "passwordreset")(new PasswordReset()) else Some(Future[Result](TemporaryRedirect("/")))

            // Test authentication:
            //            case p@ParsePath(_, "" | "html", _, _) if xsh.s.user.isEmpty => println(s"DENIED> ${p.wholePath.mkString("/", "/", p.suffix)}"); Some(Future[Result](TemporaryRedirect("/login")))

            case ParsePath(MonitorPath :: "static" :: "app" :: Nil, "js", _, _) => serveJs("/js/app.min.js")
            case ParsePath(MonitorPath :: "static" :: "demo" :: Nil, "js", _, _) => serveJs("/js/demo.js")
            case ParsePath(MonitorPath :: "static" :: "bootstrap3-wysihtml5.all.min.js" :: Nil, _, _, _) => serveJs("/js/bootstrap3-wysihtml5.all.min.js")
            case ParsePath(MonitorPath :: "static" :: "custom" :: Nil, "css", _, _) => serveCss("/css/custom.css")
            case ParsePath(MonitorPath :: "static" :: "adminLTE" :: Nil, "css", _, _) => serveCss("/css/AdminLTE.min.css")
            case ParsePath(MonitorPath :: "static" :: "all_skins" :: Nil, "css", _, _) => serveCss("/css/_all-skins.min.css")

            case ParsePath(MonitorPath :: "index" :: Nil, _, _, _) => serve(new XDashboard().rendered)
            case ParsePath(path, _, _, _) if path.mkString("/", "/", "") == Menu.sessions.link => serve(new XSessionsPage().rendered)


            // By default just get the asset:
            //            case ParsePath(path, suf, _, _) => Some({
            //              val dflt = Assets.at(s"/public", path.mkString(s"", "/", Some(suf).filter(_ != "").map("." + _).getOrElse(""))).apply(req)
            //              dflt.flatMap({
            //                case rslt@Result(ResponseHeader(404, _, _), _, _) => Assets.at("/public", path.mkString("", "/", ".html")).apply(req)
            //                case rslt => Future(rslt)
            //              })
            //            })
          }).getOrElse(Future[Result]({
            println("404!> " + req.path)
            NotFound
          }))
            .map(xs.onresponse(_)(header))
        })
      }
    } finally {
      //      println("GET > " + header.path + s" [${System.currentTimeMillis() - start}ms]")
    }
  }

}
