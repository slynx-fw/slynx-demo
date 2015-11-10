package pages

import controllers.RootController
import controllers.RootController.{XSPageHandle, XSession}
import net.liftweb.util.Html5Parser
import net.liftweb.util.Helpers._
import org.joda.time.{DateTimeZone, DateTime}
import pages.theme.{Styles, Libs}
import play.api.Play
import play.api.Play.current

import scala.collection.mutable.ListBuffer
import scala.xml.{Node, NodeSeq}

trait StandardPage extends Function1[NodeSeq, NodeSeq] {

  implicit val xsh: XSPageHandle
  def pageXsh: XSPageHandle = xsh
  def page = this
  val TH = pages.theme.TH
  def dateTime(millis: Long) = new DateTime(millis).withZone(DateTimeZone.forID("Europe/Lisbon"))

  val html5Parser = new Html5Parser {}
  def @@(path: Seq[String]): NodeSeq = (for {
    url <- Play.resource(s"/public/${path.mkString("/")}.html")
    template <- html5Parser.parse(url.openStream()).toOption
  } yield template).getOrElse(<div style="color:red;">NOT FOUND: /public/{path.mkString("/")}.html</div>)

  def surrundWith: List[String] = List("pages", "default")
  def surroundAt: String = "#page-content"

  def loadHtmlFile(path: String) = (new Html5Parser {}).parse(play.api.Play.current.resource(path).get.openStream()).get

  def libs: Set[Libs.Lib] = {
    import Libs._
    Set(
      jquery_min
      , bootstrap_min
      , jquery_slimscroll_min
      , fastclick_min
      , app_min
      , demo
      , toastr
      , bootstrap_wysihtml5
    )
  }
  def libsInit: NodeSeq = libs.toList.sortBy(s => Libs.indexOf(s)).map(lib => <script src={lib} type="text/javascript"></script>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)

  def styles: Set[Styles.Style] = {
    import Styles._
    Set(
      bootstrap_min
      , font_awesome_min
      , ionicons_min
      , adminLTE_min
      , all_skins
      , custom
      , toastr
      , bootstrap_wysihtml5
    )
  }

  def stylesInit: NodeSeq = {
    styles.toList.sortBy(s => Styles.indexOf(s)).map(style => <link href={style} rel="stylesheet" type="text/css"></link>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty) ++
      scala.xml.Unparsed(
        """<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
          |<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
          |<!--[if lt IE 9]>
          |    <script src="//oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
          |    <script src="//oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
          |<![endif]-->
          |""".stripMargin
      )
  }

  def render: NodeSeq => NodeSeq = {
    "body *+" #> (libsInit ++ <head_merge>{stylesInit}</head_merge>)
  }

  def apply(ns: NodeSeq) = {
    val rslt = scala.xml.Unparsed("<!DOCTYPE html>\n") ++ render(ns)
    var tail = new ListBuffer[Node]
    var head = new ListBuffer[Node]
    val withoutTailAndHead = (
      "head_merge" #> ((ns: NodeSeq) => {head ++= ns.head.child; NodeSeq.Empty}) &
        "tail" #> ((ns: NodeSeq) => {tail ++= ns.head.child; NodeSeq.Empty})
      ).apply(rslt)
    ("head *" #> ((ns: NodeSeq) => ns ++ head) & "body *" #> ((ns: NodeSeq) => ns ++ tail)).apply(withoutTailAndHead)
  }
}
