package pages

import net.liftweb.http.js.JsCmds
import net.liftweb.http.js.JsCmds.Run
import net.liftweb.util.Helpers._
import controllers.RootController.XSPageHandle
import scala.xml.NodeSeq

class ButtonPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile: String = "app/pages/ButtonPage.scala"

  override def pageTitle: String = "Buttons"
  override def pageSubtitle: String = "Buttons Example"

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {
      TH.Row(
        TH.Col().md12 {
          new TH.Widget {

            override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
            override def widgetTitle: String = "Button Styles"
            override def widgetBody: NodeSeq =
              <p>Various styles of standard buttons.</p>
              <table class="table table-bordered text-center">
                <tbody><tr>
                  <th>Normal</th>
                  <th>Large</th>
                  <th>Small</th>
                  <th>X-Small</th>
                  <th>Flat</th>
                  <th>Disabled</th>
                </tr>
                <tr>
                  <td>{TH.Btn().Default.block.lbl("Default").onclick(TH.Toastr.Info.withMesg("Normal Button").show()).btn}</td>
                  <td>{TH.Btn().Default.block.lbl("Default").onclick(TH.Toastr.Info.withMesg("Normal Button").show()).lg.btn}</td>
                  <td>{TH.Btn().Default.block.lbl("Default").onclick(TH.Toastr.Info.withMesg("Normal Button").show()).sm.btn}</td>
                  <td>{TH.Btn().Default.block.lbl("Default").onclick(TH.Toastr.Info.withMesg("Normal Button").show()).xs.btn}</td>
                  <td>{TH.Btn().Default.block.lbl("Default").onclick(TH.Toastr.Info.withMesg("Normal Button").show()).flat.btn}</td>
                  <td>{TH.Btn().Default.block.lbl("Default").onclick(TH.Toastr.Info.withMesg("Normal Button").show()).disabled.btn}</td>
                </tr>
                <tr>
                  <td>{TH.Btn().Primary.block.lbl("Primary").onclick(TH.Toastr.Info.withMesg("Primary Button").show()).btn}</td>
                  <td>{TH.Btn().Primary.block.lbl("Primary").onclick(TH.Toastr.Info.withMesg("Primary Button").show()).lg.btn}</td>
                  <td>{TH.Btn().Primary.block.lbl("Primary").onclick(TH.Toastr.Info.withMesg("Primary Button").show()).sm.btn}</td>
                  <td>{TH.Btn().Primary.block.lbl("Primary").onclick(TH.Toastr.Info.withMesg("Primary Button").show()).xs.btn}</td>
                  <td>{TH.Btn().Primary.block.lbl("Primary").onclick(TH.Toastr.Info.withMesg("Primary Button").show()).flat.btn}</td>
                  <td>{TH.Btn().Primary.block.lbl("Primary").onclick(TH.Toastr.Info.withMesg("Primary Button").show()).disabled.btn}</td>
                </tr>
                <!-- btnStyles -->
                <!-- Example for success buttons: -->
                <tr>
                  <!-- Normal: -->
                  <td>{TH.Btn().Success.block.lbl("Success").onclick(TH.Toastr.Info.withMesg("Success Button").show()).btn}</td>
                  <!-- Large: -->
                  <td>{TH.Btn().Success.block.lbl("Success").onclick(TH.Toastr.Info.withMesg("Success Button").show()).lg.btn}</td>
                  <!-- Small: -->
                  <td>{TH.Btn().Success.block.lbl("Success").onclick(TH.Toastr.Info.withMesg("Success Button").show()).sm.btn}</td>
                  <!-- Extra-small: -->
                  <td>{TH.Btn().Success.block.lbl("Success").onclick(TH.Toastr.Info.withMesg("Success Button").show()).xs.btn}</td>
                  <!-- Flat: -->
                  <td>{TH.Btn().Success.block.lbl("Success").onclick(TH.Toastr.Info.withMesg("Success Button").show()).flat.btn}</td>
                  <!-- Disabled: -->
                  <td>{TH.Btn().Success.block.lbl("Success").onclick(TH.Toastr.Info.withMesg("Success Button").show()).disabled.btn}</td>
                </tr>
                <!-- btnStyles -->
                <tr>
                  <td>{TH.Btn().Info.block.lbl("Info").onclick(TH.Toastr.Info.withMesg("Info Button").show()).btn}</td>
                  <td>{TH.Btn().Info.block.lbl("Info").onclick(TH.Toastr.Info.withMesg("Info Button").show()).lg.btn}</td>
                  <td>{TH.Btn().Info.block.lbl("Info").onclick(TH.Toastr.Info.withMesg("Info Button").show()).sm.btn}</td>
                  <td>{TH.Btn().Info.block.lbl("Info").onclick(TH.Toastr.Info.withMesg("Info Button").show()).xs.btn}</td>
                  <td>{TH.Btn().Info.block.lbl("Info").onclick(TH.Toastr.Info.withMesg("Info Button").show()).flat.btn}</td>
                  <td>{TH.Btn().Info.block.lbl("Info").onclick(TH.Toastr.Info.withMesg("Info Button").show()).disabled.btn}</td>
                </tr>
                <tr>
                  <td>{TH.Btn().Danger.block.lbl("Danger").onclick(TH.Toastr.Info.withMesg("Danger Button").show()).btn}</td>
                  <td>{TH.Btn().Danger.block.lbl("Danger").onclick(TH.Toastr.Info.withMesg("Danger Button").show()).lg.btn}</td>
                  <td>{TH.Btn().Danger.block.lbl("Danger").onclick(TH.Toastr.Info.withMesg("Danger Button").show()).sm.btn}</td>
                  <td>{TH.Btn().Danger.block.lbl("Danger").onclick(TH.Toastr.Info.withMesg("Danger Button").show()).xs.btn}</td>
                  <td>{TH.Btn().Danger.block.lbl("Danger").onclick(TH.Toastr.Info.withMesg("Danger Button").show()).flat.btn}</td>
                  <td>{TH.Btn().Danger.block.lbl("Danger").onclick(TH.Toastr.Info.withMesg("Danger Button").show()).disabled.btn}</td>
                </tr>
                <tr>
                  <td>{TH.Btn().Warning.block.lbl("Warning").onclick(TH.Toastr.Info.withMesg("Warning Button").show()).btn}</td>
                  <td>{TH.Btn().Warning.block.lbl("Warning").onclick(TH.Toastr.Info.withMesg("Warning Button").show()).lg.btn}</td>
                  <td>{TH.Btn().Warning.block.lbl("Warning").onclick(TH.Toastr.Info.withMesg("Warning Button").show()).sm.btn}</td>
                  <td>{TH.Btn().Warning.block.lbl("Warning").onclick(TH.Toastr.Info.withMesg("Warning Button").show()).xs.btn}</td>
                  <td>{TH.Btn().Warning.block.lbl("Warning").onclick(TH.Toastr.Info.withMesg("Warning Button").show()).flat.btn}</td>
                  <td>{TH.Btn().Warning.block.lbl("Warning").onclick(TH.Toastr.Info.withMesg("Warning Button").show()).disabled.btn}</td>
                </tr>
              </tbody>
            </table>
          }.renderedWidget ++
            codeWidget("btnStyles")
        }
      ) ++
        TH.Row(
          TH.Col().md12 {
            // appStyles
            new TH.Widget {
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
              override def widgetTitle: String = "Application Styles"
              override def widgetTextAlign: TH.TextAlign.Align = TH.TextAlign.Center
              override def widgetBody: NodeSeq =
                TH.Btn().App.lbl("Edit").icn(TH.IcnFA.icn_edit).link ++
                  TH.Btn().App.lbl("Play").icn(TH.IcnFA.icn_play).link ++
                  TH.Btn().App.lbl("Repeat").icn(TH.IcnFA.icn_repeat).link ++
                  TH.Btn().App.lbl("Pause").icn(TH.IcnFA.icn_pause).link ++
                  TH.Btn().App.lbl("Save").icn(TH.IcnFA.icn_save).link ++
                  TH.Btn().App.lbl("Notifications").icn(TH.IcnFA.icn_bullhorn).link ++
                  TH.Btn().App.lbl("Products").icn(TH.IcnFA.icn_barcode).link ++
                  TH.Btn().App.lbl("Users").icn(TH.IcnFA.icn_user).link ++
                  TH.Btn().App.lbl("Orders").icn(TH.IcnFA.icn_inbox).link ++
                  TH.Btn().App.lbl("Inbox").icn(TH.IcnFA.icn_envelope).link ++
                  TH.Btn().App.lbl("Likes").icn(TH.IcnFA.icn_heart_o).link
            }.renderedWidget ++
              // appStyles
              codeWidget("appStyles")
          }
        ) ++
        TH.Row(
          TH.Col().md6 {
            // blockButtons
            new TH.Widget {
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
              override def widgetTitle: String = "Block Buttons"
              override def widgetBody: NodeSeq =
                TH.Btn().Default.block.lbl("TH.Btn().Default.block").onclick(TH.Toastr.Info.withMesg("Default.block Button").show()).btn ++
                  TH.Btn().Default.block.lbl("TH.Btn().Default.block.flat").flat.onclick(TH.Toastr.Info.withMesg("Default.block.flat Button").show()).btn ++
                  TH.Btn().Default.block.lbl("TH.Btn().Default.block.sm").sm.onclick(TH.Toastr.Info.withMesg("Default.block.sm Button").show()).btn
            }.renderedWidget ++
              // blockButtons
              codeWidget("blockButtons")
          },
          TH.Col().md6 {
            // differentColors
            new TH.Widget {
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
              override def widgetTitle: String = "Different colors"
              override def widgetBody: NodeSeq =
                <p>
                  {
                  TH.Btn().Maroon.margin.lbl("TH.Btn().Maroon.flat").flat.onclick(TH.Toastr.Info.withMesg("Maroon.flat Button").show()).btn ++
                    TH.Btn().Purple.margin.lbl("TH.Btn().Purple.flat").flat.onclick(TH.Toastr.Info.withMesg("Purple.flat Button").show()).btn ++
                    TH.Btn().Navy.margin.lbl("TH.Btn().Navy.flat").flat.onclick(TH.Toastr.Info.withMesg("Navy.flat Button").show()).btn ++
                    TH.Btn().Orange.margin.lbl("TH.Btn().Orange.flat").flat.onclick(TH.Toastr.Info.withMesg("Orange.flat Button").show()).btn ++
                    TH.Btn().Olive.margin.lbl("TH.Btn().Olive.flat").flat.onclick(TH.Toastr.Info.withMesg("Olive.flat Button").show()).btn
                  }</p><p>
                  {
                  TH.Btn().Maroon.margin.lbl("TH.Btn().Maroon").onclick(TH.Toastr.Info.withMesg("Maroon Button").show()).btn ++
                    TH.Btn().Purple.margin.lbl("TH.Btn().Purple").onclick(TH.Toastr.Info.withMesg("Purple Button").show()).btn ++
                    TH.Btn().Navy.margin.lbl("TH.Btn().Navy").onclick(TH.Toastr.Info.withMesg("Navy Button").show()).btn ++
                    TH.Btn().Orange.margin.lbl("TH.Btn().Orange").onclick(TH.Toastr.Info.withMesg("Orange Button").show()).btn ++
                    TH.Btn().Olive.margin.lbl("TH.Btn().Olive").onclick(TH.Toastr.Info.withMesg("Olive Button").show()).btn
                  }
                </p>
            }.renderedWidget ++
              // differentColors
              codeWidget("differentColors")

          }
        )
    })
}