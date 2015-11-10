package util

import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds.{Script, _}
import net.liftweb.http.js.{JsCmd, JsExp}
import net.liftweb.util.Helpers

trait Emmiter {

  lazy val emmiterId = Helpers.nextFuncName

  protected def onSetUp(): JsCmd = Noop

  def setUp(): JsCmd = Run( s"""if(!window.cb$emmiterId) {window.cb$emmiterId = $$.Callbacks(); ${onSetUp().toJsCmd}} window.cb$emmiterId.fire(${value().toJsCmd});""".stripMargin)

  def fire(): JsCmd = setUp()

  def value(): JsExp

  def setUpNs() = <tail>{Script(setUp())}</tail>
}

trait EmmiterStateful extends Emmiter {

  def initialValue(): JsExp

  override protected def onSetUp(): JsCmd = super.onSetUp() & Run( s"""window.val$emmiterId = ${initialValue().toJsCmd};""")

  def setValue(jsExp: JsExp): JsCmd = Run( s"""window.val$emmiterId = ${jsExp.toJsCmd}; ${fire().toJsCmd}""")

  override def value(): JsExp = JsRaw( s"""window.val$emmiterId""")
}

trait Listener {

  def update(value: JsExp)

}

object Listener {

  def span(emmiter: Emmiter) = {
    val id = Helpers.nextFuncName
    <span id={id}></span> ++
      <tail>{Script(Run(
        s"""${emmiter.setUp().toJsCmd};
           |$$('#$id').text(${emmiter.value().toJsCmd});
           |window.cb${emmiter.emmiterId}.add(function(value) {$$('#$id').text(value)});
           |""".stripMargin))}</tail>
  }

  def pipeTo(src: EmmiterStateful, dest: EmmiterStateful) = {
    <tail>{Script(Run {
      s"""${src.setUp().toJsCmd};
         |${dest.setUp().toJsCmd};
         |${dest.setValue(src.value()).toJsCmd};
         |window.cb${src.emmiterId}.add(function(value) {${dest.setValue(JsRaw("value")).toJsCmd}});
         |""".stripMargin
    })}</tail>
  }
}


