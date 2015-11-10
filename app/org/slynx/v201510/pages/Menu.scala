package org.slynx.v201510.pages

import org.slynx.v201510.monitor.XMonitor
import org.slynx.v201510.pages.theme.XTH
import play.api.mvc.{AnyContent, Request}

import scala.xml.NodeSeq

trait XMonitorMenu {
  self: XMonitor =>

  object Menu {

    trait MenuItem {
      def rendered(req: Request[AnyContent], lvl: Int): NodeSeq
      def allSubItems: List[MenuItem]
      def active(req: Request[AnyContent]): Boolean
    }

    case class MenuLink(name: String, link: String, icn: TH.Icon) extends MenuItem {

      def rendered(req: Request[AnyContent], lvl: Int): NodeSeq = <li class={if (active(req)) " active" else ""}>
        <a href={link}>
          <i class={icn.clas}></i> <span>
          {name}
        </span>
        </a>
      </li>

      lazy val path = link.split("/").drop(1).toList

      def allSubItems: List[MenuItem] = List(this)

      def active(req: Request[AnyContent]) = req.path == link
    }

    case class MenuGroup(name: String, icn: TH.Icon)(items: MenuItem*) extends MenuItem {

      def rendered(req: Request[AnyContent], lvl: Int): NodeSeq = {
        <li class={(if (lvl == 0) "treeview" else "") + (if (active(req)) " active" else "")}>
          <a href="javascript: void(0)">
            <i class={icn.clas}></i> <span>
            {name}
          </span> <i class="fa fa-angle-left pull-right"></i>
          </a>
          <ul class="treeview-menu">
            {items.map(_.rendered(req, lvl + 1)).reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)}
          </ul>
        </li>
      }

      override def allSubItems: List[MenuItem] = items.toList.flatMap(_.allSubItems)

      def active(req: Request[AnyContent]) = allSubItems.exists(_.active(req))
    }


    val dashboard = MenuLink("Dashboard", "/" + MonitorPath + "/", TH.IcnFA.icn_dashboard)
    val sessions = MenuLink("Sessions", "/" + MonitorPath + "/sessions", TH.IcnFA.icn_table)

    val pages: List[MenuItem] = List[MenuItem](
      dashboard,
      sessions
    )

    def renderedMenu(req: Request[AnyContent]) = pages.map(_.rendered(req, 0)).reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)
  }

}