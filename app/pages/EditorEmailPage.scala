package pages

import net.liftweb.http.js.{JsCmd, JsCmds}
import net.liftweb.util.Helpers._
import controllers.RootController.XSPageHandle
import pages.theme.Libs._
import pages.theme.{Libs, Styles}
import pages.theme.Styles._

import scala.xml.NodeSeq

class EditorEmailPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageTitle: String = "Editor"
  override def pageSubtitle: String = "Editor Examples"
  override def pageCodeFile: String = "app/pages/EditorEmailPage.scala"

  override def render: (NodeSeq) => NodeSeq = super.render andThen {
    ".page-contents" #> {
      TH.Row(
        TH.Col().md12 {
          var email = ""
          new TH.SimpleWidget("Email Editor Example", style = TH.WidgetStyle.Primary)(
            // formExample1
            new TH.DefaultForm() {

              var emailTo = ""
              var emailFor = ""
              var subject = ""


              lazy val emailToF = TextField("To", () => emailTo, s => {emailTo = s; JsCmds.Noop})
              lazy val emailForF = TextField("For", () => emailFor, s => {emailFor = s; JsCmds.Noop})
              lazy val subjectF = TextField("Subject", () => subject, s => {subject = s; JsCmds.Noop})

              override val field: Field =
                VerticalGroup()(
                  emailToF,
                  emailForF,
                  subjectF
                )
            }.rendered
              ++ TH.BootstrapWysiwyg(() => email, style= "width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid rgb(221, 221, 221); padding: 10px;").rendered
            // formExample1
          ).renderedWidget ++
            codeWidget("formExample1")
        }
      )
    }
  }
}