package pages

import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.util.Helpers._
import pages.theme
import pages.theme.Libs._
import pages.theme.Styles.Style
import pages.theme.{Libs, Styles}

import scala.xml.NodeSeq

class WidgetsPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile: String = "app/pages/WidgetsPage.scala"

  override def pageTitle: String = "Widget Page"
  override def pageSubtitle: String = ""

  override def libs: Set[Lib] = super.libs + sh_core + sh_brush
  override def styles: Set[Style] = super.styles + Styles.shCore + Styles.shThemeDefault

  override def render: (NodeSeq) => NodeSeq = super.render andThen {
    ".page-contents" #> {
      TH.Row("margin-bottom: 80px;")(
        TH.Col().md6 {
          // sampleWidget
          new TH.Widget {
            override def widgetTitle: String = "Sample Widget"
            override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
            override def solid: Boolean = true
            override def widgetBody: NodeSeq =
              TH.Btn()
                .Default
                .lbl("Sample Button")
                .ajax(Alert("Clicked button!"))
                .btn
          }.renderedWidget ++
            // sampleWidget
            codeWidget("sampleWidget")
        } ++
          TH.Col().md6 {
            // DangerWidget
            new TH.Widget {
              override def widgetTitle: String = "With Close & Collapse Buttons (Style: \"Danger\")"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def closable: Boolean = true
              override def collapsible: Boolean = true
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Danger
              override def widgetBody: NodeSeq =
                TH.Btn()
                  .Default
                  .lbl("Sample Button")
                  .ajax(Alert("Clicked button!"))
                  .btn
            }.renderedWidget ++
              // DangerWidget
              codeWidget("DangerWidget")
          }
      ) ++
        TH.Row("margin-bottom: 80px;")(
          TH.Col().md6 {
            // SolidWidget
            new TH.Widget {
              override def widgetTitle: String = "Solid Widget"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def solid: Boolean = true
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
              override def widgetBody: NodeSeq =
                TH.Btn()
                  .Default
                  .lbl("Sample Button")
                  .ajax(Alert("Clicked button!"))
                  .btn
            }.renderedWidget ++
              // SolidWidget
              codeWidget("SolidWidget")
          } ++ TH.Col().md6 {
            // CloseWidget
            new TH.Widget {
              override def widgetTitle: String = "With Close & Collapse Buttons (Style: \"Primary\")"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def closable: Boolean = true
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
              override def widgetBody: NodeSeq =
                TH.Btn()
                  .Default
                  .lbl("Sample Button")
                  .ajax(Alert("Clicked button!"))
                  .btn
            }.renderedWidget ++
              // CloseWidget
              codeWidget("CloseWidget")
          }
        ) ++
        TH.Row("margin-bottom: 80px;")(
          TH.Col().md6 {
            // SolidCloseWidget
            new TH.Widget {
              override def widgetTitle: String = "Solid Widget"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def solid: Boolean = true
              override def closable: Boolean = true
              override def collapsible: Boolean = true
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Success
              override def widgetBody: NodeSeq =
                TH.Btn()
                  .Default
                  .lbl("Sample Button")
                  .ajax(Alert("Clicked button!"))
                  .btn
            }.renderedWidget ++
              // SolidCloseWidget
              codeWidget("SolidCloseWidget")
          } ++ TH.Col().md6 {
            // SucessCloseWidget
            new TH.Widget {
              override def widgetTitle: String = "With Close & Collapse Buttons (Style: \"Sucess\")"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def collapsible: Boolean = true
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Success
              override def widgetBody: NodeSeq =
                TH.Btn()
                  .Default
                  .lbl("Sample Button")
                  .ajax(Alert("Clicked button!"))
                  .btn
            }.renderedWidget ++
              // SucessCloseWidget
              codeWidget("SucessCloseWidget")
          }
        )
    }
  }
}
