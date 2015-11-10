package pages.theme

import net.liftweb.http.js.JsCmds._
import net.liftweb.util.Helpers

trait BWAUtils {

  def timeFrom(ts: Long) = {
    val id = Helpers.nextFuncName
    <span id={id}></span> ++ Script(Run(s"$$('#$id').text(moment.unix(${ts/1000}).fromNow());"))
  }
}
