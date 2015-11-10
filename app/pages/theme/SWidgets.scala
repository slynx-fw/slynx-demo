package pages.theme

import net.liftweb.http.S
import net.liftweb.http.js.JsCmds.SetHtml

import scala.xml.NodeSeq

trait SWidgets extends SBase {

  object WidgetStyle extends Enumeration {
    val Default = Value
    val Primary = Value
    val Info = Value
    val Warning = Value
    val Success = Value
    val Danger = Value

    implicit class RichWidgetStyle(v: Value) {

      def toClass: String = v match {
        case Default => ""
        case Primary => " box-primary "
        case Info => " box-info "
        case Warning => " box-warning "
        case Success => " box-success "
        case Danger => " box-danger "
      }
    }

  }

  trait Widget extends Id {
    def style: WidgetStyle.Value = WidgetStyle.Default
    def closable: Boolean = false
    def collapsible: Boolean = false
    def solid: Boolean = false
    def widgetTextAlign: TextAlign.Align = TextAlign.Left

    val widgetId = id('widget)
    val headerId = id('contents)
    val bodyId = id('body)

    def widgetTitle: String
    def widgetActions: NodeSeq = NodeSeq.Empty

    def renderedHeader: NodeSeq = {
      <div class="box-header with-border">
        <h3 class="box-title">{widgetTitle}</h3>
        <div class="box-tools pull-right"> {
          widgetActions ++
            (if (collapsible) <button data-widget="collapse" class="btn btn-box-tool"><i class="fa fa-minus"></i></button> else NodeSeq.Empty) ++
            (if (closable) <button data-widget="remove" class="btn btn-box-tool"><i class="fa fa-times"></i></button> else NodeSeq.Empty)
        } </div>
      </div>
    }

    def widgetBody: NodeSeq

    def renderedBody: NodeSeq = {
      <div class="box-body" id={bodyId} style={s"text-align: ${widgetTextAlign}"}>
        {widgetBody}
      </div>
    }

    def rerenderWidget() = SetHtml(widgetId, renderedWidget)
    def rerenderHeader() = SetHtml(headerId, renderedHeader)
    def rerenderBody() = SetHtml(bodyId, widgetBody)

    def widgetClass = "box " + style.toClass + (if (solid) " box-solid" else "")
    def widgetStyle = ""

    def renderedWidget: NodeSeq = {
      <div id={widgetId} class={widgetClass} style={widgetStyle}>
        {renderedHeader}
        {renderedBody}
      </div>
    }
  }

  case class SimpleWidget(
                           widgetTitle: String,
                           override val style: WidgetStyle.Value = WidgetStyle.Default,
                           override val closable: Boolean = false,
                           override val collapsible: Boolean = false,
                           override val solid: Boolean = false,
                           override val widgetTextAlign: TextAlign.Align = TextAlign.Left,
                           override val widgetActions: NodeSeq = NodeSeq.Empty
                           )(
                           val widgetBody: NodeSeq
                           ) extends Widget

}
