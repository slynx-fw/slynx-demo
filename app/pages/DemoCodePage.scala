package pages

import java.io.InputStream
import java.util.zip.ZipInputStream

import code.model.User
import net.liftweb.http.js.JsCmd
import net.liftweb.util.{CssSel, ClearNodes}
import net.liftweb.util.Helpers._
import pages.theme.Libs.Lib
import pages.theme.{Styles, Libs}
import pages.theme.Styles._
import util.ImplicitHelpers.Pipe

import scala.io.Source
import scala.xml.{NodeSeq, Text}

trait DemoCodePage extends StandardPage {


  override def libs: Set[Lib] = {
    import Libs._
    super.libs + sh_core + sh_brush
  }

  override def styles: Set[Style] = {
    import Styles._
    super.styles + shCore + shThemeDefault
  }

  def pageCodeFile: String

  lazy val is: InputStream = this.getClass.getResourceAsStream("/public/app.zip")
  lazy val zipIs: ZipInputStream = new ZipInputStream(is)
  lazy val file: Option[String] = {
    Iterator.continually(zipIs.getNextEntry).takeWhile(_ != null).collectFirst({
      case entry if entry != null && entry.getName == pageCodeFile =>
        Source.fromInputStream(zipIs).getLines().mkString("\n")
    })
  }

  override def render: (NodeSeq) => NodeSeq = super.render andThen {
    file.map(file => {
      ".view-all-code *" #> {
          <button class="btn btn-sm btn-primary pull-right" onclick={xsh.ajaxInvoke(() => new TH.DefaultModal() {
            override def modalStyle: TH.ModalStyle.Style = TH.ModalStyle.Default
            override def modalTitleStr: String = s"File: ${pageCodeFile}"
            // Header:
            override def modalEnableCloseBtn: Boolean = false
            override def modalSaveBtnLbl: String = "Close"
            override def modalOnSaveClientSide: JsCmd = hideAndDestroy()
            override def modalFullscreen: Boolean = true

            override def modalContents: NodeSeq = {
              TH.Highlight(file).render
            }
          }.showAndInstall()).toJsCmd}>
          <span class="fa fa-file-text-o"></span>  &nbsp; View Full Code</button>
        }
    }).getOrElse(".view-all-code" #> ClearNodes)
  }

  def codeWidget(codeId: String) = file.map(file => {
    new TH.Widget {
      override def widgetTitle: String = "Code"
      override def widgetBody: NodeSeq = {
        TH.Highlight(
          file
            .split("\\n")
            .dropWhile(l => !l.contains(s"// $codeId") && !l.contains(s"""<!-- $codeId -->"""))
            .drop(1)
            .takeWhile(l => !l.contains(s"// $codeId") && !l.contains(s"""<!-- $codeId -->"""))
            .mkString("\n")
        ).render
      }
    }.renderedWidget
  }).getOrElse(NodeSeq.Empty)
}