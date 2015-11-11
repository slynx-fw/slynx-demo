package pages


import play.api.mvc.{AnyContent, Request}

import scala.xml.NodeSeq
import pages.theme.TH

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

  val tableSectionTables = MenuLink("Basic Table", "/tables", TH.IcnFA.icn_table)
  val tableSectionFullExample = MenuLink("Full Example", "/simpletables", TH.IcnFA.icn_table)
  val tableSectionPaginationtables = MenuLink("Paginated Table", "/paginationtables", TH.IcnFA.icn_table)
  val tableSectionSortabletables = MenuLink("Sortable Table", "/sortabletables", TH.IcnFA.icn_table)
  val tableSectionSearchabletables = MenuLink("Searchable Table", "/searchabletables", TH.IcnFA.icn_table)
  val tableSectionSelColsTable = MenuLink("Selectable Cols Table", "/selColsTable", TH.IcnFA.icn_table)
  val tableSectionRowDetailsOnClickTable = MenuLink("Row Details (on click) Table", "/rowDetailsOnClickTable", TH.IcnFA.icn_table)
  val tableSectionFixedHeaderTable = MenuLink("Fixed Header Table", "/fixedHeaderTable", TH.IcnFA.icn_table)
  val tableSectionLazyLoadingTable = MenuLink("Lazy Loading Table", "/lazyLoadingTable", TH.IcnFA.icn_table)
  val tableSectionSortableRowsTable = MenuLink("Sortable Rows Table", "/sortableRowsTable", TH.IcnFA.icn_table)

  val dashboard = MenuLink("Dashboard", "/", TH.IcnFA.icn_dashboard)
  val widgets = MenuLink("Widgets", "/widgets", TH.IcnFA.icn_th_large)
  val modals = MenuLink("Modals", "/modals", TH.IcnFA.icn_laptop)
  val knobs = MenuLink("Knobs", "/knobs", TH.IcnFA.icn_bar_chart_o)
  val toastr = MenuLink("Toastr Notification", "/toastr-notification", TH.IcnFA.icn_warning)
  val tables = MenuGroup("Tables", TH.IcnFA.icn_table)(
    tableSectionTables,
    tableSectionFullExample,
    tableSectionPaginationtables,
    tableSectionSortabletables,
    tableSectionSearchabletables,
    tableSectionSelColsTable,
    tableSectionRowDetailsOnClickTable,
    tableSectionFixedHeaderTable,
    tableSectionLazyLoadingTable,
    tableSectionSortableRowsTable
  )
  val flotcharts = MenuLink("Flot Charts", "/flot-charts", TH.IcnFA.icn_pie_chart)
  val forms = MenuLink("Forms", "/forms", TH.IcnFA.icn_list)
  val buttons = MenuLink("Buttons", "/buttons", TH.IcnFA.icn_hand_o_up)
  val emails = MenuLink("Emails", "/emails", TH.IcnFA.icn_envelope)
  val adminSection = MenuLink("Admin Section", "/xadmin/", TH.IcnFA.icn_user)
  val adminLTE = MenuGroup("AdminLTE Sample Pages", TH.IcnFA.icn_folder)(
    MenuGroup("Mailbox", TH.IcnFA.icn_folder)(
      MenuLink("Indox", "/adminlte/mailbox", TH.IcnFA.icn_circle_o),
      MenuLink("Compose", "/adminlte/mailboxCompose", TH.IcnFA.icn_circle_o),
      MenuLink("Read", "/adminlte/mailboxRead", TH.IcnFA.icn_circle_o)),
    MenuLink("Invoice", "/adminlte/invoice", TH.IcnFA.icn_circle_o),
    MenuLink("Blank Page", "/adminlte/blankPage", TH.IcnFA.icn_circle_o),
    MenuLink("User Profile", "/adminlte/userProfile", TH.IcnFA.icn_circle_o))

  val pages: List[MenuItem] = List[MenuItem](
    dashboard
    , widgets
    , modals
    , knobs
    , toastr
    , tables
    , flotcharts
    , forms
    , buttons
    , emails
    , adminSection
    , adminLTE
  )

  def renderedMenu(req: Request[AnyContent]) = pages.map(_.rendered(req, 0)).reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)
}