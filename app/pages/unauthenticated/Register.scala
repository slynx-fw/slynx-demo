package pages.unauthenticated

import java.util.UUID

import controllers.RootController.XSPageHandle
import code.model.{Account, User}
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds.{Alert, RedirectTo, Run}
import net.liftweb.http.{S, SHtml}
import net.liftweb.json.JsonAST.{JArray, JString}
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.liftweb.util.Props
import org.squeryl.PrimitiveTypeMode._

import scala.xml.NodeSeq


class Register() (implicit val xsh: XSPageHandle) extends (NodeSeq => NodeSeq){

  override def apply(v1: NodeSeq): NodeSeq = render.apply(v1)
  def render: NodeSeq => NodeSeq = {

    "@register-btn [onclick]" #> xsh.jsonCall(
      JsRaw(
        """[
          |  $('#first-name-input').val(),
          |  $('#last-name-input').val(),
          |  $('#email-input').val(),
          |  $('#password1-input').val(),
          |  $('#password2-input').val()
          |]""".stripMargin),
      (_: JValue) match {
        case JArray(List(
        JString(firstName),
        JString(lastName),
        JString(email),
        JString(password1),
        JString(password2)
        )) =>
          if (firstName == "") Alert("First name is required.")
          else if (lastName == "") Alert("Last name is required.")
          else if (email == "") Alert("Email is required.")
          else if (password1 == "") Alert("Password is required.")
          else if (password2 == "") Alert("Password is required.")
          else if (password1 != password2) Alert("Passwords must match.")
          else {
            inTransaction {

              new User(new Account().save().id, firstName, lastName, email).setPassword(password1).save().login(password1)

              User.currentUserOpt.map(user => {
                Run(s"document.cookie='rememberLogin=${user.rememberLogin}; path=/;'") &
                  RedirectTo(xsh.req.queryString.get("redir").flatMap(_.headOption).getOrElse(xsh.s.redirectTo))
              }).getOrElse({
                net.liftweb.http.js.JsCmds.Alert("Invalid login.")
              })
            }
          }
        case _ => Alert("All fields are required.")
      })
  }
}
