package pages.theme

import scala.xml.{NodeSeq, Text}


trait BWATypography {

  object Txt {
    def muted(ns: NodeSeq): NodeSeq = <span class="muted">{ns}</span>
    def muted(s: String): NodeSeq = muted(Text(s))

    def warning(ns: NodeSeq): NodeSeq = <span class="text-warning">{ns}</span>
    def warning(s: String): NodeSeq = warning(Text(s))

    def error(ns: NodeSeq): NodeSeq = <span class="text-error">{ns}</span>
    def error(s: String): NodeSeq = error(Text(s))

    def info(ns: NodeSeq): NodeSeq = <span class="text-info">{ns}</span>
    def info(s: String): NodeSeq = info(Text(s))

    def success(ns: NodeSeq): NodeSeq = <span class="text-success">{ns}</span>
    def success(s: String): NodeSeq = success(Text(s))

  }

}
