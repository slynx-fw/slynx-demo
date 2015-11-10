package util

import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds.Run

import scala.xml.NodeSeq

object ImplicitHelpers {

  implicit class Pipe[T](val v: T) extends AnyVal {
    def |>[U](f: T => U) = f(v)
    def $$[U](f: T => U): T = { f(v); v }
    def #!(str: String = "", fmt: T => String = _.toString): T = { println(str + fmt(v)); v }
  }

  implicit class RichJsCmd(c: JsCmd) {
    def P = Run("window.console && console.log(" + c.toJsCmd.encJs + ");") & c
  }

  object Profile {
    var idx = 0
    var lastOpen = false
    var enabled = true

    /**
      * Profile
      */
    def %?[T](s: String)(b: => T): T = %?[T](s, null)(b)

    def %??[T1, T2](s: String)(f: T1 => T2): T1 => T2 = (v: T1) => %?(s)(f(v))

    def %?[T](s: String, rslt: T => String)(b: => T): T = {
      if (enabled) {
        val space = (0 until idx).map(_ => "  ").mkString

        if (lastOpen) println()
        print(space + s"Starting '$s'...")

        lastOpen = true
        idx = idx + 2

        val start = System.currentTimeMillis()
        val (ret, ex) = try {
          val ret = b
          (Some(ret), None)
        } catch {
          case t: Throwable => (None, Some(t))
        }
        val took = System.currentTimeMillis() - start

        idx = idx - 2

        val exception = ex match {
          case Some(t) => {
            s" !! {Exception: '${t.getMessage}'} @ " + Thread.currentThread().getStackTrace.mkString("\n", "\n", "\n")
          }
          case None => ""
        }

        val rsltStr = ret.flatMap(ret => Option(rslt).map(_ (ret))).map(" [" + _ + "]").getOrElse("")
        println(
          if (lastOpen) s" [${took}ms]$exception$rsltStr" else s"${space}Finished '$s' [${took}ms]$exception$rsltStr"
        )
        ex.foreach(_.printStackTrace())
        lastOpen = false

        (ret, ex) match {
          case (Some(ret), _) => ret
          case (_, Some(t)) => throw t
          case _ => ???
        }
      } else {
        b
      }

    }
  }

}