package pages.theme

import util.ImplicitHelpers._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds.{OnLoad, Run, Script}
import net.liftweb.json.JValue
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.Serialization.write
import net.liftweb.json._
import net.liftweb.util.Helpers._
import util.{RawJSON, JsonSerializable, JSON}

import scala.xml.NodeSeq

trait SFCharts {

  /**
   * @param options
   * @param data
   * @param style
   * @param plotClick Run on plot click: event, pos, item are available
   * @param plotHover Run on plot hover: event, pos, item are available
   */
  case class FChart(
                     options: FCharts.Options,
                     data: List[FCharts.Data],
                     style: String = "height: 300px;",

                     /**
                      * event, pos, item are available
                      * item: {
                      * datapoint: the point, e.g. [0, 2]
                      * dataIndex: the index of the point in the data array
                      * series: the series object
                      * seriesIndex: the index of the series
                      * pageX, pageY: the global screen coordinates of the point
                      * }
                      */
                     plotClick: JsCmd = JsCmds.Noop,

                     /**
                      * event, pos, item are available
                      * item: {
                      * datapoint: the point, e.g. [0, 2]
                      * dataIndex: the index of the point in the data array
                      * series: the series object
                      * seriesIndex: the index of the series
                      * pageX, pageY: the global screen coordinates of the point
                      * }
                      */
                     plotHover: JsCmd = JsCmds.Noop
                     ) extends Id {

    val chartId = id('chart)

    def renderChart(): NodeSeq = {
      implicit val formats = Serialization.formats(NoTypeHints)
      val optionsJson = JSON.L.writeValueAsString(options)
      val dataJson = JSON.L.writeValueAsString(data)
      <div id={chartId} style={style}></div> ++
        <tail>{Script(OnLoad(Run(s"""
          $$.plot('#$chartId', $dataJson, $optionsJson);
          $$('#$chartId').bind("plotclick", function (event, pos, item) { ${plotClick.toJsCmd} });
          $$('#$chartId').bind("plothover", function (event, pos, item) { ${plotHover.toJsCmd} });
          """)))}</tail>
    }
  }

  object FChartsImplicits {
    implicit def toOption[T](v: T): Option[T] = Some(v)
    implicit def toList[T](v: T): List[T] = List(v)
  }

  object FCharts {

    case class Options(
                        grid: Option[Grid] = None,
                        series: Option[Series] = None,
                        xaxis: Option[Axis] = None,
                        yaxis: Option[Axis] = None,
                        xaxes: List[Axis] = Nil,
                        yaxes: List[Axis] = Nil,
                        color: Option[List[String]] = None,
                        interaction: Option[Interaction] = None,
                        legend: Option[Legend] = None
                        )

    case class Interaction(redrawOverlayInterval: Option[Double] = None)

    case class Legend(
                       show: Option[Boolean] = None,
                       noColumns: Option[Int] = None,
                       labelFormatter: Option[RawJSON] = None,
                       labelBoxBorderColor: Option[String] = None,
                       container: Option[RawJSON] = None,
                       position: Option[String] = None,
                       margin: Option[Int] = None,
                       backgroundColor: Option[String] = None,
                       backgroundOpacity: Option[Int] = None,
                       sorted: Option[String] = None
                       )

    case class Range(
                      from: Option[Int] = None,
                      to: Option[Int] = None
                      )

    case class Marking(
                        color: Option[String] = None,
                        lineWidth: Option[Int] = None,
                        xaxis: Option[Range] = None,
                        yaxis: Option[Range] = None
                        )

    case class Grid(
                     borderColor: Option[String] = None,
                     borderWidth: Option[Int] = None,
                     tickColor: Option[String] = None,
                     show: Option[Boolean] = None,
                     aboveData: Option[Boolean] = None,
                     color: Option[String] = None,
                     backgroundColor: Option[String] = None,
                     margin: Option[Int] = None,
                     labelMargin: Option[Int] = None,
                     axisMargin: Option[Int] = None,
                     markingsLineWidth: Option[Int] = None,
                     markingsColor: Option[String] = None,
                     markings: Option[List[Marking]] = None,
                     minBorderMargin: Option[Int] = None,
                     clickable: Option[Boolean] = None,
                     hoverable: Option[Boolean] = None,
                     autoHighlight: Option[Boolean] = None,
                     mouseActiveRadius: Option[Int] = None
                     )

    case class Points(
                       show: Option[Boolean] = None,
                       color: Option[String] = None,
                       lineWidth: Option[Int] = None,
                       fill: Option[Boolean] = None,
                       fillColor: Option[String] = None,
                       radius: Option[Int] = None,
                       symbol: Option[String] = None
                       )

    case class Series(
                       shadowSize: Option[Int] = None,
                       highlightColor: Option[String] = None,
                       lines: Option[Lines] = None,
                       bars: Option[Bars] = None,
                       points: Option[Points] = None,
                       pie: Option[Pie] = None
                       )

    case class Lines(
                      show: Option[Boolean] = None,
                      zero: Option[Boolean] = None,
                      color: Option[String] = None,
                      lineWidth: Option[Int] = None,
                      fill: Option[Boolean] = None,
                      fillColor: Option[String] = None,
                      steps: Option[Boolean] = None
                      )

    case class Bars(
                     show: Option[Boolean] = None,
                     zero: Option[Boolean] = None,
                     color: Option[String] = None,
                     lineWidth: Option[Int] = None,
                     fill: Option[Boolean] = None,
                     fillColor: Option[String] = None,
                     barWidth: Option[Double] = None,
                     align: Option[String] = None,
                     horizontal: Option[Boolean] = None
                     )

    case class Like(
                     size: Option[Int] = None,
                     lineHeight: Option[Int] = None,
                     style: Option[String] = None,
                     weight: Option[String] = None,
                     family: Option[String] = None,
                     variant: Option[String] = None
                     )


    case class MinTickSize(value: Int, unit: String) extends JsonSerializable {override def json(): Option[String] = Some(s"[$value, ${unit.encJs}]")}

    case class Axis(
                     show: Option[Boolean] = None,
                     mode: Option[String] = None,
                     position: Option[String] = None,
                     font: Option[Like] = None,
                     color: Option[String] = None,
                     tickColor: Option[String] = None,
                     transform: Option[RawJSON] = None,
                     inverseTransform: Option[RawJSON] = None,
                     min: Option[Long] = None,
                     max: Option[Long] = None,
                     autoscaleMargin: Option[Double] = None,
                     //ticks: Option[RawJSON] = None, // either [1, 3] or [[1, "a"], 3] or (fn: axis info -> ticks) or app. number of ticks for auto-ticks
                     tickFormatter: Option[RawJSON] = None,
                     labelWidth: Option[Int] = None,
                     labelHeight: Option[Int] = None,
                     reserveSpace: Option[Boolean] = None,
                     tickLength: Option[Int] = None,
                     alignTicksWithAxis: Option[Int] = None,
                     tickDecimals: Option[Double] = None,
                     tickSize: Option[Int] = None,
                     minTickSize: Option[MinTickSize] = None,
                     timeformat: Option[String] = None
                     )

    case class Data(
                     data: List[List[Double]],
                     color: Option[String] = None,
                     label: Option[String] = None,
                     xaxis: Option[Int] = None,
                     yaxis: Option[Int] = None
                     )

    case class Shadow(
                       left: Option[Int] = None,
                       top: Option[Int] = None,
                       alpha: Option[Double] = None
                       )

    case class Pie(
                    show: Option[Boolean] = None,
                    radius: Option[Double] = None,
                    innerRadius: Option[Double] = None,
                    startAngle: Option[Int] = None,
                    tilt: Option[Double] = None,
                    offset: Option[Offset] = None,
                    combine: Option[Combine] = None,
                    stroke: Option[Stroke] = None,
                    label: Option[Label] = None,
                    highlight: Option[Highlight] = None,
                    shadow: Option[Shadow] = None
                    )


    case class Offset(
                       top: Option[Int] = None,
                       left: Option[Int] = None
                       )

    case class Stroke(
                       color: Option[String] = None,
                       width: Option[Int] = None
                       )

    /**
     *
     * @param show
     * @param formatter
     * @param radius radius at which to place the labels (based on full calculated radius if <=1, or hard pixel value)
     * @param background
     * @param threshold
     */
    case class Label(
                      show: Option[Boolean] = None,
                      formatter: Option[RawJSON] = None,
                      radius: Option[Double] = None,
                      background: Option[Background] = None,
                      threshold: Option[Double] = None
                      )

    case class Background(
                           color: Option[String] = None,
                           opacity: Option[Double] = None
                           )

    case class Combine(
                        threshold: Option[Double] = None,
                        color: Option[String] = None,
                        label: Option[String] = None
                        )

    case class Highlight(opacity: Option[Double] = None)

  }

}

