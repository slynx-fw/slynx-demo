package pages

import java.io.InputStream
import java.net.URLEncoder
import java.text._
import java.util.Date
import java.util.zip.ZipInputStream

import controllers.RootController.XSPageHandle
import net.liftweb.common.Full
import net.liftweb.http.SHtml
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{OnLoad, Script, Run}
import net.liftweb.util.Helpers._
import pages.theme._
import pages.theme.Libs.Lib
import pages.theme.Styles._
import play.api.Play
import util.RawJSON

import scala.io.Source
import scala.xml.NodeSeq


class FlotChartsPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  import TH.FChartsImplicits._
  import TH.FCharts._

  override def pageCodeFile = "app/pages/FlotChartsPage.scala"

  override def pageTitle: String = "Flot Charts"
  override def pageSubtitle: String = "Flot Charts Examples"

  override def libs: Set[Lib] = {
    import Libs._
    super.libs + jquery_flot + jquery_flot_resize + jquery_flot_pie + jquery_flot_categories + jquery_flot_time + jquery_knob
  }
  override def styles: Set[Style] = {
    import Styles._
    super.styles + shCore + shThemeDefault
  }

  lazy val lineChartExampleWidget = {
    new TH.Widget {
      // lineChartExampleWidget
      override def widgetTitle: String = "Line Chart Example"
      override def widgetBody: NodeSeq = {
        val sin = (1 to 100).map(_ / 2d).map(i => List(i, math.sin(i))).toList
        val cos = (1 to 100).map(_ / 2d).map(i => List(i, math.cos(i))).toList
        TH.FChart(
          options = Options(
            grid = Grid(borderColor = "#f3f3f3", borderWidth = 1, tickColor = "#f3f3f3"),
            series = Series(
              lines = Lines(show = true, fill = false, color = "#3c8dbc"),
              points = Points(show = true))
          ),
          data = List(Data(data = sin, color = "#3c8dbc"), Data(data = cos, color = "#13547A"))
        ).renderChart()
      }
    }.renderedWidget++
      // lineChartExampleWidget
      codeWidget("lineChartExampleWidget")
  }

  lazy val barChartExampleWidget = {
    // barChartExampleWidget
    new TH.Widget {
      override def widgetTitle: String = "Bar Chart Example"
      override def widgetBody: NodeSeq = {
        val sin = (1 to 10).map(_ / 4d - 0.2).map(i => List(i, math.sin(i))).toList
        TH.FChart(
          options = Options(
            grid = Grid(hoverable = true, borderColor = "#f3f3f3", tickColor = "#f3f3f3"),
            series = Series(
              bars = Bars(
                barWidth = 0.05, show = true,
                fillColor = "rgba(77, 179, 238, 0.31)", align = "center"
              )
            ),
            xaxis = Axis(tickFormatter = RawJSON("function(v) {return v + '!';}"))
          ),
          data = List(Data(data = sin, color = "#3c8dbc"), Data(data = sin, color = "#13547A"))
        ).renderChart()
      }
    }.renderedWidget++
      // barChartExampleWidget
      codeWidget("barChartExampleWidget")
  }
  // pieChartExample1
  lazy val pieChartExample1 = {
    TH.FChart(
      options = Options(series = Series(pie = Pie(show = true))),
      data = List(
        Data(data = (List(List(1, 0.511))), color = "#00437a", label = "Barack Obama"),
        Data(data = (List(List(1, 0.472))), color = "#e22e2d", label = "Mitt Romney")
      ),
      style = "height:200px;"
    ).renderChart()

  }

  lazy val pieChartExample2 = {
    TH.FChart(
      options = Options(
        series = Series(
          pie = Pie(
            show = true, radius = 1d,
            label = Label(
              show = true, radius = 0.75,
              background = Background(opacity = 0.9, color = "#fff"),
              formatter = RawJSON(
                """function(s,v) { return '<div style="color:black;text-align:center;">'+s+'<br/>'+v.percent.toFixed(1)+'%</div>'; }"""
              )
            )
          )
        ),
        legend = Legend(show = false)
      ),
      data = List(
        Data(data = (List(List(1, 0.511))), color = "#00437a", label = "Barack Obama"),
        Data(data = (List(List(1, 0.472))), color = "#e22e2d", label = "Mitt Romney")
      ),
      style = "height:200px;"
    ).renderChart()
    // pieChartExample1
  }

  lazy val pieChartExample1Widget = {
    new TH.Widget {
      override def widgetTitle: String = "Pie Chart Example 1"
      override def widgetBody: NodeSeq = pieChartExample1
    }.renderedWidget
  }

  lazy val pieChartExample2Widget = {

    new TH.Widget {
      override def widgetTitle: String = "Pie Chart Example 2"
      override def widgetBody: NodeSeq = pieChartExample2
    }.renderedWidget
  }

  lazy val pieChartsCodeWidget1_2 = {
    codeWidget("pieChartExample1")
  }
  // pieChartExample2
  lazy val pieChartExample3 = {
    TH.FChart(
      options = Options(series = Series(pie = Pie(show = true, innerRadius = 0.5))),
      data = List(
        Data(data = (List(List(1, 0.273))), color = "#3265cb", label = "Russia")
        , Data(data = (List(List(1, 0.16))), color = "#dc3811", label = "Canada")
        , Data(data = (List(List(1, 0.154))), color = "#ff9800", label = "China")
        , Data(data = (List(List(1, 0.154))), color = "#109618", label = "United States")
        , Data(data = (List(List(1, 0.136))), color = "#980098", label = "Brazil")
        , Data(data = (List(List(1, 0.123))), color = "#0098c6", label = "Australia")
      ),
      style = "height:200px;"
    ).renderChart()
  }

  lazy val pieChartExample4 = {
    TH.FChart(
      options = Options(series = Series(pie = Pie(show = true)), grid = Grid(hoverable = true, clickable = true), legend = Legend(show = false)),
      data = List(
        Data(data = (List(List(1, 0.273))), color = "#3265cb", label = "Russia")
        , Data(data = (List(List(1, 0.16))), color = "#dc3811", label = "Canada")
        , Data(data = (List(List(1, 0.154))), color = "#ff9800", label = "China")
        , Data(data = (List(List(1, 0.154))), color = "#109618", label = "United States")
        , Data(data = (List(List(1, 0.136))), color = "#980098", label = "Brazil")
        , Data(data = (List(List(1, 0.123))), color = "#0098c6", label = "Australia")
      ),
      style = "height:200px;"
    ).renderChart()
  }
  // pieChartExample2
  lazy val pieChartExample3Widget = {
    new TH.Widget {
      override def widgetTitle: String = "Pie Chart Example 3"
      override def widgetBody: NodeSeq = pieChartExample3
    }.renderedWidget
  }

  lazy val pieChartExample4Widget = {

    new TH.Widget {
      override def widgetTitle: String = "Pie Chart Example 4"
      override def widgetBody: NodeSeq = pieChartExample4
    }.renderedWidget
  }

  lazy val pieChartsCodeWidget3_4 = {
    codeWidget("pieChartExample2")
  }

  lazy val pieChartExample5 = {
    // pieChartExample3
    TH.FChart(
      options = Options(series = Series(
        pie = Pie(show = true,
          radius = 500.0,
          label = Label(show = true, threshold = 0.1,
            background = Background(opacity = 0.9, color = "#fff"),
            formatter = RawJSON(
              """function(s,v) { return '<div style="color:black;text-align:center;padding: 0 3px;">'+s+'</div>'; }"""
            )
        ))),
        legend = Legend(show = false)),
      data = List(
        Data(data = (List(List(1, 0.273))), color = "#00437a", label = "Jan")
        , Data(data = (List(List(1, 0.16))), color = "#bd3c37", label = "Feb")
        , Data(data = (List(List(1, 0.154))), color = "#8cb042", label = "Mar")
        , Data(data = (List(List(1, 0.154))), color = "#664a87", label = "Apr")
        , Data(data = (List(List(1, 0.136))), color = "#38a7c5", label = "May")
        , Data(data = (List(List(1, 0.123))), color = "#d87323", label = "Jun")
      ),
      style = "height:200px;"
    ).renderChart()
  }

  lazy val pieChartExample6 = {
    TH.FChart(
      options = Options(series = Series(
        pie = Pie(show = true, tilt = 0.5,
          label = Label(show = true,
            background = Background(opacity = 0.9, color = "#fff"),
            formatter = RawJSON(
              """function(s,v) { return '<div style="color:black;text-align:center;padding: 0 3px;">'+s+'</div>'; }"""
            )
            ))),
        legend = Legend(show = false)),
      data = List(
        Data(data = (List(List(1, 0.273))), color = "#00437a", label = "Jan")
        , Data(data = (List(List(1, 0.16))), color = "#bd3c37", label = "Feb")
        , Data(data = (List(List(1, 0.154))), color = "#8cb042", label = "Mar")
        , Data(data = (List(List(1, 0.154))), color = "#664a87", label = "Apr")
        , Data(data = (List(List(1, 0.136))), color = "#38a7c5", label = "May")
        , Data(data = (List(List(1, 0.123))), color = "#d87323", label = "Jun")
      ),
      style = "height:200px;"
    ).renderChart()
  }
  // pieChartExample3
  lazy val pieChartExample5Widget = {
    new TH.Widget {
      override def widgetTitle: String = "Pie Chart Example 5"
      override def widgetBody: NodeSeq = pieChartExample5
    }.renderedWidget
  }

  lazy val pieChartExample6Widget = {

    new TH.Widget {
      override def widgetTitle: String = "Pie Chart Example 6"
      override def widgetBody: NodeSeq = pieChartExample6
    }.renderedWidget
  }

  lazy val pieChartsCodeWidget5_6 = {
    codeWidget("pieChartExample3")
  }

  lazy val multipleAxisChartExampleWidget = {
    // multipleAxisChartExampleWidget
    new TH.Widget {
      override def widgetTitle: String = "Multiple Axes Chart Example"
      override def widgetBody: NodeSeq = {
        TH.FChart(
          options = Options(
            xaxes = Axis(mode = "time"),
            yaxes = List(
              Axis(min = 0, position = "left"),
              Axis(
                alignTicksWithAxis = 1,
                position = "right",
                tickFormatter = RawJSON("""function(v, axis) { return v.toFixed(axis.tickDecimals) + "€"; }""")
              )
            ),
            legend = Legend(position = "sw")
          ),
          data = List(
            Data(data = TH.DemoData.oilprices, label = "Oil price ($)"),
            Data(data = TH.DemoData.exchangerates, label = "USD/EUR exchange rate", yaxis = 2)
          )).renderChart()
      }
    }.renderedWidget++
    // multipleAxisChartExampleWidget
      codeWidget("multipleAxisChartExampleWidget")
  }

  lazy val timeAxisChartExampleWidget = {
    // timeAxisChartExampleWidget
    new TH.Widget {
      override def widgetTitle: String = "Time Axes Chart Example"
      override def widgetBody: NodeSeq = {
        TH.FChart(
          options = Options(
            grid = Grid(hoverable = true, clickable = true),
            xaxis = Axis(
              mode = "time",
              minTickSize = MinTickSize(1, "year"),
              min = TH.DemoData.oilPrices2.view.map(_._1).min,
              max = TH.DemoData.oilPrices2.view.map(_._1).max
            ),
            legend = Legend(show = false)
          ),
          data = List(Data(data = TH.DemoData.oilPrices2.map(p => List[Double](p._1, p._2))))
        ).renderChart()
      }
    }.renderedWidget++
      // timeAxisChartExampleWidget
      codeWidget("timeAxisChartExampleWidget")
  }

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {
      TH.Row(
        TH.Col().lg6 {
          lineChartExampleWidget
        },
        TH.Col().lg6 {
          barChartExampleWidget
        }
      ) ++
        TH.Row(
          TH.Col().lg12 {
            TH.Row(
              TH.Col().lg3 {
                pieChartExample1Widget
              },
              TH.Col().lg3 {
                pieChartExample2Widget
              },
              TH.Col().lg3 {
                pieChartExample3Widget
              },
              TH.Col().lg3 {
                pieChartExample4Widget
              }
            )
          }) ++
        TH.Row(
          TH.Col().lg12 {
            TH.Row(
              TH.Col().lg6 {
                pieChartsCodeWidget1_2
              },
              TH.Col().lg6 {
                pieChartsCodeWidget3_4
              }
            )
          }) ++
      TH.Row(
        TH.Col().lg6 {
        TH.Row(
          TH.Col().lg6 {
            pieChartExample5Widget
          },
          TH.Col().lg6 {
            pieChartExample6Widget
          }) ++
        TH.Row(
          TH.Col().lg12 {
            pieChartsCodeWidget5_6
          }
        )},
          TH.Col().lg6 {
            multipleAxisChartExampleWidget
          }) ++
        TH.Row(
          TH.Col().lg12 {
            timeAxisChartExampleWidget
          })
    })
}

