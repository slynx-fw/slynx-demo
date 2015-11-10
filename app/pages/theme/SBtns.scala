package pages.theme

import java.util.UUID

import controllers.RootController.{XSPageHandle, XSession}
import util.ImplicitHelpers._
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmds.{Run, Replace}
import net.liftweb.http.js.{JsCmd, JsCmds}

import scala.xml.NodeSeq


trait SBtns extends SIcons {

  case class Btn(
                  clas: String,
                  style: String = "",
                  cont: String = "",
                  onclickJs: JsCmd = JsCmds.Noop,
                  ajaxJsOpt: Option[() => JsCmd] = None,
                  id: String = UUID.randomUUID().toString,
                  icnOpt: Option[Icon] = None,
                  hrefOpt: Option[String] = None,
                  center: Boolean = false
                  )(implicit val xsh: XSPageHandle) {

    def App: Btn = copy(clas = clas + " btn btn-app")
    def Default: Btn = copy(clas = clas + " btn btn-default")
    def Primary: Btn = copy(clas = clas + " btn btn-primary ")
    def Info: Btn = copy(clas = clas + " btn btn-info ")
    def Warning: Btn = copy(clas = clas + " btn btn-warning ")
    def Success: Btn = copy(clas = clas + " btn btn-success ")
    def Danger: Btn = copy(clas = clas + " btn btn-danger ")

    def withClass(cs: String): Btn = copy(clas = clas + cs)

    def lg: Btn = copy(clas = clas + " btn-lg")
    def sm: Btn = copy(clas = clas + " btn-sm")
    def xs: Btn = copy(clas = clas + " btn-xs")
    def flat: Btn = copy(clas = clas + " btn-flat")
    def block: Btn = copy(clas = clas + " btn-block")
    def disabled: Btn = copy(clas = clas + " disabled")
    def margin: Btn = copy(clas = clas + " margin")
    def right: Btn = copy(clas = clas + " pull-right")

    def Maroon: Btn = copy(clas = clas + " btn bg-maroon")
    def Purple: Btn = copy(clas = clas + " btn bg-purple")
    def Navy: Btn = copy(clas = clas + " btn bg-navy")
    def Orange: Btn = copy(clas = clas + " btn bg-orange")
    def Olive: Btn = copy(clas = clas + " btn bg-olive")


    def withStyle(sstyle: String): Btn = copy(style = sstyle)

    def lbl(scont: String): Btn = copy(cont = scont)

    def onclick(cmd: JsCmd): Btn = copy(onclickJs = cmd & onclickJs)

    def href(to: String): Btn = copy(hrefOpt = Some(to))

    def ajax(ajaxf: => JsCmd): Btn = copy(ajaxJsOpt = Some(() => ajaxf))

    def icn(icn: Icon): Btn = copy(icnOpt = Some(icn))

    def centered: Btn = copy(center = true)

    def btn: NodeSeq = {
      <button id={id}  onclick={(onclickJs & ajaxJsOpt.map(ajaxJs => xsh.ajaxInvoke(ajaxJs)).getOrElse(JsCmds.Noop)).toJsCmd} style={(if (center) "margin: 0 auto; display: block" else "") + style} class={clas}>{icnOpt.map(_.icn ++ <span> </span>).getOrElse(NodeSeq.Empty)}{cont}</button>
    }

    def link: NodeSeq = {
      <a href={hrefOpt.getOrElse("javascript:void(0)")} id={id} onclick={(onclickJs & ajaxJsOpt.map(ajaxJs => xsh.ajaxInvoke(ajaxJs)).getOrElse(JsCmds.Noop)).toJsCmd} style={style} class={clas}>{icnOpt.map(_.icn ++ <span> </span>).getOrElse(NodeSeq.Empty)}{cont}</a>
    }

    def replace() = Replace(id, btn)

    def toggle(get: => Boolean, set: Boolean => JsCmd, selected: Btn => Btn, unselected: Btn => Btn): NodeSeq = {
      val isSelected = get
      (if (isSelected) selected(this) else unselected(this)).ajax({
        set(!isSelected) & Replace(id, toggle(get, set, selected, unselected))
      }).btn
    }

    def changeToDefault: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger')")
    def changeToPrimary: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-primary')")
    def changeToInfo: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-info')")
    def changeToWarning: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-warning')")
    def changeToSuccess: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-success')")
    def changeToDanger: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-danger')")
  }

  object Btn {
    def apply()(implicit xsh: XSPageHandle) = new Btn("")
  }

}

