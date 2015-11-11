package pages.unauthenticated

import java.util.UUID

import controllers.RootController.XSPageHandle
import code.model.{DBS, User}
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds.{Alert, RedirectTo, Run}
import net.liftweb.json.JsonAST.{JArray, JString}
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.liftweb.util.{ClearNodes, Props}
import org.squeryl.PrimitiveTypeMode._
import play.api.Play.current

import scala.xml.NodeSeq

class Login()(implicit val xsh: XSPageHandle) extends (NodeSeq => NodeSeq) {

  override def apply(v1: NodeSeq): NodeSeq = render.apply(v1)

  def render: NodeSeq => NodeSeq = {

    val login = xsh.jsonCall(
      JsRaw("[$('#email-input').val(), $('#password-input').val()]"),
      (_: JValue) match {
        case JArray(List(JString(email), JString(password))) =>
          println("HERE...")
          if (email == "") Alert("Email is required.")
          else if (password == "") Alert("Password is required.")
          else {
            inTransaction {
              DBS.users.where(_.email === email).headOption.foreach(_.login(password))

              User.currentUserOpt.map(user => {
                val newToken = UUID.randomUUID().toString
                user.update(user => user.rememberLogin = newToken)

                Run(s"document.cookie='rememberLogin=${user.rememberLogin}; path=/;'") &
                  RedirectTo(xsh.req.queryString.get("redir").flatMap(_.headOption).getOrElse(xsh.s.redirectTo))
              }).getOrElse({
                net.liftweb.http.js.JsCmds.Alert("Invalid login.")
              })
            }
          }
        case _ => Alert("Please enter your email and password")
      }).toJsCmd

    "#login-btn [onclick]" #> login &
      "#email-input [value]" #> "demo@slynx.org" &
      "#email-input [onkeyup]" #> Run(s"{var e = event || window.event; if ((e.keyCode ? e.keyCode : e.which) == 13) { $login }}") &
      "#password-input [value]" #> "demo" &
      "#password-input [onkeyup]" #> Run(s"{var e = event || window.event; if ((e.keyCode ? e.keyCode : e.which) == 13) { $login }}")
  }
}
