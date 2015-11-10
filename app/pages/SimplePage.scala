package pages

import controllers.RootController.XSPageHandle
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq

class SimplePage(path: String*)(implicit val xsh: XSPageHandle) extends MenuPage {

  override def pageTitle: String = ""
  override def pageSubtitle: String = ""

  override def render: (NodeSeq) => NodeSeq = super.render andThen (".content-wrapper" #> @@(path))
}
