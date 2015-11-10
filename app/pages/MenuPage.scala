package pages

import java.io.InputStream
import java.util.zip.ZipInputStream

import code.model.User
import net.liftweb.http.js.JsCmd
import net.liftweb.util.{CssSel, ClearNodes}
import net.liftweb.util.Helpers._

import scala.io.Source
import scala.xml.{NodeSeq, Text}

trait MenuPage extends StandardPage with LoggedInPage {
  def pageTitle: String
  def pageSubtitle: String

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".sidebar-menu *" #> {
      <li class="header">MAIN NAVIGATION</li> ++
        Menu.renderedMenu(xsh.req)
    } &
      "@page-title" #> Text(pageTitle) &
      "@page-subtitle" #> Text(pageSubtitle)
    )
}
