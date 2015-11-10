package pages

import util.ImplicitHelpers._
import controllers.RootController.XSPageHandle
import net.liftweb.util.Helpers._
import pages.theme.Libs.Lib
import pages.theme.Styles.Style
import pages.theme.{Libs, Styles}

import scala.xml.NodeSeq

class ToastrNotificationsPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile = "app/pages/ToastrNotificationsPage.scala"

  override def libs: Set[Lib] = {
    import Libs._
    super.libs + sh_core + sh_brush + toastr
  }

  override def styles: Set[Style] = {
    import Styles._
    super.styles + shCore + shThemeDefault + toastr
  }
  override def pageTitle: String = "Notifications"
  override def pageSubtitle: String = "Sample Notifications"

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {
      TH.Row("margin-bottom: 30px;")(
        TH.Col().md6 {
          new TH.Widget {
            override def widgetTitle: String = "Notifications: Simple Notification"
            override def widgetBody: NodeSeq = {
              TH.Row(
                TH.Col().md12 {
                  // SampleNotifications
                  TH.Btn().centered.Primary.withClass("create-user").lbl("Show notification")
                    .onclick(
                      TH.Toastr.Info
                        .withTitle("Sample Notification")
                        .withMesg("This is a sample notification")
                        .show()
                    ).btn
                })
            }
          }.renderedWidget ++
            // SampleNotifications
            codeWidget("SampleNotifications")
        } ++
          TH.Col().md6 {
            new TH.Widget {
              override def widgetTitle: String = "Notifications: Success Notification"
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Success
              override def widgetBody: NodeSeq = {
                TH.Row(
                  TH.Col().md12 {
                    // SuccessNotifications
                    TH.Btn().centered.Success.withClass("create-user").lbl("Show notification")
                      .onclick(
                        TH.Toastr.Success
                          .withDuration(400)
                          .withMesg("Success notification, with a show duration of 400ms")
                          .show()
                      ).btn
                  })
              }
            }.renderedWidget ++
              // SuccessNotifications
              codeWidget("SuccessNotifications")
          }) ++
        TH.Row("margin-bottom: 30px;")(
          TH.Col().md6 {
            new TH.Widget {
              override def widgetTitle: String = "Notifications: Close Button and Progress Bar"
              override def widgetBody: NodeSeq = {
                TH.Row(
                  TH.Col().md12 {
                    // CloseNotifications
                    TH.Btn().centered.Primary.withClass("create-user").lbl("Show notification")
                      .onclick(
                        TH.Toastr.Info
                          .closeBtn(true)
                          .progressBar(true)
                          .withMesg("This is a notification with a close button and a progress bar")
                          .show()
                      ).btn
                  })
              }
            }.renderedWidget ++
              // CloseNotifications
              codeWidget("CloseNotifications")
          } ++
            TH.Col().md6 {
              new TH.Widget {
                override def widgetTitle: String = "Notifications: Preventing Duplicates"
                override def widgetBody: NodeSeq = {
                  TH.Row(
                    TH.Col().md12 {
                      // PreventingNotifications
                      TH.Btn().centered.Primary.withClass("create-user").lbl("Show notification")
                        .onclick(
                          TH.Toastr.Info
                            .preventDuplicates(true)
                            .withMesg("This notification will prevent duplicates")
                            .show()
                        ).btn
                    })
                }
              }.renderedWidget ++
                // PreventingNotifications
                codeWidget("PreventingNotifications")
            }) ++
        TH.Row("margin-bottom: 30px;")(
          TH.Col().md6 {
            new TH.Widget {
              override def widgetTitle: String = "Notifications: Top-Center Error Notification"
              override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Danger
              override def widgetBody: NodeSeq = {
                TH.Row(
                  TH.Col().md12 {
                    // ErrorNotifications
                    TH.Btn().centered.Danger.withClass("create-user").lbl("Show notification")
                      .onclick(
                        TH.Toastr.Error
                          .newestOnTop(true)
                          .withMesg("This is an error notification on the top-center of the page")
                          .show()
                      ).btn
                  })
              }
            }.renderedWidget++
            // ErrorNotifications
            codeWidget("ErrorNotifications")
          } ++
            TH.Col().md6 {
              new TH.Widget {
                override def widgetTitle: String = "Notifications: Timings"
                override def widgetBody: NodeSeq = {
                  TH.Row(
                    TH.Col().md12 {
                      // TimingsNotifications
                      TH.Btn().centered.Primary.withClass("create-user").lbl("Show notification")
                        .onclick(
                          TH.Toastr.Info
                            .withPosTopCenter
                            .withTimeout(5000)
                            .withDuration(400)
                            .withMesg("This notification will appear on the top-center of the page, with a timeout of 5000ms and 400ms of duration")
                            .show()
                        ).btn
                    })
                }
              }.renderedWidget++
                // TimingsNotifications
                codeWidget("TimingsNotifications")
            })
    }
    )
}