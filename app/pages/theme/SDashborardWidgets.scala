package pages.theme

import net.liftweb.http.js.JsCmds.SetHtml

import scala.xml.NodeSeq

trait SDashborardWidgets extends SBase {


  case class DashboardWidgetSmall(
                                   icn: TH.Icon,
                                   topText: String = "",
                                   mainText: String = "",
                                   progressBar: Option[(String, Double)] = None,
                                   fillColor: Boolean = false,
                                   styleClass: String = ""
                                   ) extends Id {

    val widgetId = id('widget)

    def rerender() = SetHtml(widgetId, rendered)

    def styleAqua = copy(styleClass = "bg-aqua")
    def styleGreen = copy(styleClass = "bg-green")
    def styleYellow = copy(styleClass = "bg-yellow")
    def styleRed = copy(styleClass = "bg-red")

    def rendered: NodeSeq = {
      <div id={widgetId} class={"info-box " + (if (fillColor) styleClass else "")}>
        <div class={"info-box-icon " + (if (!fillColor) styleClass else "")}><i class={icn.clas}></i></div>
         <div class="info-box-content">
           <span class="info-box-text">{topText}</span>
           <span class="info-box-number">{mainText}</span>
           { 
             progressBar.map({
               case (label, percent) =>
                 <div class="progress">
                   <div class="progress-bar" style= {"width:" + percent * 100 + "%;"}></div>
                 </div>
                 <span class="progress-description">{label}</span>
             }).getOrElse(NodeSeq.Empty)
           }
         </div>
      </div>
    }
  }

  case class DashboardWidgetLarge(
                                   icn: TH.Icon,
                                   bottomText: String = "",
                                   mainText: String = "",
                                   refSmallBox: Option[String] = None,
                                   styleClass: String = ""
                                   ) extends Id {
    val widgetId = id('widget)

    def rerender() = SetHtml(widgetId, rendered)

    def styleAqua = copy(styleClass = "bg-aqua")
    def styleGreen = copy(styleClass = "bg-green")
    def styleYellow = copy(styleClass = "bg-yellow")
    def styleRed = copy(styleClass = "bg-red")

    def rendered: NodeSeq = {
       <div id={widgetId} class={"small-box " + styleClass}>
          <div class="inner"><h3>{mainText}</h3><p>{bottomText}</p></div>
          <div class="icon"><i class={icn.clas}></i></div>
          <a href={refSmallBox.getOrElse("#")} class="small-box-footer">
            More info <i class="fa fa-arrow-circle-right"></i>
          </a>
        </div>
    }
  }

}