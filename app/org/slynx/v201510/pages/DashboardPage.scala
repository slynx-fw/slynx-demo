package org.slynx.v201510.pages

import org.slynx.v201510.monitor.XMonitor
import org.slynx.v201510.pages.theme.XTH

import scala.xml.NodeSeq


trait XMonitorDashboardPage extends XTH {
  self: XMonitor =>

  class XDashboard()(implicit val xsh: XSPageHandle) extends StandardPage with MenuPage {
    override def contents(): NodeSeq = {
      TH.Row(
        TH.Col().md4 {
          TH.DashboardWidgetLarge(TH.IcnFA.icn_bar_chart, "#Sessions", self.sessions.size + "").styleRed.rendered
        },
        TH.Col().md4 {
          TH.DashboardWidgetLarge(TH.IcnFA.icn_bar_chart, "#Page Handles", self.sessions.map(_._2.handles.size).sum + "").styleRed.rendered
        },
        TH.Col().md4 {
          TH.DashboardWidgetLarge(TH.IcnFA.icn_bar_chart, "#Functions", self.sessions.values.map(_.nFunctions).sum + "").styleRed.rendered
        }
      )
    }

  }

}
