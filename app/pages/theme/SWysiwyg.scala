package pages.theme

import controllers.RootController.XSPageHandle
import net.liftweb.http.SHtml.ElemAttr
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd, JsCmds, JsExp}
import net.liftweb.util.Helpers

import scala.xml.NodeSeq


trait SWysiwyg {


  case class BootstrapWysiwyg(
                               get: () => String,
                               fontStyles: Boolean = true,
                               emphasis: Boolean = true,
                               lists: Boolean = true,
                               html: Boolean = true,
                               link: Boolean = true,
                               image: Boolean = true,
                               color: Boolean = true,
                               style: String = "",
                               onLoadOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onBeforeloadOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onFocusOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onFocusComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onFocusTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onBlurOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onBlurComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onBlurTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onChangeOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onChangeComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onChangeTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onPasteOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onPasteComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onPasteTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onNewwordComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onDestroyComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onChange_viewOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onShowDialogOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onSaveDialogOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onCancelDialogOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onUndoComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onRedoComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onBeforecommandComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               onAftercommandComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                               textareaId: String = Helpers.nextFuncName,
                               attrs: Seq[ElemAttr] = Nil
                               )(implicit xsh: XSPageHandle) {

    def onLoad(cmd: JsCmd) = copy(onLoadOpt = Some(cmd))
    def onBeforeload(cmd: JsCmd) = copy(onBeforeloadOpt = Some(cmd))
    def onFocus(cmd: JsCmd) = copy(onFocusOpt = Some(cmd))
    def onFocusComposer(cmd: JsCmd) = copy(onFocusComposerOpt = Some(cmd))
    def onFocusTextarea(cmd: JsCmd) = copy(onFocusTextareaOpt = Some(cmd))
    def onBlur(cmd: JsCmd) = copy(onBlurOpt = Some(cmd))
    def onBlurComposer(cmd: JsCmd) = copy(onBlurComposerOpt = Some(cmd))
    def onBlurTextarea(cmd: JsCmd) = copy(onBlurTextareaOpt = Some(cmd))
    def onChange(cmd: JsCmd) = copy(onChangeOpt = Some(cmd))
    def onChangeComposer(cmd: JsCmd) = copy(onChangeComposerOpt = Some(cmd))
    def onChangeTextarea(cmd: JsCmd) = copy(onChangeTextareaOpt = Some(cmd))
    def onPaste(cmd: JsCmd) = copy(onPasteOpt = Some(cmd))
    def onPasteComposer(cmd: JsCmd) = copy(onPasteComposerOpt = Some(cmd))
    def onPasteTextarea(cmd: JsCmd) = copy(onPasteTextareaOpt = Some(cmd))
    def onNewwordComposer(cmd: JsCmd) = copy(onNewwordComposerOpt = Some(cmd))
    def onDestroyComposer(cmd: JsCmd) = copy(onDestroyComposerOpt = Some(cmd))
    def onChange_view(cmd: JsCmd) = copy(onChange_viewOpt = Some(cmd))
    def onShowDialog(cmd: JsCmd) = copy(onShowDialogOpt = Some(cmd))
    def onSaveDialog(cmd: JsCmd) = copy(onSaveDialogOpt = Some(cmd))
    def onCancelDialog(cmd: JsCmd) = copy(onCancelDialogOpt = Some(cmd))
    def onUndoComposer(cmd: JsCmd) = copy(onUndoComposerOpt = Some(cmd))
    def onRedoComposer(cmd: JsCmd) = copy(onRedoComposerOpt = Some(cmd))
    def onBeforecommandComposer(cmd: JsCmd) = copy(onBeforecommandComposerOpt = Some(cmd))
    def onAftercommandComposer(cmd: JsCmd) = copy(onAftercommandComposerOpt = Some(cmd))

    lazy val eventsJson = List(
      onLoadOpt.map(cmd => s""""load": function() { ${cmd.toJsCmd} }""")
      , onBeforeloadOpt.map(cmd => s""""beforeload": function() { ${cmd.toJsCmd} }""")
      , onFocusOpt.map(cmd => s""""focus": function() { ${cmd.toJsCmd} }""")
      , onFocusComposerOpt.map(cmd => s""""focus:composer": function() { ${cmd.toJsCmd} }""")
      , onFocusTextareaOpt.map(cmd => s""""focus:textarea": function() { ${cmd.toJsCmd} }""")
      , onBlurOpt.map(cmd => s""""blur": function() { ${cmd.toJsCmd} }""")
      , onBlurComposerOpt.map(cmd => s""""blur:composer": function() { ${cmd.toJsCmd} }""")
      , onBlurTextareaOpt.map(cmd => s""""blur:textarea": function() { ${cmd.toJsCmd} }""")
      , onChangeOpt.map(cmd => s""""change": function() { ${cmd.toJsCmd} }""")
      , onChangeComposerOpt.map(cmd => s""""change:composer": function() { ${cmd.toJsCmd} }""")
      , onChangeTextareaOpt.map(cmd => s""""change:textarea": function() { ${cmd.toJsCmd} }""")
      , onPasteOpt.map(cmd => s""""paste": function() { ${cmd.toJsCmd} }""")
      , onPasteComposerOpt.map(cmd => s""""paste:composer": function() { ${cmd.toJsCmd} }""")
      , onPasteTextareaOpt.map(cmd => s""""paste:textarea": function() { ${cmd.toJsCmd} }""")
      , onNewwordComposerOpt.map(cmd => s""""newword:composer": function() { ${cmd.toJsCmd} }""")
      , onDestroyComposerOpt.map(cmd => s""""destroy:composer": function() { ${cmd.toJsCmd} }""")
      , onChange_viewOpt.map(cmd => s""""change_view": function() { ${cmd.toJsCmd} }""")
      , onShowDialogOpt.map(cmd => s""""show:dialog": function() { ${cmd.toJsCmd} }""")
      , onSaveDialogOpt.map(cmd => s""""save:dialog": function() { ${cmd.toJsCmd} }""")
      , onCancelDialogOpt.map(cmd => s""""cancel:dialog": function() { ${cmd.toJsCmd} }""")
      , onUndoComposerOpt.map(cmd => s""""undo:composer": function() { ${cmd.toJsCmd} }""")
      , onRedoComposerOpt.map(cmd => s""""redo:composer": function() { ${cmd.toJsCmd} }""")
      , onBeforecommandComposerOpt.map(cmd => s""""beforecommand:composer": function() { ${cmd.toJsCmd} }""")
      , onAftercommandComposerOpt.map(cmd => s""""aftercommand:composer": function() { ${cmd.toJsCmd} }""")
    ).flatten.mkString(",\n")

    def rendered: NodeSeq =
      attrs.foldLeft(<textarea class="textarea" style={style} id={textareaId}>{get()}</textarea>)(_ % _) ++
      <tail>{
        Script(OnLoad(Run(
          s"""$$('#$textareaId').wysihtml5({
             |  "toolbar": {
             |    "font-styles": $fontStyles,
             |    "emphasis": $emphasis,
             |    "lists": $lists,
             |    "html": $html,
             |    "link": $link,
             |    "image": $image,
             |    "color": $color
             |  },
             |  "events": {
             |    $eventsJson
             |  }
             |});
             |""".stripMargin)))
        }</tail>

    def jsValue(): JsExp = JsRaw(s"""$$('#$textareaId').val()""")
  }

}
