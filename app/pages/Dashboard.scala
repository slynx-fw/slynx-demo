package pages

import java.net.URLEncoder


import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{Reload, Alert}
import net.liftweb.util.Helpers._
import org.squeryl.PrimitiveTypeMode._
import pages.{theme, MenuPage}
import pages.theme.Libs
import pages.theme.Libs.Lib
import pages.theme.Styles.Style

import scala.io.Source
import scala.xml.{Text, NodeSeq}

class Dashboard()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile: String = "app/pages/DashboardPage.scala"
  override def pageTitle: String = "Dashboard"
  override def pageSubtitle: String = ""

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {

      // InfoWidget1
      TH.Row(
        TH.Col().md3 {
          TH.DashboardWidgetSmall(TH.IcnFA.icn_envelope_o, "Messages", "1.486").styleAqua.rendered
        } ++ TH.Col().md3 {
          TH.DashboardWidgetSmall(TH.IcnFA.icn_flag_o, "Bookmarks", "410").styleGreen.rendered
        } ++ TH.Col().md3 {
          TH.DashboardWidgetSmall(TH.IcnFA.icn_file_o, "Uploads", "13.684").styleYellow.rendered
        } ++ TH.Col().md3 {
          TH.DashboardWidgetSmall(TH.IcnFA.icn_star_o, "Likes", "9.954").styleRed.rendered
        }) ++
        TH.Row(
          TH.Col().md3 {
            TH.DashboardWidgetSmall(TH.IcnFA.icn_bookmark_o, "Bookmarks", "41.410", Some("70% Increase in 30 Days", 0.7), fillColor = true).styleAqua.rendered
          } ++ TH.Col().md3 {
            TH.DashboardWidgetSmall(TH.IcnFA.icn_thumbs_o_up, "Likes", "41.410", Some("70% Increase in 30 Days", 0.7), fillColor = true).styleGreen.rendered
          } ++ TH.Col().md3 {
            TH.DashboardWidgetSmall(TH.IcnFA.icn_calendar, "Events", "41.410", Some("70% Increase in 30 Days", 0.7), fillColor = true).styleYellow.rendered
          } ++ TH.Col().md3 {
            TH.DashboardWidgetSmall(TH.IcnFA.icn_comment_o, "Comments", "41.410", Some("70% Increase in 30 Days", 0.7), fillColor = true).styleRed.rendered
          }) ++
        TH.Row(
          TH.Col().md3 {
            TH.DashboardWidgetLarge(TH.IcnFA.icn_shopping_cart, "New Orders", "150").styleAqua.rendered
          } ++ TH.Col().md3 {
            TH.DashboardWidgetLarge(TH.IcnFA.icn_bar_chart, "Bounce Rate", "53%").styleGreen.rendered
          } ++ TH.Col().md3 {
            TH.DashboardWidgetLarge(TH.IcnFA.icn_user, "User Registations", "150").styleYellow.rendered
          } ++ TH.Col().md3 {
            TH.DashboardWidgetLarge(TH.IcnFA.icn_pie_chart, "Unique Visitors", "150").styleRed.rendered
          }) ++
        // InfoWidget1
        TH.Row(
          TH.Col().md12 {
            codeWidget("InfoWidget1")
          }
        )
    }
    )
}
