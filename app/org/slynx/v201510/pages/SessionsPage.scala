package org.slynx.v201510.pages


import util.ImplicitHelpers._
import com.omertron.imdbapi.ImdbApi
import com.omertron.imdbapi.model.ImdbList
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.util.Helpers._
import net.liftweb.util.{PassThru, Html5Parser}
import org.slynx.v201510.monitor.XMonitor
import org.slynx.v201510.pages.theme.XTH
import org.slynx.v201510.pages.theme._
import Libs.Lib
import Styles.Style
import play.twirl.api.TemplateMagic.javaCollectionToScala
import util.ImplicitHelpers.Pipe


import scala.xml.NodeSeq

trait XMonitorSessionsPage extends XTH {
  self: XMonitor =>

  class XSessionsPage()(implicit val xsh: XSPageHandle) extends MenuPage {

    override val pageTitle: String = "Slynx Sessions"

    override def libs: Set[Lib] = {
      import Libs._
      super.libs + jquery_data_tables + data_tables_bootstrap + jquery_floatThead + jquery_floatThead + jquery_ui + toastr
    }

    override def styles: Set[Style] = {
      import Styles._
      super.styles + data_tables_bootstrap + toastr
    }

    override def contents(): NodeSeq = {
      TH.Row(
        TH.Col().md12 {
          new TH.Widget {
            override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Danger
            override def solid: Boolean = true
            override def widgetTitle: String = "Current Slynx Sessions"
            override def widgetBody: NodeSeq = new TH.SimpleTable {

              override implicit def xsh: XSPageHandle = page.xsh
              override type R = XSession

              override lazy val allColumns: List[C] = List(
                ColStr("Session Id", r => r.id.toString),
                ColStr("#Page Handles", r => r.nPageHandles.toString),
                ColStr("#Functions", r => r.nFunctions.toString),
                ColStr("Created At", r => r.createdAt.toDateTime.toString("dd MMM yyyy, HH:mm:ss", java.util.Locale.forLanguageTag("en")))
              )

              override def allRows(): Seq[R] = sessionsLock.synchronized(sessions.values.toSeq)

              override def sortableRowsOnSort(row: R, idx: Int): JsCmd = JsCmds.Noop

              override def rowDetailsRender(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, Column)]): NodeSeq = NodeSeq.Empty
            }.renderedTable
          }.renderedWidget
        }
      )
    }
  }

}
