package pages

import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.util.Helpers._
import pages.theme.{Styles, Libs}
import pages.theme.Libs._
import pages.theme.Styles._

import scala.xml.NodeSeq

class ModalsPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile = "app/pages/ModalsPage.scala"
  override def pageTitle: String = "Modals"
  override def pageSubtitle: String = "Sample Modals"

  override def libs: Set[Lib] ={
    import Libs._
    super.libs+ sh_core + sh_brush}
  override def styles: Set[Style] = {
    import Styles._
    super.styles + shCore + shThemeDefault
  }

  override def render: (NodeSeq) => NodeSeq = super.render andThen {
    ".page-contents" #> {
      TH.Row("margin-bottom: 80px;")(
        TH.Col().md6{
          new TH.Widget {
            override def widgetTitle: String = "Simple Modal with 4 lines of code"
            override def widgetBody: NodeSeq = {
              // modal
              val modal = TH.SimpleModal(
                modalTitleStr = "Modal Title",
                modalContentsF = (_, _) => <p>Modal contents...</p>,
                modalOnSaveClientSideF = (modal, _) => modal.hideAndDestroy() & Alert("Close!")
              )
              <div style="text-align: center;">
                {TH.Btn().Primary.lbl("Open Modal").ajax(modal.showAndInstall()).btn}
              </div>
            }
          }.renderedWidget++
            // modal
            codeWidget("modal")
        },
          TH.Col().md6 {
            new TH.Widget {
              override def widgetTitle: String = "Modal with style 'Primary' and no top-right close button"
              override def widgetBody: NodeSeq = {
                // modal1
                val modal = new TH.DefaultModal() {
                  override def modalStyle: TH.ModalStyle.Style = TH.ModalStyle.Primary
                  override def modalTitleStr: String = "Modal Title"
                  override def modalEnableTopRightCloseBtn: Boolean = false
                  override def modalContents: NodeSeq = <p>Modal contents...</p>
                  override def modalOnSaveServerSide(): JsCmd = {
                    hideAndDestroy() & Alert("Saved!")
                  }
                }
                <div style="text-align: center;">
                {TH.Btn().Primary.lbl("Open Modal").ajax(modal.showAndInstall()).btn}
              </div>
              }
            }.renderedWidget++
              // modal1
              codeWidget("modal1")
          }
        )++
        TH.Row("margin-bottom: 80px;")(
          TH.Col().md6 {
            new TH.Widget {
              override def widgetTitle: String = "Modal without close on Esc, and with only 1 button on the footer"
              override def widgetBody: NodeSeq = {
                // modal2
                val modal = new TH.DefaultModal() {
                  override def modalStyle: TH.ModalStyle.Style = TH.ModalStyle.Info
                  override def modalCloseOnEsc: Boolean = false
                  override def modalTitleStr: String = "Modal Title"
                  override def modalSaveBtnLbl: String = "Close"

                  override def modalEnableCloseBtn: Boolean = false
                  override def modalContents: NodeSeq = <p>Modal contents...</p>
                  override def modalOnSaveServerSide(): JsCmd = {
                    hideAndDestroy() & Alert("Close!")
                  }
                }
                <div style="text-align: center;">
                {TH.Btn().Primary.lbl("Open Modal").ajax(modal.showAndInstall()).btn}
              </div>
              }
            }.renderedWidget++
              // modal2
              codeWidget("modal2")
          },
          TH.Col().md6 {
            new TH.Widget {
              override def widgetTitle: String = "Modal without close on click outside (default style)"
              override def widgetBody: NodeSeq = {
                // modal3
                val modal = new TH.DefaultModal() {
                  override def modalStyle: TH.ModalStyle.Style = TH.ModalStyle.Default
                  override def modalCloseOnClickOutside: Boolean = false
                  override def modalTitleStr: String = "Modal Title "

                  override def modalContents: NodeSeq = <p>Modal contents...</p>
                  override def modalOnSaveServerSide(): JsCmd = {
                    hideAndDestroy() & Alert("Saved!")
                  }
                }
                <div style="text-align: center;">
                {TH.Btn().Primary.lbl("Open Modal").ajax(modal.showAndInstall()).btn}
              </div>
              }
            }.renderedWidget++
              // modal3
              codeWidget("modal3")
          }
        )
    }
  }

}
