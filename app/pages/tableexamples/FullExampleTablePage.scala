package pages.tableexamples

import util.ImplicitHelpers._
import com.omertron.imdbapi.ImdbApi
import com.omertron.imdbapi.model.ImdbList
import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JsCmd
import net.liftweb.util.Helpers._
import pages.theme.Libs.Lib
import pages.theme.Styles.Style
import pages.theme.{Libs, Styles}
import pages.{Menu, DemoCodePage, MenuPage}
import play.twirl.api.TemplateMagic.javaCollectionToScala

import scala.xml.NodeSeq


class FullExampleTablePage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageCodeFile = "app/pages/tableexamples/FullExampleTablePage.scala"
  override def pageTitle: String = Menu.tableSectionFullExample.name
  override def pageSubtitle: String = "Table with simple utilization, but many advanced features"

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
          // SimpleTable
          new TH.Widget {
            override def style: TH.WidgetStyle.Value = TH.WidgetStyle.Primary
            override def solid: Boolean = true
            override def widgetTitle: String = "Simple Table Example"
            override def widgetBody: NodeSeq = new TH.SimpleTable {

              override def parentXsh: XSPageHandle = page.xsh
              override type R = ImdbList

              override lazy val allColumns: List[C] = List(
                ColStr("Title", r => r.getTitle, Some(_.sortBy(_.getTitle)), Some(_.getTitle.toLowerCase contains _.toLowerCase)),
                ColStr("Type", r => r.getType, Some(_.sortBy(_.getType)), Some(_.getType.toLowerCase contains _.toLowerCase)),
                ColStr("Year", r => r.getYear.toString, Some(_.sortBy(_.getYear)), Some(_.getYear.toString contains _.toLowerCase)),
                ColStr("Rating", r => r.getRating.toString, Some(_.sortBy(_.getRating)), Some(_.getRating.toString contains _.toLowerCase))
              )
              override def allRows(): Seq[R] = Profile.%?("getTop250")(new ImdbApi().getTop250.toVector)

              override def sortableRowsOnSort(row: R, idx: Int): JsCmd = TH.Toastr.Info("Reordered Row", s"${row.getTitle} is now in index $idx").show()

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
            }.renderedTable
          }.renderedWidget ++
            // SimpleTable
            codeWidget("SimpleTable")
        }
      )
    }
    )
}
