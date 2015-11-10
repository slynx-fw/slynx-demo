package pages

import code.model.User
import net.liftweb.util.Helpers._

import scala.xml.{NodeSeq, Text}


trait LoggedInPage extends StandardPage {

  lazy val user = xsh.s.user.get

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    "@user-fullname" #> user.fullName &
      "@signout [onclick]" #> User.logout()
    )
}
