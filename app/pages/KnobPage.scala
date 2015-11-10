package pages

import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JE.AnonFunc
import net.liftweb.http.js.JsCmds.Alert
import net.liftweb.util.Helpers._
import pages.theme.Libs.Lib
import pages.theme.Styles.Style
import pages.theme.{Libs, Styles}

import scala.xml.NodeSeq

class KnobPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile = "app/pages/KnobPage.scala"
  override def pageTitle: String = "Knob Page"
  override def pageSubtitle: String = ""

  override def libs: Set[Lib] = {
    import Libs._
    super.libs + jquery_knob + toastr
  }

  override def styles: Set[Style] = {
    import Styles._
    super.styles + shCore + shThemeDefault + toastr
  }

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {
      TH.Row("margin-bottom: 30px;")(
        TH.Col().md12 {
          new TH.Widget {
            override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Default
            override def widgetTitle: String = " jQuery Knob"
            override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
            override def widgetBody: NodeSeq = {
              // jQueryKnob
              TH.Row(
                TH.Col().md3 {
                  TH.Knob().lbl("New Visitors")
                    .initialValue(30)
                    .inputColor("rgb(60, 141, 188)").bgColor("#eeeeee").fgColor("#3c8dbc")
                    .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                },
                TH.Col().md3 {
                  TH.Knob().lbl("Bounce Rate")
                    .initialValue(70)
                    .inputColor("rgb(245, 105, 84)").bgColor("#eeeeee").fgColor("#f56954")
                    .onReleaseAjax(value => {TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()}).rendered
                },
                TH.Col().md3 {
                  TH.Knob().lbl("Server Load")
                    .min(-150).max(150).initialValue(-80)
                    .inputColor("rgb(0, 166, 90)").bgColor("#eeeeee").fgColor("#00a65a")
                    .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                },
                TH.Col().md3 {
                  TH.Knob().lbl("Disk Space")
                    .initialValue(40)
                    .inputColor("rgb(0, 192, 239)").bgColor("#eeeeee").fgColor("#00c0ef")
                    .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                }
              ) ++
                TH.Row(
                  TH.Col().md6 {
                    TH.Knob().lbl("Bandwidth")
                      .initialValue(90)
                      .inputColor("rgb(147, 42, 182)").bgColor("#eeeeee").fgColor("#932ab6")
                      .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                  },
                  TH.Col().md6 {
                    TH.Knob().lbl("CPU")
                      .step(2).initialValue(50)
                      .fgColor("#39CCCC").inputColor("rgb(57, 204, 204)").bgColor("#eeeeee")
                      .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                  }
                )
            }
          }.renderedWidget
        } ++
          TH.Col().md12 {
            // jQueryKnob
            codeWidget("jQueryKnob")
          }) ++
        TH.Row("margin-bottom: 30px;")(
          TH.Col().md12 {
            new TH.Widget {
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Default
              override def widgetTitle: String = " jQuery Knob Different Sizes"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def widgetBody: NodeSeq = {
                // jQueryKnobDifferent
                TH.Row(
                  TH.Col().md3 {
                    TH.Knob().lbl("width(90)")
                      .initialValue(30)
                      .readOnly(true)
                      .inputColor("rgb(60, 141, 188)").bgColor("#eeeeee").fgColor("#3c8dbc")
                      .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                  },
                  TH.Col().md3 {
                    TH.Knob().lbl("width(120)")
                      .initialValue(30).width(120).height(120)
                      .inputColor("rgb(245, 105, 84)").bgColor("#eeeeee").fgColor("#f56954")
                      .onReleaseAjax(value => {TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()}).rendered
                  },
                  TH.Col().md3 {
                    TH.Knob().lbl("thickness(0.1)")
                      .max(30).thickness(0.1).initialValue(30)
                      .inputColor("rgb(0, 166, 90)").bgColor("#eeeeee").fgColor("#00a65a")
                      .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                  },
                  TH.Col().md3 {
                    TH.Knob().lbl("angleArc(250)")
                      .initialValue(30).thickness(0.2)
                      .width(120).height(120)
                      .angleArc(250).angleOffset(-120)
                      .inputColor("rgb(0, 192, 239)").bgColor("#eeeeee").fgColor("#00c0ef")
                      .onReleaseAjax(value => TH.Toastr.Info.withTitle(s"Changed to $value").withMesg("").withDuration(800).show()).rendered
                  }
                )
              }
            }.renderedWidget
          } ++
            TH.Col().md12 {
              // jQueryKnobDifferent
              codeWidget("jQueryKnobDifferent")
            }) ++ <div style="clearfix"></div>
    }
    )
}