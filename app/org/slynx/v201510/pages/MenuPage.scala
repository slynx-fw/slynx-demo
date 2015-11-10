package org.slynx.v201510.pages

import net.liftweb.util.Helpers._
import org.slynx.v201510.monitor.XMonitor
import scala.xml.{NodeSeq, Text}


trait XMonitorMenuPage {
  self: XMonitor =>

  trait MenuPage extends StandardPage {

    override def render: (NodeSeq) => NodeSeq = super.render andThen (
      ".sidebar-menu *" #> {
        <li class="header">MAIN NAVIGATION</li> ++
          Menu.renderedMenu(xsh.req)
      })
  }

}

