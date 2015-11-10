package pages.theme

import util.ImplicitHelpers._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq


trait SToastr {

  case class Toastr private[theme](
                                    protected val clas: String,
                                    protected val title: String = "",
                                    protected val mesg: String = "",
                                    protected val pos: Option[String] = None,
                                    protected val showEasing: Option[String] = None,
                                    protected val hideEasing: Option[String] = None,
                                    protected val showMethod: Option[String] = None,
                                    protected val hideMethod: Option[String] = None,
                                    protected val _newestOnTop: Option[Boolean] = None,
                                    protected val _preventDuplicates: Option[Boolean] = None,
                                    protected val _closeBtn: Option[Boolean] = None,
                                    protected val _progressBar: Option[Boolean] = None,
                                    protected val duration: Option[Long] = None,
                                    protected val hideDuration: Option[Long] = None,
                                    protected val timeout: Option[Long] = None,
                                    protected val extendedTimeout: Option[Long] = None
                                    ) {

    def withTitle(title: String): Toastr = copy(title = title)

    def withMesg(s: String): Toastr = copy(mesg = s)

    def withPosTopRight: Toastr = copy(pos = Some("toast-top-right"))
    def withPosBottomRight: Toastr = copy(pos = Some("toast-bottom-right"))
    def withPosBottomLeft: Toastr = copy(pos = Some("toast-bottom-left"))
    def withPosTopLeft: Toastr = copy(pos = Some("toast-top-left"))
    def withPosTopFullWidth: Toastr = copy(pos = Some("toast-top-full-width"))
    def withPosBottomFullWidth: Toastr = copy(pos = Some("toast-bottom-full-width"))
    def withPosTopCenter: Toastr = copy(pos = Some("toast-top-center"))
    def withPosBottomCenter: Toastr = copy(pos = Some("toast-bottom-center"))


    def closeBtn(v: Boolean): Toastr = copy(_closeBtn = Some(v))
    def progressBar(v: Boolean): Toastr = copy(_progressBar = Some(v))
    def newestOnTop(v: Boolean): Toastr = copy(_newestOnTop = Some(v))
    def preventDuplicates(v: Boolean): Toastr = copy(_preventDuplicates = Some(v))

    def withDuration(v: Long): Toastr = copy(duration = Some(v))
    def withTimeout(v: Long): Toastr = copy(timeout = Some(v))
    def withHideDuration(v: Long): Toastr = copy(hideDuration = Some(v))
    def withExtendedTimeout(v: Long): Toastr = copy(extendedTimeout = Some(v))

    def showEasingLinear: Toastr = copy(showEasing = Some("linear"))
    def showEasingSwing: Toastr = copy(showEasing = Some("swing"))

    def hideEasingLinear: Toastr = copy(hideEasing = Some("linear"))
    def hideEasingSwing: Toastr = copy(hideEasing = Some("swing"))

    def showMethodFadeIn: Toastr = copy(showMethod = Some("fadeIn"))
    def showMethodSlideDown: Toastr = copy(showMethod = Some("slideDown"))
    def showMethodShow: Toastr = copy(showMethod = Some("show"))

    def hideMethodFadeOut: Toastr = copy(hideMethod = Some("fadeOut"))
    def hideMethodSlideUp: Toastr = copy(hideMethod = Some("slideUp"))
    def hideMethodShow: Toastr = copy(hideMethod = Some("hide"))

    def show(): JsCmd = {
      Run({
        val options = List(
          _newestOnTop.map(v => "newestOnTop: " + v)
          , _closeBtn.map(v => "closeButton: " + v)
          , _progressBar.map(v => "progressBar: " + v)
          , pos.map(v => "positionClass: " + v.toString.encJs)
          , _preventDuplicates.map(v => "preventDuplicates: " + v)
          , duration.map(v => "showDuration: " + v)
          , hideDuration.map(v => "hideDuration: " + v)
          , timeout.map(v => "timeOut: " + v)
          , extendedTimeout.map(v => "extendedTimeOut: " + v)
          , showEasing.map(v => "showEasing: " + v.toString.encJs)
          , hideEasing.map(v => "hideEasing: " + v.toString.encJs)
          , showMethod.map(v => "showMethod: " + v.toString.encJs)
          , hideMethod.map(v => "hideMethod: " + v.toString.encJs)
        ).flatten.reduceOption(_ + ",\n" + _).getOrElse("")

        s"""toastr.options = {$options}; toastr[${clas.encJs}](${mesg.encJs},${title.encJs});"""
      })
    }
  }

  object Toastr {
    def Info = Toastr("info")
    def Info(title: String, mesg: String) = Toastr("info", title, mesg)

    def Warning = Toastr("warning")
    def Warning(title: String, mesg: String) = Toastr("warning", title, mesg)

    def Success = Toastr("success")
    def Success(title: String, mesg: String) = Toastr("success", title, mesg)

    def Error = Toastr("error")
    def Error(title: String, mesg: String) = Toastr("error", title, mesg)

  }

}

