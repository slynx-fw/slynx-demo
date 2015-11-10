package pages.theme

import controllers.RootController.XSPageHandle
import util.ImplicitHelpers._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds.{Replace, Run}
import net.liftweb.util.Helpers
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq


trait SModals extends Id {


  object ModalStyle extends Enumeration {
    type Style = String

    val Default: Style = ""
    val Primary: Style = "modal-primary"
    val Info: Style = "modal-info"
    val Warning: Style = "modal-warning"
    val Success: Style = "modal-success"
    val Danger: Style = "modal-danger"
  }

  trait Modal extends Id {

    implicit val xsh: XSPageHandle

    val modalId = id('modal)
    val headerId = id('header)
    val bodyId = id('body)
    val footerId = id('footer)

    // Global:
    def modalStyle: ModalStyle.Style
    def modalClasses: List[String] = (if (modalFullscreen) List("fullscreen") else Nil) ::: List("modal", modalStyle + "")
    def modalFullscreen: Boolean = false
    def modalCloseOnEsc: Boolean = true
    def modalCloseOnClickOutside: Boolean = true
    // Header:
    def modalEnableCloseBtn: Boolean = true
    def modalEnableTopRightCloseBtn: Boolean = true
    def modalTitleStr: String
    def modalTitleNs: NodeSeq = scala.xml.Text(modalTitleStr)
    // Body:
    def modalContents: NodeSeq
    // Footer:
    def modalCloseBtnLbl: String = "Close"
    def modalSaveBtnLbl: String = "Save"
    def modalCloseBtn: NodeSeq = if (modalEnableCloseBtn) <button type="button" onclick={modalOnCloseClientSide.toJsCmd} class="btn btn-default pull-left">{modalCloseBtnLbl}</button> else NodeSeq.Empty
    def modalSaveBtn: NodeSeq = <button type="button" onclick={modalOnSaveClientSide.toJsCmd} class="btn btn-primary">{modalSaveBtnLbl}</button>

    def modalOnCloseClientSide: JsCmd = hideAndDestroy()
    def modalOnSaveServerSide(): JsCmd = JsCmds.Noop
    def modalOnSaveClientSide: JsCmd = xsh.ajaxInvoke(() => modalOnSaveServerSide())

    def renderedHeader: NodeSeq = {
      <div id={headerId} class="modal-header">
        {if (modalEnableTopRightCloseBtn) <button type="button" class="close" onclick={modalOnCloseClientSide.toJsCmd} aria-label="Close"><span aria-hidden="true">×</span></button> else NodeSeq.Empty}
        <h4 class="modal-title">{modalTitleNs}</h4>
      </div>
    }

    def rerenderHeader(): JsCmd = Replace(headerId, renderedHeader)

    def renderedBody: NodeSeq = {
      <div id={bodyId} class="modal-body">
        {modalContents}
      </div>
    }

    def rerenderBody(): JsCmd = Replace(bodyId, renderedBody)

    def renderedFooter: NodeSeq = {
      <div id={footerId} class="modal-footer">
        {modalCloseBtn}
        {modalSaveBtn}
      </div>
    }

    def rerenderFooter(): JsCmd = Replace(footerId, renderedFooter)

    def renderedModal: NodeSeq = {
      <div id={modalId} class={modalClasses.mkString(" ")} tabindex="-1">
        <div class="modal-dialog">
          <div class="modal-content">
            {renderedHeader}
            {renderedBody}
            {renderedFooter}
          </div>
        </div>
      </div>
    }

    def rerenderModal(): JsCmd = Replace(modalId, renderedModal)

    def showAndInstall(): JsCmd = Run(s"""$$('body').append(${renderedModal.toString().encJs});""") & showOnly()

    def showOnly(): JsCmd = Run(s"""$$("#$modalId").modal({backdrop: ${if (modalCloseOnClickOutside) modalCloseOnClickOutside else s"'static'"}, keyboard: $modalCloseOnEsc});""")

    def hideAndDestroy(): JsCmd = Run(s"""$$("#$modalId").modal('hide'); $$("#$modalId").on('hidden.bs.modal', function () { $$("#$modalId").remove(); });""")

    def hideOnly(): JsCmd = Run(s"""$$("#$modalId").modal('hide');""")
  }

  abstract class DefaultModal()(implicit val xsh: XSPageHandle) extends Modal

  case class SimpleModal(
                          modalTitleStr: String,
                          modalContentsF: (SimpleModal, XSPageHandle) => NodeSeq,
                          modalStyle: ModalStyle.Style = ModalStyle.Default,
                          override val modalCloseBtnLbl: String = "Close",
                          override val modalSaveBtnLbl: String = "Close",
                          override val modalEnableCloseBtn: Boolean = false,
                          override val modalEnableTopRightCloseBtn: Boolean = true,
                          modalOnSaveServerSideF: (SimpleModal, XSPageHandle) => JsCmd = (_, _) => JsCmds.Noop,
                          modalOnSaveClientSideF: (SimpleModal, XSPageHandle) => JsCmd = (modal, xsh) => xsh.ajaxInvoke(() => modal.modalOnSaveServerSide())
                          )(implicit val xsh: XSPageHandle) extends Modal {

    override def modalContents: NodeSeq = modalContentsF(this, xsh)

    override def modalOnSaveServerSide(): JsCmd = modalOnSaveServerSideF(this, xsh)
    override def modalOnSaveClientSide: JsCmd = modalOnSaveClientSideF(this, xsh)
  }
}
