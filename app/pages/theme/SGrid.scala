package pages.theme


import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{Run, Replace}
import net.liftweb.util.Helpers._
import net.liftweb.util.Helpers
import scala.xml.NodeSeq

trait SGrid {

  def Row(col: NodeSeq*): NodeSeq = <div class="row">{col.reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)}</div>

  def Row(style: String = "")(col: NodeSeq*): NodeSeq = <div class="row" style={style}>{col.reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)}</div>


  case class Col(s: String, ns: Col => NodeSeq = _ => NodeSeq.Empty, id: String = Helpers.nextFuncName, style: String = "") {

    def xs1: Col = copy(s = s + " col-xs-1 ")
    def xs2: Col = copy(s = s + " col-xs-2 ")
    def xs3: Col = copy(s = s + " col-xs-3 ")
    def xs4: Col = copy(s = s + " col-xs-4 ")
    def xs5: Col = copy(s = s + " col-xs-5 ")
    def xs6: Col = copy(s = s + " col-xs-6 ")
    def xs7: Col = copy(s = s + " col-xs-7 ")
    def xs8: Col = copy(s = s + " col-xs-8 ")
    def xs9: Col = copy(s = s + " col-xs-9 ")
    def xs10: Col = copy(s = s + " col-xs-10 ")
    def xs11: Col = copy(s = s + " col-xs-11 ")
    def xs12: Col = copy(s = s + " col-xs-12 ")

    def sm1: Col = copy(s = s + " col-sm-1 ")
    def sm2: Col = copy(s = s + " col-sm-2 ")
    def sm3: Col = copy(s = s + " col-sm-3 ")
    def sm4: Col = copy(s = s + " col-sm-4 ")
    def sm5: Col = copy(s = s + " col-sm-5 ")
    def sm6: Col = copy(s = s + " col-sm-6 ")
    def sm7: Col = copy(s = s + " col-sm-7 ")
    def sm8: Col = copy(s = s + " col-sm-8 ")
    def sm9: Col = copy(s = s + " col-sm-9 ")
    def sm10: Col = copy(s = s + " col-sm-10 ")
    def sm11: Col = copy(s = s + " col-sm-11 ")
    def sm12: Col = copy(s = s + " col-sm-12 ")

    def md1: Col = copy(s = s + " col-md-1 ")
    def md2: Col = copy(s = s + " col-md-2 ")
    def md3: Col = copy(s = s + " col-md-3 ")
    def md4: Col = copy(s = s + " col-md-4 ")
    def md5: Col = copy(s = s + " col-md-5 ")
    def md6: Col = copy(s = s + " col-md-6 ")
    def md7: Col = copy(s = s + " col-md-7 ")
    def md8: Col = copy(s = s + " col-md-8 ")
    def md9: Col = copy(s = s + " col-md-9 ")
    def md10: Col = copy(s = s + " col-md-10 ")
    def md11: Col = copy(s = s + " col-md-11 ")
    def md12: Col = copy(s = s + " col-md-12 ")

    def lg1: Col = copy(s = s + " col-lg-1 ")
    def lg2: Col = copy(s = s + " col-lg-2 ")
    def lg3: Col = copy(s = s + " col-lg-3 ")
    def lg4: Col = copy(s = s + " col-lg-4 ")
    def lg5: Col = copy(s = s + " col-lg-5 ")
    def lg6: Col = copy(s = s + " col-lg-6 ")
    def lg7: Col = copy(s = s + " col-lg-7 ")
    def lg8: Col = copy(s = s + " col-lg-8 ")
    def lg9: Col = copy(s = s + " col-lg-9 ")
    def lg10: Col = copy(s = s + " col-lg-10 ")
    def lg11: Col = copy(s = s + " col-lg-11 ")
    def lg12: Col = copy(s = s + " col-lg-12 ")

    def xs_offset1: Col = copy(s = s + "col-xs-offset-1 ")
    def xs_offset2: Col = copy(s = s + "col-xs-offset-2 ")
    def xs_offset3: Col = copy(s = s + "col-xs-offset-3 ")
    def xs_offset4: Col = copy(s = s + "col-xs-offset-4 ")
    def xs_offset5: Col = copy(s = s + "col-xs-offset-5 ")
    def xs_offset6: Col = copy(s = s + "col-xs-offset-6 ")
    def xs_offset7: Col = copy(s = s + "col-xs-offset-7 ")
    def xs_offset8: Col = copy(s = s + "col-xs-offset-8 ")
    def xs_offset9: Col = copy(s = s + "col-xs-offset-9 ")
    def xs_offset10: Col = copy(s = s + "col-xs-offset-10 ")
    def xs_offset11: Col = copy(s = s + "col-xs-offset-11 ")
    def xs_offset12: Col = copy(s = s + "col-xs-offset-12 ")

    def sm_offset1: Col = copy(s = s + "col-sm-offset-1 ")
    def sm_offset2: Col = copy(s = s + "col-sm-offset-2 ")
    def sm_offset3: Col = copy(s = s + "col-sm-offset-3 ")
    def sm_offset4: Col = copy(s = s + "col-sm-offset-4 ")
    def sm_offset5: Col = copy(s = s + "col-sm-offset-5 ")
    def sm_offset6: Col = copy(s = s + "col-sm-offset-6 ")
    def sm_offset7: Col = copy(s = s + "col-sm-offset-7 ")
    def sm_offset8: Col = copy(s = s + "col-sm-offset-8 ")
    def sm_offset9: Col = copy(s = s + "col-sm-offset-9 ")
    def sm_offset10: Col = copy(s = s + "col-sm-offset-10 ")
    def sm_offset11: Col = copy(s = s + "col-sm-offset-11 ")
    def sm_offset12: Col = copy(s = s + "col-sm-offset-12 ")

    def md_offset1: Col = copy(s = s + "col-md-offset-1 ")
    def md_offset2: Col = copy(s = s + "col-md-offset-2 ")
    def md_offset3: Col = copy(s = s + "col-md-offset-3 ")
    def md_offset4: Col = copy(s = s + "col-md-offset-4 ")
    def md_offset5: Col = copy(s = s + "col-md-offset-5 ")
    def md_offset6: Col = copy(s = s + "col-md-offset-6 ")
    def md_offset7: Col = copy(s = s + "col-md-offset-7 ")
    def md_offset8: Col = copy(s = s + "col-md-offset-8 ")
    def md_offset9: Col = copy(s = s + "col-md-offset-9 ")
    def md_offset10: Col = copy(s = s + "col-md-offset-10 ")
    def md_offset11: Col = copy(s = s + "col-md-offset-11 ")
    def md_offset12: Col = copy(s = s + "col-md-offset-12 ")

    def lg_offset1: Col = copy(s = s + "col-lg-offset-1 ")
    def lg_offset2: Col = copy(s = s + "col-lg-offset-2 ")
    def lg_offset3: Col = copy(s = s + "col-lg-offset-3 ")
    def lg_offset4: Col = copy(s = s + "col-lg-offset-4 ")
    def lg_offset5: Col = copy(s = s + "col-lg-offset-5 ")
    def lg_offset6: Col = copy(s = s + "col-lg-offset-6 ")
    def lg_offset7: Col = copy(s = s + "col-lg-offset-7 ")
    def lg_offset8: Col = copy(s = s + "col-lg-offset-8 ")
    def lg_offset9: Col = copy(s = s + "col-lg-offset-9 ")
    def lg_offset10: Col = copy(s = s + "col-lg-offset-10 ")
    def lg_offset11: Col = copy(s = s + "col-lg-offset-11 ")
    def lg_offset12: Col = copy(s = s + "col-lg-offset-12 ")

    def rendered = <div id={id} class={s}>{ns(this)}</div>

    def rerender() = Replace(id, rendered)

    def withContents(contents: => NodeSeq): Col = copy(ns = _ => contents)
    def withContents(contents: Col => NodeSeq): Col = copy(ns = contents)

    def apply(contents: => NodeSeq): NodeSeq = copy(ns = _ => contents).rendered
    def apply(contents: Col => NodeSeq): NodeSeq = copy(ns = contents).rendered
    def hide(): JsCmd = Run(s"$$('#$id').hide()")
    def show(): JsCmd = Run(s"$$('#$id').show()")

    def hidden = copy(style = "display: none;")

    def updateSize(): JsCmd = Run(s"$$('#$id').attr('class', ${s.encJs})")

  }

  object Col {
    def apply() = new Col("", _ => NodeSeq.Empty, Helpers.nextFuncName)
  }

}