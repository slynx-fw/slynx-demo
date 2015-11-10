package pages.theme

import java.util.UUID

import util.ImplicitHelpers._
import controllers.RootController
import controllers.RootController._
import net.liftweb.builtin.snippet.Tail
import net.liftweb.common.Box
import net.liftweb.http.SHtml
import net.liftweb.http.SHtml.ElemAttr
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import play.api.Play
import play.api.mvc.Result
import util.ImplicitHelpers.Pipe
import util.{Props, JSON, RawJSON}

import scala.concurrent.Future
import scala.xml.{Xhtml, NodeSeq}


trait SModularTables {

  trait Table extends Id {

    val stopPropagation = Run( """; var e = e ? e : window.event; if (typeof e.stopPropagation != "undefined") { e.stopPropagation(); } else if (typeof e.cancelBubble != "undefined") { e.cancelBubble = true; } ;""")

    def parentXsh: RootController.XSPageHandle

    /** Row Type */
    type R
    /** Col Type */
    type C

    // ================================= COLUMNS: =================================
    def thClasses(col: C, colId: String, colIdx: Int): List[String] = Nil
    def thStyle(col: C, colId: String, colIdx: Int): List[String] = Nil

    def tdClasses(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): List[String] = Nil
    def tdStyle(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): List[String] = Nil

    def colTransformTh(col: C, colId: String, colIdx: Int)(implicit xsh: XSPageHandle): NodeSeq => NodeSeq =
      "th [class+]" #> thClasses(col, colId, colIdx).mkString(" ") &
        "th [col-table]" #> id('table) &
        "th [style+]" #> thStyle(col, colId, colIdx).mkString(";", ";", ";")

    def colTransformFootTh(col: C, colId: String, colIdx: Int)(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = colTransformTh(col, colId, colIdx)

    def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq =
      "td [class+]" #> tdClasses(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols).mkString(" ") &
        "td [style+]" #> tdStyle(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols).mkString(";", ";", ";") &
        "td [id]" #> colId

    def colIdFor(col: C): String

    // ================================= END OF COLUMNS =================================


    def columns(): Seq[C]
    def rows(): Seq[R]

    def table = this

    val handleId = id('handle)
    val wrapperId = id('wrapper)
    val tableId = id('table)
    val theadId = id('thead)
    val tbodyId = id('tbody)
    val tfootId = id('tfoot)

    def tableClasses: List[String] = Nil

    val template: NodeSeq

    def keepClasses: List[String] = Nil

    def transformTr(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      "tr [id]" #> rowId andThen
        "td" #> _cols.zipWithIndex.map({ case ((colId, col), colIdx) => colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) })
    }

    def transformHeadTr(_cols: Seq[(String, C)])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      "th" #> _cols.zipWithIndex.map({ case ((colId, col), colIdx) => colTransformTh(col, colId, colIdx) })
    }

    def transformFootTr(_cols: Seq[(String, C)])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      "th" #> _cols.zipWithIndex.map({ case ((colId, col), colIdx) => colTransformFootTh(col, colId, colIdx) })
    }

    def transformPage(_rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      ((ns: NodeSeq) =>
        (".mdl-clearable" #> ClearNodes).apply(
          (keepClasses.map(clas => s".$clas [class!]" #> "mdl-clearable").reduceOption(_ & _).getOrElse(PassThru)).apply(ns)
        )) andThen
        ".mdl-wrapper [id]" #> wrapperId &
          "table [id]" #> tableId &
          "thead [id]" #> theadId &
          "tbody [id]" #> tbodyId &
          "tfoot [id]" #> tfootId andThen
        "thead tr" #> transformHeadTr(_cols) andThen
        "tbody tr" #> _rows.zipWithIndex.map({ case ((rowId, row), rowIdx) => transformTr(row, rowId, rowIdx, _rows, _cols) }) andThen
        "tfoot tr" #> transformFootTr(_cols)
    }

    def transformPage(rows: Seq[R], cols: Seq[C], _arg3: Unit = Unit)(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      val _rows = rows.zipAll(Nil, null.asInstanceOf[R], Helpers.nextFuncName).zipWithIndex.map(e => ((e._1._2 + e._2), e._1._1))
      val _cols = cols.zipAll(Nil, null.asInstanceOf[C], Helpers.nextFuncName).zipWithIndex.map(e => ((e._1._2 + e._2), e._1._1))
      transformPage(_rows, _cols)
    }

    def transformPage(rows: Seq[R])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = transformPage(rows, columns())

    def transformPage()(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = transformPage(rows())

    def renderTable()(implicit xsh: XSPageHandle): NodeSeq = transformPage().apply(template)

    def renderedTable()(implicit xsh: XSPageHandle = parentXsh.createSubHandle(handleId)): NodeSeq = renderTable()

    def onRerenderTable(): JsCmd = Noop

    def rerenderTable(): JsCmd = onRerenderTable() & Replace(wrapperId, renderedTable())

    lazy val props: Props = Props.InMemory
  }

  trait SortableTable extends Table {

    private val p = props / "sortable"

    def sortableDefaultSortCol(): Option[C]
    def sortableDefaultSortAsc(col: C) = true
    def sortableIsSortable(col: C) = true

    val sortableSortColId = p.CusOpt[String]("sortColId", sortableDefaultSortCol().map(colIdFor(_)), id => Some(id).filter(_ => columns().exists(c => colIdFor(c) == id)), c => c)
    def sortableSortCol: Option[C] = sortableSortColId().flatMap(id => columns().find(c => colIdFor(c) == id))
    val sortableSortAsc = p.Bool("sortAsc", sortableSortCol.map(sortableDefaultSortAsc).getOrElse(true))

    def sortableSortAscClass: String
    def sortableSortDescClass: String
    def sortableSortNeutralClass: String

    override def thClasses(col: C, colId: String, colIdx: Int): List[String] = super.thClasses(col, colId, colIdx) ::: {
      if (sortableSortColId() == Some(colIdFor(col))) List(if (sortableSortAsc()) sortableSortAscClass else sortableSortDescClass) else if (sortableIsSortable(col)) List(sortableSortNeutralClass) else Nil
    }

    override def colTransformTh(col: C, colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = super.colTransformTh(col, colId, colIdx) andThen {
      if (sortableIsSortable(col)) {
        "th [onclick]" #> xsh.ajaxInvoke(() => {
          if (sortableSortColId() == Some(colIdFor(col))) {
            sortableSortAsc() = !sortableSortAsc()
          }
          else {
            sortableSortAsc() = sortableDefaultSortAsc(col)
            sortableSortColId() = Some(colIdFor(col))
          }
          rerenderTable()
        })
      } else {
        PassThru
      }
    }
  }

  trait PagTable extends Table {

    def pagBtnsCurrentClass: String
    def pagBtnsDisabledClass: String
    def pagBtnsShowFirstAndLast: Boolean = true
    def pagBtnsShowPrevAndNext: Boolean = true
    def pagHideBtnsForOnePage: Boolean = false
    def pagNBtns = 5
    def pagPageSizes = 3 :: 10 :: 20 :: 40 :: 60 :: 100 :: Nil
    def pagInitPageSize = 3

    override def keepClasses: List[String] = "mdl-pag-keep" :: super.keepClasses

    var pagCurPage = 0
    var pagCurPageSize = pagInitPageSize
    def pagCurOffset = pagCurPage * pagCurPageSize

    def nPages(rowsSize: Long) = math.max(1, math.ceil(rowsSize / pagCurPageSize.toDouble).toInt)

    def firstPage()(implicit xsh: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = 0; rerenderTable()}) & Run("return false;")
    def prevPage()(implicit xsh: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = math.max(0, pagCurPage - 1); rerenderTable()}) & Run("return false;")
    def toPage(n: Int)(implicit xsh: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = n; rerenderTable()}) & Run("return false;")
    def nextPage(rowsSize: Long)(implicit xsh: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = math.min(nPages(rowsSize) - 1, pagCurPage + 1); rerenderTable()}) & Run("return false;")
    def lastPage(rowsSize: Long)(implicit xsh: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = nPages(rowsSize) - 1; rerenderTable()}) & Run("return false;")

    def currentButtons(rowsSize: Long) = {
      val side = pagNBtns - 1
      val all = ((pagCurPage - side) until pagCurPage) ++ ((pagCurPage + 1) to (pagCurPage + side))
      all.filter(_ >= 0).filter(_ < nPages(rowsSize)).sortBy(n => math.abs(pagCurPage - n)).take(side).:+(pagCurPage).sorted
    }

    def transformPag(rowsSize: Long)(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      if (nPages(rowsSize) == 1 && pagHideBtnsForOnePage) ClearNodes
      else {
        (if (!pagBtnsShowFirstAndLast) ".mdl-pag-first" #> ClearNodes & ".mdl-pag-last" #> ClearNodes else PassThru) andThen
          (if (!pagBtnsShowPrevAndNext) ".mdl-pag-prev" #> ClearNodes & ".mdl-pag-next" #> ClearNodes else PassThru) andThen
          ".mdl-pag-first [class+]" #> (if (pagCurPage == 0) pagBtnsDisabledClass else "") &
            ".mdl-pag-first" #> {".mdl-pag-btn [onclick]" #> firstPage()} &
            ".mdl-pag-prev [class+]" #> (if (pagCurPage == 0) pagBtnsDisabledClass else "") &
            ".mdl-pag-prev" #> {".mdl-pag-btn [onclick]" #> prevPage()} &
            ".mdl-pag-next [class+]" #> (if (pagCurPage == nPages(rowsSize) - 1) pagBtnsDisabledClass else "") &
            ".mdl-pag-next" #> {".mdl-pag-btn [onclick]" #> nextPage(rowsSize)} &
            ".mdl-pag-last [class+]" #> (if (pagCurPage == nPages(rowsSize) - 1) pagBtnsDisabledClass else "") &
            ".mdl-pag-last" #> {".mdl-pag-btn [onclick]" #> lastPage(rowsSize)} andThen
          ".mdl-pag-n" #> currentButtons(rowsSize).map(n => {
            ".mdl-pag-n [class+]" #> (if (pagCurPage == n) pagBtnsCurrentClass else "") &
              ".mdl-pag-btn *" #> (n + 1).toString &
              ".mdl-pag-btn [onclick]" #> toPage(n) &
              ".mdl-pag-btn [href]" #> "javascript: void(0)"
          })
      } andThen
        ".mdl-pag-info" #> pagInfo(math.min(rowsSize, (pagCurPage * pagCurPageSize + 1)), math.min(rowsSize, ((pagCurPage + 1) * pagCurPageSize)), rowsSize) andThen
        ".mdl-pag-sizes" #> xsh.ajaxSelectElem[Int](pagPageSizes, Box(pagPageSizes.find(_ == pagCurPageSize)))(s => {pagCurPageSize = s; rerenderTable()})
    }

    def pagInfo(from: Long, to: Long, of: Long): NodeSeq = <span>Showing
      {from}
      to
      {to}
      of
      {of}
      entries</span>

    @scala.deprecated(message = "Use pagRows instead")
    override def rows(): Seq[R] = ???

    def pagRows(pagOffset: Int, pagSize: Int): (Seq[R], Long)

    override def transformPage()(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      val (rows, rowsSize) = pagRows(pagCurOffset, pagCurPageSize)
      transformPage(rows) andThen
        transformPag(rowsSize)
    }
  }

  trait SearchableTable extends Table {

    var searchQuery = Option.empty[String]

    override def keepClasses: List[String] = "mdl-search-keep" :: super.keepClasses

    @scala.deprecated(message = "Use searchRows instead")
    override def rows(): Seq[R] = ???

    def searchRows(query: Option[String]): Seq[R]

    def searchInputAttrs: Seq[ElemAttr] = Nil

    def transformSearch()(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      ".mdl-search-input" #> xsh.ajaxText(searchQuery.getOrElse(""), v => {
        val before = searchQuery
        searchQuery = Some(v.trim).filter(_ != "")
        (if (before != searchQuery) rerenderTable().P else JsCmds.Noop)
      }, searchInputAttrs: _*)
    }

    override def transformPage()(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      val rows = searchRows(searchQuery)
      transformPage(rows) andThen
        transformSearch()
    }
  }

  trait PagSearchableTable extends Table
                                   with PagTable
                                   with SearchableTable {


    @scala.deprecated(message = "Use searchAndPagRows instead")
    override final def pagRows(pagOffset: Int, pagSize: Int): (Seq[R], Long) = ???

    @scala.deprecated(message = "Use searchAndPagRows instead")
    override final def searchRows(query: Option[String]): Seq[R] = ???

    def searchAndPagRows(pagOffset: Int, pagSize: Int, query: Option[String]): (Seq[R], Long)

    override def transformPage()(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      val (rows, rowsSize) = searchAndPagRows(pagCurOffset, pagCurPageSize, searchQuery)
      transformPage(rows) andThen
        transformPag(rowsSize) andThen
        transformSearch()
    }
  }

  trait SelColsTable extends Table {

    override def keepClasses: List[String] = "mdl-selcols-keep" :: super.keepClasses

    def selColsInitialSelection: Set[C]
    lazy val selCols = collection.mutable.Set[C](selColsInitialSelection.toVector: _*)

    def allColumns(): Seq[C]
    override def columns(): Seq[C] = allColumns().filter(selCols.contains)

    def selColsColName(col: C): String

    def selColSelected: NodeSeq
    def selColUnselected: NodeSeq

    override def transformPage()(implicit xsh: XSPageHandle): NodeSeq => NodeSeq = {
      super.transformPage() andThen
        ".mdl-selcols-select" #> {
          ".mdl-selcols-col" #> allColumns().map(col => {
            ".mdl-selcols-btn [onclick]" #> xsh.ajaxInvoke(() => {
              if (selCols.size > 1 || !selCols.contains(col)) {
                if (selCols.contains(col)) selCols -= col else selCols += col
                rerenderTable()
              } else JsCmds.Noop
            }) &
              ".mdl-selcols-btn *" #> <span>
                {if (selCols.contains(col)) selColSelected else selColUnselected}{selColsColName(col)}
              </span>
          })
        }
    }

    override def colTransformTh(col: C, colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq =
      super.colTransformTh(col, colId, colIdx) andThen
        ".mdl-selcols-closebtn [onclick]" #> (xsh.ajaxInvoke(() => {if (selCols.size > 1) {selCols -= col; rerenderTable()} else JsCmds.Noop}) & stopPropagation)
  }

  trait ClickableRowsTable extends Table {

    def clickableRowsIsClickable(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): Boolean = true

    def clickableRowsOnClick(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): JsCmd = JsCmds.Noop

    override def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq =
      super.colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen
        (if (clickableRowsIsClickable(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols))
          "td [onclick+]" #> xsh.ajaxInvoke(() => clickableRowsOnClick(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols))
        else PassThru)
  }

  trait RowDetailsTable extends Table {

    def rowDetailsRender(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): NodeSeq

    def rowDetailsOpenRow(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): JsCmd = Run {
      val ns = <tr id={rowId + "-details"} class="mdl-rowdetails-tr">
        <td colspan={_cols.size + ""}>
          <div style="display:none;" class="mdl-rowdetails-content">
            {rowDetailsRender(row, rowId, rowIdx, _rows, _cols)}
          </div>
        </td>
      </tr>
      sel(rowId, s".after(${ns.toString().encJs});") +
        sel(s"$rowId-details .mdl-rowdetails-content", ".slideDown(400);")
    }

    protected def rowDetailsCloseRow(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): JsCmd = Run {
      s"""${sel(s"$rowId-details .mdl-rowdetails-content")}.slideUp(400, function() {${sel(s"$rowId-details")}.remove();});"""
    }
  }

  trait RowDetailsOnClickTable extends Table
                                       with RowDetailsTable
                                       with ClickableRowsTable {

    var rowDetailsOnClickCurOpen: Option[(R, JsCmd)] = None

    override def clickableRowsOnClick(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): JsCmd =
      super.clickableRowsOnClick(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) & {
        rowDetailsOnClickCurOpen match {
          case Some((prev, close)) if prev == row =>
            rowDetailsOnClickCurOpen = None
            close
          case None =>
            rowDetailsOnClickCurOpen = Some((row, rowDetailsCloseRow(row, rowId, rowIdx, _rows, _cols)))
            rowDetailsOpenRow(row, rowId, rowIdx, _rows, _cols)
          case Some((_, close)) =>
            rowDetailsOnClickCurOpen = Some((row, rowDetailsCloseRow(row, rowId, rowIdx, _rows, _cols)))
            close & rowDetailsOpenRow(row, rowId, rowIdx, _rows, _cols)
        }
      }
  }

  trait FixedHeaderTable extends Table {

    case class FixedHeaderOptions(
                                   position: Option[String] = Some("fixed"),
                                   scrollContainer: Option[RawJSON] = None,
                                   headerCellSelector: Option[String] = None,
                                   floatTableClass: Option[String] = None,
                                   floatContainerClass: Option[String] = None,
                                   top: Option[RawJSON] = None,
                                   bottom: Option[RawJSON] = None,
                                   zIndex: Option[Int] = None,
                                   debug: Option[Boolean] = None,
                                   getSizingRow: Option[RawJSON] = None,
                                   copyTableClass: Option[Boolean] = None,
                                   enableAria: Option[Boolean] = None,
                                   autoReflow: Option[Boolean] = None
                                 )

    def fixedHeaderOptions: FixedHeaderOptions = FixedHeaderOptions()

    override def renderTable()(implicit xsh: XSPageHandle): NodeSeq =
      super.renderTable() ++
        Tail.render(Script(OnLoad(Run( s"""$$('#$tableId').floatThead(${JSON.L.writeValueAsString(fixedHeaderOptions)});"""))))

    override def onRerenderTable(): JsCmd = Run( s"""$$('#$tableId').floatThead('destroy');""") & super.onRerenderTable()
  }

  trait LazyLoadingTable extends Table {

    override def rerenderTable(): JsCmd = Run( s"""$$('#$tableId').floatThead('destroy');""") & super.rerenderTable()

    def lazyLoadingPlaceholder = <div style="text-align:center; margin: 20px 0;">
      <img src="/static/img/ajax-loader.gif"></img>
    </div>

    override def renderTable()(implicit xsh: XSPageHandle): NodeSeq =
      <div id={wrapperId}>
        {lazyLoadingPlaceholder}
      </div> ++
        Tail.render(Script(OnLoad(xsh.ajaxInvoke(() => Replace(wrapperId, super.renderTable())))))
  }

  trait SortableRowsTable extends Table {

    def sortableRowsIsSortable(row: R): Boolean = true

    def sortableRowsOnSort(row: R, idx: Int): JsCmd = Noop

    override def tableClasses: List[String] = "mdl-sortablerows" :: super.tableClasses

    override def tdClasses(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]) =
      if (sortableRowsIsSortable(row)) super.tdClasses(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols)
      else "mdl-no-rowsort" :: super.tdClasses(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols)

    //    override protected def rowTransforms(row: R, rowId: String, rowIdx: Int, rrdRow: () => JsCmd, rows: Seq[(String, R)])(implicit xsh: XSPageHandle): NodeSeq => NodeSeq =
    //      super.rowTransforms(row, rowId, rowIdx, rrdRow, rows) andThen
    //        "tr [reorder]" #> SHtml.ajaxCall(JsRaw("idx"), idx => {
    //          sortedRow(row, idx.toInt) & rerenderTable()
    //        }).toJsCmd

    override def transformTr(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq =
      super.transformTr(row, rowId, rowIdx, _rows, _cols) andThen
        "tr [reorder]" #> xsh.ajaxCall(JsRaw("idx"), idx => sortableRowsOnSort(row, idx.toInt))

    def sortableRowsReorderRowMinDistancePx = 15

    override def renderTable()(implicit xsh: XSPageHandle): NodeSeq =
      super.renderTable() ++ Tail.render(Script(Run(
        s"""$$('#$tbodyId').sortable({
           |  axis: 'y',
           |  items: 'tr:not(.mdl-no-rowsort)',
           |  distance: $sortableRowsReorderRowMinDistancePx,
           |  update:
           |    function( event, ui ) {
           |      var sorted =
           |        $$.map(
           |          $$.map($$('#$tbodyId tr'),
           |          function(r) {return {id:$$(r).attr('id'), offset: r.offsetTop};}
           |        )
           |          .sort(function(a,b){return a.offset-b.offset;}), function(r) {return r.id;});
           |      var idx = sorted.indexOf(ui.item.attr('id'));
           |      eval('0, ' + ui.item.attr('reorder'));
           |  },
           |  helper:
           |    function(e, ui) {
           |      ui.children().each(function() { $$(this).width($$(this).width()); });
           |      return ui;
           |    }
           |})""".stripMargin
      )))
  }

  trait SimpleTable
    extends Table
            with SortableTable
            with PagSearchableTable
            with SelColsTable
            with RowDetailsOnClickTable
            with FixedHeaderTable
            with LazyLoadingTable
            with SortableRowsTable {

    val template: NodeSeq = (new Html5Parser {}).parse(play.api.Play.current.resource(s"/public/templates/simple-table.html").get.openStream()).get

    def allRows(): Seq[R]

    def selColSelected: NodeSeq = TH.IcnFA.icn_check_square_o.icn
    def selColUnselected: NodeSeq = TH.IcnFA.icn_square_o.icn

    override def searchAndPagRows(pagOffset: Int, pagSize: Int, query: Option[String]): (Seq[R], Long) = {
      val all: Seq[R] = allRows().filter(r => query.isEmpty || columns().exists(_.matchWith.map(_ (r, query.get)).getOrElse(false)))
      val rows =
        sortableSortCol.map(sortRowsBy => sortRowsBy.sortBy.get(all).|>(rows => if (sortableSortAsc()) rows else rows.reverse)).getOrElse(all)
          .drop(pagOffset).take(pagSize)
      (rows, all.size)
    }

    override def pagBtnsCurrentClass: String = "active"
    override def pagBtnsDisabledClass: String = "disabled"

    override def sortableDefaultSortCol(): Option[Column] = columns().filter(sortableIsSortable).headOption

    override def sortableSortAscClass: String = "sorting_asc"
    override def sortableSortDescClass: String = "sorting_desc"
    override def sortableSortNeutralClass: String = "sorting"

    override def colTransformTh(col: C, colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = super.colTransformTh(col, colId, colIdx) andThen col.colRenderHead(colId, colIdx)
    override def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = super.colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen col.colRenderRow(row, rowId, rowIdx, colId, colIdx, _rows, _cols)

    override def sortableIsSortable(col: C) = col.sortBy.isDefined

    override type C = Column

    override def selColsInitialSelection: Set[Column] = allColumns().toSet
    override def selColsColName(col: Column): String = col.name


    override def colIdFor(col: Column): String = col.name

    trait Column {

      def name: String
      def sortBy: Option[Seq[R] => Seq[R]]
      def matchWith: Option[(R, String) => Boolean]
      def colRenderHead(colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = PassThru
      def colRenderFoot(colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = PassThru
      def colRenderRow(row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = PassThru
    }

    def ColStr(colName: String, contentstd: R => (String), sorter: Option[Seq[R] => Seq[R]] = None, matches: Option[(R, String) => Boolean] = None): C = new Column {

      override def name: String = colName

      override def sortBy: Option[Seq[R] => Seq[R]] = sorter

      override def matchWith: Option[(R, String) => Boolean] = matches

      override def colRenderRow(row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq =
        super.colRenderRow(row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen
          "td *" #> contentstd(row)

      override def colRenderHead(colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq =
        super.colRenderHead(colId, colIdx) andThen
          ".mdl-th-title" #> name

      override def colRenderFoot(colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq =
        super.colRenderFoot(colId, colIdx) andThen
          ".mdl-th-title" #> name
      override def toString: String = name
    }
  }

}


