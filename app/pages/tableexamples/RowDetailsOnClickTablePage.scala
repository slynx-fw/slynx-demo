package pages.tableexamples

import util.ImplicitHelpers._
import com.omertron.imdbapi.ImdbApi
import com.omertron.imdbapi.model.ImdbList
import controllers.RootController.XSPageHandle
import net.liftweb.util.Helpers._
import net.liftweb.util.{Html5Parser, PassThru}
import pages.theme.Libs.Lib
import pages.theme.Styles.Style
import pages.theme.{Libs, Styles}
import pages.{Menu, DemoCodePage, MenuPage}
import play.twirl.api.TemplateMagic.javaCollectionToScala

import scala.xml.NodeSeq


class RowDetailsOnClickTablePage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile = "app/pages/tableexamples/RowDetailsOnClickTablePage.scala"
  override def pageTitle: String = Menu.tableSectionRowDetailsOnClickTable.name
  override def pageSubtitle: String = ""

  override def libs: Set[Lib] = {
    import Libs._
    super.libs + jquery_data_tables + data_tables_bootstrap + jquery_floatThead + jquery_floatThead + jquery_ui + toastr
  }

  override def styles: Set[Style] = {
    import Styles._
    super.styles + data_tables_bootstrap + toastr
  }

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {
      TH.Row("margin-bottom: 30px;")(
        TH.Col().md12 {
          // RowDetailsOnClickTablePage
          new TH.Widget {
            override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
            override def solid: Boolean = true
            override def widgetTitle: String = "Row Details OnClick Table Example"
            override def widgetBody: NodeSeq = new TH.RowDetailsOnClickTable {


              override def rowDetailsRender(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, Column)])(implicit xsh: XSPageHandle): NodeSeq = {
                <p>
                  <img style="float:left;max-height:150px;margin: 20px;" onclick={xsh.ajaxInvoke(() => TH.SimpleModal(
                    row.getTitle,
                    (_, _) => <div style="text-align:center;"><img src={row.getImage.getUrl.replaceAllLiterally("http://ia.media-imdb.com/", "/imdb/")} style="max-height:600px;"></img></div>,
                    modalOnSaveClientSideF = (modal, _) => modal.hideAndDestroy()
                  ).showAndInstall()).toJsCmd} src={row.getImage.getUrl.replaceAllLiterally("http://ia.media-imdb.com/", "/imdb/")}></img>
                  <h3>{row.getTitle} ({row.getYear})</h3>
                  <b>Type:</b> {row.getType} <br/>
                  <b>Rating:</b> {row.getRating} <br/>
                  <div style="clear:both"></div>
                </p>
              }

              override val template: NodeSeq = loadHtmlFile(s"/public/templates/simple-table.html")

              override def colTransformTh(col: C, colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = super.colTransformTh(col, colId, colIdx) andThen col.colRenderHead(colId, colIdx)
              override def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = super.colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen col.colRenderRow(row, rowId, rowIdx, colId, colIdx, _rows, _cols)

              type C = Column

              override def colIdFor(col: Column): String = col.name


              trait Column {

                def name: String
                def colRenderHead(colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = PassThru
                def colRenderFoot(colId: String, colIdx: Int)(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = PassThru
                def colRenderRow(row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)])(implicit xsh: XSPageHandle): (NodeSeq) => NodeSeq = PassThru
              }

              def ColStr(colName: String, contentstd: R => (String)): C = new Column {

                override def name: String = colName

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

              override def parentXsh: XSPageHandle = page.xsh
              override type R = ImdbList

              override def columns(): Seq[C] = List(
                ColStr("Title", r => r.getTitle),
                ColStr("Type", r => r.getType),
                ColStr("Year", r => r.getYear.toString),
                ColStr("Rating", r => r.getRating.toString)
              )

              override def rows(): Seq[ImdbList] = Profile.%?("getTop250")(new ImdbApi().getTop250.toVector).take(40)
            }.renderedTable
          }.renderedWidget ++
            // RowDetailsOnClickTablePage
            codeWidget("RowDetailsOnClickTablePage")
        }
      )
    }
    )
}
