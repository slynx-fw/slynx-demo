package controllers

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import code.model._
import net.coobird.thumbnailator.Thumbnailator
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.http._
import net.liftweb.util.{PassThru, Html5Parser}
import org.slynx.v201510.XSessionSupport
import org.slynx.v201510.monitor.XMonitor
import pages._
import pages.tableexamples._
import pages.unauthenticated.{Register, PasswordReset, Login}
import play.api.Play
import play.api.http.Writeable
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.Play.current

import scala.concurrent.Future
import scala.io.Source
import scala.xml.{Xhtml, NodeSeq}

import scala.util.Try
import scala.xml.NodeSeq
import org.squeryl.PrimitiveTypeMode._

case class State(
                  var user: Option[User] = None,
                  var redirectTo: String = "/",
                  props: collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()
                  )

object RootController extends Controller with XSessionSupport with XMonitor {

  import play.api.libs.concurrent.Execution.Implicits._

  override type S = State
  override def createState() = State()

  val parser = new Html5Parser {}
  def @@(path: String*)(f: NodeSeq => NodeSeq): Option[Future[Result]] = for {
    url <- Play.resource(s"/public/${path.mkString("/")}.html")
    template <- parser.parse(url.openStream()).toOption
    ns = f(template)
  } yield Future(Ok(Xhtml.toXhtml(ns)).as("text/html"))

  def route(header: RequestHeader) = {

    //    println("GET > " + header.path)

    val start = System.currentTimeMillis()

    try {
      net.liftweb.http.Req.parsePath(header.path) match {

        // Slynx:
        case ParsePath("xscall" :: xsId :: Nil, _, _, _) => handleCall(xsId, header)
        case ParsePath(MonitorPath :: _, _, _, _) => handleAdminArea(header)
        //        case ParsePath("xsws" :: xsId :: Nil, _, _, _) => WebSocket.using[String](req => (Iteratee.consume[String](), Enumerator("Hello!") >>> Enumerator.eof))

        case path if header.path.startsWith("/static") => Action.async(implicit req => {
          Assets.at(s"/public", path.partPath.mkString(s"", "/", Some(path.suffix).filter(_ != "").map("." + _).getOrElse(""))).apply(req).flatMap(Future(_))
        })

        case path => Action.async(implicit req => {

          object #/ {def unapply(path: List[String]) = Some(path.mkString("/", "/", ""))}

          lazy val xs = sessionFor(req)
          implicit lazy val xsh = xs.createHandle(req)

          User.currentUserOpt

          def serveFile(p: ParsePath) = Some(Assets.at(s"/public", p.partPath.mkString(s"", "/", Some(p.suffix).filter(_ != "").map("." + _).getOrElse(""))).apply(req))

          (path match {

            // Resources:
            case p@ParsePath("static" :: _, _, _, _) => serveFile(p)

            // Unauthenticated:
            case ParsePath("login" :: Nil, _, _, _) => if (xsh.s.user.isEmpty) @@("pages", "login")(new Login()) else Some(Future[Result](TemporaryRedirect("/")))
            case ParsePath("register" :: Nil, _, _, _) => if (xsh.s.user.isEmpty) @@("pages", "register")(new Register()) else Some(Future[Result](TemporaryRedirect("/")))
            case ParsePath("passwordreset" :: Nil, _, _, _) => if (xsh.s.user.isEmpty) @@("pages", "passwordreset")(new PasswordReset()) else Some(Future[Result](TemporaryRedirect("/")))

            // Test authentication:
            case p@ParsePath(_, "" | "html", _, _) if xsh.s.user.isEmpty => println(s"DENIED> ${p.wholePath.mkString("/", "/", p.suffix)}"); Some(Future[Result](TemporaryRedirect("/login")))
            // Your pages:
            case ParsePath("index" :: Nil, _, _, _) => @@("pages", "default")(new Dashboard())
            case ParsePath(path, _, _, _) if path == pages.Menu.widgets.path => @@("pages", "default")(new WidgetsPage())
            case ParsePath(path, _, _, _) if path == pages.Menu.modals.path => @@("pages", "default")(new ModalsPage())
            case ParsePath(path, _, _, _) if path == pages.Menu.knobs.path => @@("pages", "default")(new KnobPage())
            case ParsePath(path, _, _, _) if path == pages.Menu.toastr.path => @@("pages", "default")(new ToastrNotificationsPage())
            case ParsePath("tables" :: Nil, _, _, _) => @@("pages", "default")(new BasicTablePage())
            case ParsePath("simpletables" :: Nil, _, _, _) => @@("pages", "default")(new FullExampleTablePage())
            case ParsePath("paginationtables" :: Nil, _, _, _) => @@("pages", "default")(new PaginationTablePage())
            case ParsePath("sortabletables" :: Nil, _, _, _) => @@("pages", "default")(new SortableTablePage())
            case ParsePath("searchabletables" :: Nil, _, _, _) => @@("pages", "default")(new SearchableTablePage())
            case ParsePath("selColsTable" :: Nil, _, _, _) => @@("pages", "default")(new SelColsTablePage())
            case ParsePath("rowDetailsOnClickTable" :: Nil, _, _, _) => @@("pages", "default")(new RowDetailsOnClickTablePage())
            case ParsePath("fixedHeaderTable" :: Nil, _, _, _) => @@("pages", "default")(new FixedHeaderTablePage())
            case ParsePath("lazyLoadingTable" :: Nil, _, _, _) => @@("pages", "default")(new LazyLoadingTablePage())
            case ParsePath("sortableRowsTable" :: Nil, _, _, _) => @@("pages", "default")(new SortableRowsTablePage())
            case ParsePath(path, _, _, _) if path == pages.Menu.flotcharts.path => @@("pages", "default")(new FlotChartsPage())
            case ParsePath(path, _, _, _) if path == pages.Menu.forms.path => @@("pages", "default")(new FormPage())
            case ParsePath(path, _, _, _) if path == pages.Menu.buttons.path => @@("pages", "default")(new ButtonPage())
            case ParsePath(path, _, _, _) if path == pages.Menu.emails.path => @@("pages", "default")(new EditorEmailPage())

            // Original AdminLTE pages:
            case ParsePath("adminlte" :: page :: Nil, _, _, _) => @@("pages", "default")(new SimplePage("pages", "adminlte", page))

            // By default just get the asset:
            case ParsePath(path, suf, _, _) => Some({
              val dflt = Assets.at(s"/public", path.mkString(s"", "/", Some(suf).filter(_ != "").map("." + _).getOrElse(""))).apply(req)
              dflt.flatMap({
                case rslt@Result(ResponseHeader(404, _, _), _, _) => Assets.at("/public", path.mkString("", "/", ".html")).apply(req)
                case rslt => Future(rslt)
              })
            })
          }).getOrElse(Future[Result]({
            println("404!> " + req.path)
            NotFound
          }))
            .map(xs.onresponse)
        })
      }
    } finally {
      //      println("GET > " + header.path + s" [${System.currentTimeMillis() - start}ms]")
    }
  }
}

