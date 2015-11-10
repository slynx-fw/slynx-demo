package pages.unauthenticated

import java.util.{Properties, UUID}
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Transport, Message, PasswordAuthentication, Session}

import code.model.{User, DBS}
import controllers.RootController.XSPageHandle
import net.liftweb.http.SHtml
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmds.{RedirectTo, Run, Alert}
import net.liftweb.json.JsonAST.{JArray, JString}
import net.liftweb.json._
import net.liftweb.util.Helpers._
import net.liftweb.util.Mailer._
import net.liftweb.util.{Mailer, Props}
import org.squeryl.PrimitiveTypeMode._

import scala.xml.NodeSeq


class PasswordReset()(implicit val xsh: XSPageHandle) extends (NodeSeq => NodeSeq) {

  override def apply(v1: NodeSeq): NodeSeq = render.apply(v1)

  def render: NodeSeq => NodeSeq = {

    "@passwordreset-btn [onclick]" #> xsh.jsonCall(
      JsRaw("[$('#email-input').val()]"),
      (_: JValue) match {
        case JArray(List(JString(email))) =>
          inTransaction {
            DBS.users.where(_.email === email).headOption.map(user => {
              val newPassWord = UUID.randomUUID().toString.take(8)
              user.update(user => user.setPassword(newPassWord))

              new Thread("PW Reset") {
                override def run(): Unit = {
                  try {
                    val username = "vasco.silva.noreply@gmail.com";
                    val password = "d7046be4-3d0f-11e5-9a8b-d0509954bbac";
                    val props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");


                    val session = Session.getInstance(props, new javax.mail.Authenticator() {
                      override def getPasswordAuthentication(): PasswordAuthentication = {
                        new PasswordAuthentication(username, password)
                      }
                    })
                    val message = new MimeMessage(session)
                    message.setFrom(new InternetAddress(email))
                    message.setRecipients(Message.RecipientType.TO, email)
                    message.setSubject("[Demo Slynx] Password Reset")
                    message.setText(
                      s"""Hi,
                        |
                        |Here is your new password to the Demo Slynx APP platform: ${newPassWord}
                        |
                        |--Demo Slynx APP
                        |""".stripMargin
                    )
                    Transport.send(message)
                  } catch {
                    case e: Exception =>
                      e.printStackTrace()
                  }
                }
              }.start()

              RedirectTo("/")
            }).getOrElse(Alert("Email not found"))
          }
        case _ => Alert("Field is required.")
      })
  }
}
