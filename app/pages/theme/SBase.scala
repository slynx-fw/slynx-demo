package pages.theme

import java.util.UUID

import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JsCmds.{Replace, Run}
import net.liftweb.http.js.{JsCmd, JsCmds}

import scala.xml.NodeSeq


trait SBase {

  object TextAlign {
    type Align = String
    val Left = ("left": Align)
    val Center = ("center": Align)
    val Right = ("right": Align)
  }

}

