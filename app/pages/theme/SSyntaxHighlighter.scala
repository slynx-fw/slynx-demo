package pages.theme

import net.liftweb.builtin.snippet.Tail
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.{Run, OnLoad, Script}

import scala.xml.NodeSeq

trait SSyntaxHighlighter {

  case class Highlight(
                        code: String,
                        autoLinks: Option[Boolean] = None,
                        className: Option[String] = None,
                        collapse: Option[Boolean] = None,
                        firstLine: Option[Int] = None,
                        highlight: Option[Array[Int]] = None,
                        gutter: Option[Boolean] = None,
                        htmlScript: Option[Boolean] = None,
                        smartTabs: Option[Boolean] = None,
                        toolSize: Option[Int] = None,
                        toolBar: Option[Boolean] = None
                        ) {

    def autoLinks(autoLinks_value: Boolean): Highlight = copy(autoLinks = Some(autoLinks_value))
    def className(className_value: String): Highlight = copy(className = Some(className_value))
    def collapse(collapse_value: Boolean): Highlight = copy(collapse = Some(collapse_value))
    def firstLine(firstLine_value: Int): Highlight = copy(firstLine = Some(firstLine_value))
    def highlight(highlight_value: Array[Int]): Highlight = copy(highlight = Some(highlight_value))
    def gutter(gutter_value: Boolean): Highlight = copy(gutter = Some(gutter_value))
    def htmlScript(htmlScript_value: Boolean): Highlight = copy(htmlScript = Some(htmlScript_value))
    def smartTabs(smartTabs_value: Boolean): Highlight = copy(smartTabs = Some(smartTabs_value))
    def toolSize(toolSize_value: Int): Highlight = copy(toolSize = Some(toolSize_value))
    def toolBar(toolbar_value: Boolean): Highlight = copy(toolBar = Some(toolbar_value))

    def show(): String = {
      val options = List(
        autoLinks.map(v => "auto-links: " + v)
        , className.map(v => "class-name: " + v.toString)
        , collapse.map(v => "collapse: " + v)
        , firstLine.map(v => "first-line: " + v)
        , highlight.map(v => "highlight: " + v)
        , gutter.map(v => "gutter: " + v)
        , htmlScript.map(v => "html-scriptv: " + v)
        , smartTabs.map(v => "smart-tabs" + v)
        , toolSize.map(v => "tool-size: " + v)
        , toolBar.map(v => "toolbar: " + v)
      ).flatten.reduceOption(_ + ",\n" + _).getOrElse("")
      s"""brush: scala; $options;"""
    }


    def render(): NodeSeq = {
      <script type="syntaxhighlighter" class={show()}>{scala.xml.PCData(code)}</script> ++
        Tail.render(Script(OnLoad(Run("SyntaxHighlighter.highlight();"))))
    }
  }

}

