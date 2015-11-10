package pages.theme

import java.util.UUID

import net.liftweb.builtin.snippet.Tail
import net.liftweb.common.Full
import net.liftweb.http.SHtml.ElemAttr
import net.liftweb.http.js.JE.{ValById, JsRaw}
import net.liftweb.http.js.jquery.JqJsCmds.{Hide, Show}
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.{SHtml}
import net.liftweb.json.JsonAST.{JNull, JNothing, JString, JArray}
import net.liftweb.util.Helpers
import net.liftweb.util.Helpers._
import controllers.RootController.XSPageHandle

import scala.xml.NodeSeq

trait SForms extends SBtns {

  abstract class DefaultForm()(implicit val xsh: XSPageHandle) extends Form

  trait Form extends Id {

    implicit val xsh: XSPageHandle

    val field: Field

    var saveFailed = false

    def onChangeClientSide(): JsCmd = JsCmds.Noop

    def onChangeServerSide(f: Field): JsCmd = field.update() & field.onChangedField(f)

    def afterSucessfullSave(): JsCmd = JsCmds.Noop

    def onSave(): JsCmd = {
      val errors = field.errors()
      if (errors.isEmpty) {
        saveFailed = false
        field.doSave() & field.update() & afterSucessfullSave()
      } else {
        saveFailed = true
        field.update()
      }
    }

    def rendered: NodeSeq = {
      <form method="POST" action="javascript:void(0);" role="form">
        {field.rendered}
      </form>
  }

    trait Field {

      val id = Helpers.nextFuncName

      def update(): JsCmd

      def children: Seq[Field] = Nil

      def onChangedField(f: Field): JsCmd = if (deps.contains(f)) Replace(id, rendered) else {children.map(_.onChangedField(f)).reduceOption(_ & _).getOrElse(JsCmds.Noop)}

      def modified: Boolean

      def doSave(): JsCmd

      def errors(): Seq[(Field, String)]

      def rendered: NodeSeq

      def enabled: () => Boolean

      def deps: Seq[Field]
    }

    trait VerticalGroupBase extends Field {

      def fields: Seq[Field]

      override def children: Seq[Field] = fields

      def modified: Boolean = fields.exists(_.modified)

      def errors(): Seq[(Field, String)] = fields.flatMap(_.errors())

      def doSave(): JsCmd = fields.map(_.doSave()).foldLeft[JsCmd](JsCmds.Noop)(_ & _)

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & fields.map(_.update()).reduceOption(_ & _).getOrElse(JsCmds.Noop)

      def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id}>{fields.map(_.rendered).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)}</div>
      }
    }

    case class VerticalGroup(enabled: () => Boolean = () => true, deps: Seq[Field] = Nil)(val fields: Field*) extends VerticalGroupBase

    trait HorizontalGroupBase extends Field {

      def fields: Seq[Field]

      override def children: Seq[Field] = fields

      def modified: Boolean = fields.exists(_.modified)

      def errors(): Seq[(Field, String)] = fields.flatMap(_.errors())

      def doSave(): JsCmd = fields.map(_.doSave()).foldLeft[JsCmd](JsCmds.Noop)(_ & _)

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & fields.map(_.update()).reduceOption(_ & _).getOrElse(JsCmds.Noop)

      def size = "md"

      def rendered: NodeSeq = {
        val width = fields.filter(_.enabled()).size match {
          case 0 => "12"
          case 1 => "12"
          case 2 => "6"
          case 3 => "4"
          case 4 => "3"
        }
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="row">
          {fields.map(f => <div class={s"col-$size-$width"}>{f.rendered}</div>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)}
        </div>
      }
    }

    case class HorizontalGroup(
                                enabled: () => Boolean = () => true,
                                deps: Seq[Field] = Nil,
                                override val size: String = "md"
                                )(val fields: Field*) extends HorizontalGroupBase


    trait RadioFieldBase[T] extends Field {

      var modified = false
      def labelText: String
      def labelFor: T => String
      def allOptions: Seq[T]
      def save: T => JsCmd
      var value = get()
      def get: () => T
      def errors(): Seq[(Field, String)] = Nil

      def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

      override def doSave(): JsCmd = { modified = false; save(value) }

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

      override def rendered: NodeSeq = {
        val renderedOptions = allOptions
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label>{labelText}:</label>
          {
          renderedOptions.zipWithIndex.map(tuple => {
            <div class="radio">
              <label>
                <input type="radio" name={id + "input"} value={tuple._2.toString} onchange="" checked={if (tuple._1 == value) "checked" else null }/> {labelFor(tuple._1)}
              </label>
            </div>
          }).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)
          }
          <span id={id + "errors"}>{errorsNs}</span>
        </div> ++
          Tail.render(Script(OnLoad(Run(
            s"""
               |$$('input[type=radio][name=${id + "input"}]')
               |  .on('change', function() {
               |    ${xsh.ajaxCall(JsRaw("$(this).val()"), v => {value = renderedOptions(v.toInt); modified = true; onChangeServerSide(this)}).toJsCmd}
               |  });
               |""".stripMargin
          ))))
      }

    }

    case class RadioField[T](
                              labelText: String,
                              allOptions: Seq[T],
                              get: () => T,
                              save: T => JsCmd,
                              labelFor: T => String = (_: T).toString,
                              enabled: () => Boolean = () => true,
                              req: Boolean = false,
                              deps: Seq[Field] = Nil
                              ) extends RadioFieldBase[T]

    trait MultiCheckboxFieldBase[T] extends Field {

      var modified = false
      def labelText: String
      def labelFor: T => String
      def allOptions: Seq[T]
      def save: Set[T] => JsCmd
      var value = get()
      def get: () => Set[T]
      def errors(): Seq[(Field, String)] = Nil


      def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

      override def doSave(): JsCmd = { modified = false; save(value) }

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

      override def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>
          {
          allOptions.map(option => {
            val idcheckbox = Helpers.nextFuncName
            <div class="checkbox">
              <label>
                <input type="checkbox" id={idcheckbox} value={Helpers.nextFuncName} onchange={xsh.ajaxCall(JsRaw(s"$$('#${idcheckbox}').is(':checked')"), v => {
                if (v == "true") {value = value + option} else {value = value - option}
                modified = true;
                onChangeServerSide(this)
              }).toJsCmd}/> {labelFor(option)}
              </label>
            </div>
          }).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)
          }
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }

    }

    case class MultiCheckboxField[T](
                                      labelText: String,
                                      allOptions: Seq[T],
                                      get: () => Set[T],
                                      save: Set[T] => JsCmd,
                                      labelFor: T => String = (_: T).toString,
                                      enabled: () => Boolean = () => true,
                                      req: Boolean = false,
                                      deps: Seq[Field] = Nil
                                      ) extends MultiCheckboxFieldBase[T]


    trait GenTextFieldBase[T] extends Field {

      def toStr(v: T): String
      def fromStr(s: String): Option[T]

      var modified = false

      def placeholderText: String

      def labelText: String

      def inputName: Option[String]

      def get: () => T

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

      var value = get()

      def errors(): Seq[(Field, String)] = Nil

      def save: T => JsCmd

      override def doSave(): JsCmd = { modified = false; save(value) }

      def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

      def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>
          <input name={inputName.orNull} placeholder={placeholderText} value={toStr(value)} onchange={(onChangeClientSide() & xsh.ajaxCall(ValById(id + "input"), (v: String) => {fromStr(v).map(v => {value = v; modified = true; onChangeServerSide(this)}).getOrElse(JsCmds.Noop)})).toJsCmd} id={id + "input"} class="form-control" type="text"/>
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }
    }


    case class TextField(labelText: String, get: () => String, save: String => JsCmd, placeholderText: String = "", enabled: () => Boolean = () => true, inputName: Option[String] = None, req: Boolean = false, deps: Seq[Field] = Nil) extends GenTextFieldBase[String] {

      def toStr(v: String): String = v
      def fromStr(s: String): Option[String] = Some(s)

      override def errors(): Seq[(Field, String)] = if (req && value == "") List(this -> "Required") else Nil
    }

    case class DoubleField(
                            labelText: String,
                            get: () => Double,
                            save: Double => JsCmd,
                            placeholderText: String = "",
                            enabled: () => Boolean = () => true,
                            inputName: Option[String] = None,
                            req: Boolean = false,
                            fmt: String = "%.2f",
                            deps: Seq[Field] = Nil) extends GenTextFieldBase[Double] {

      def toStr(v: Double): String = v.formatted(fmt)
      def fromStr(s: String): Option[Double] = scala.util.Try(s.toDouble).toOption
    }

    case class IntField(
                         labelText: String,
                         get: () => Int,
                         save: Int => JsCmd,
                         placeholderText: String = "",
                         enabled: () => Boolean = () => true,
                         inputName: Option[String] = None,
                         req: Boolean = false,
                         deps: Seq[Field] = Nil) extends GenTextFieldBase[Int] {

      def toStr(v: Int): String = v.toString
      def fromStr(s: String): Option[Int] = scala.util.Try(s.toInt).toOption
    }

    case class LongField(
                          labelText: String,
                          get: () => Long,
                          save: Long => JsCmd,
                          placeholderText: String = "",
                          enabled: () => Boolean = () => true,
                          inputName: Option[String] = None,
                          req: Boolean = false,
                          deps: Seq[Field] = Nil) extends GenTextFieldBase[Long] {

      def toStr(v: Long): String = v.toString
      def fromStr(s: String): Option[Long] = scala.util.Try(s.toLong).toOption
    }

    case class EmptyField() extends Field {
      override def doSave(): JsCmd = JsCmds.Noop
      override def enabled: () => Boolean = () => true
      override def rendered: NodeSeq = <span id={id}></span>
      override def errors(): Seq[(Field, String)] = Nil
      override def update(): JsCmd = JsCmds.Noop
      override def modified: Boolean = false
      override def deps: Seq[Field] = Nil
    }

    trait SelectFieldBase[T] extends Field {

      var modified = false

      def labelText: String

      def labelFor: T => String

      def inputName: Option[String]

      def allOptions: Seq[T]

      def get: () => T

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

      def errors(): Seq[(Field, String)] = Nil

      def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

      var value: T = get()

      def save: T => JsCmd

      override def doSave(): JsCmd = save(value)

      def attrs: Seq[ElemAttr] = Seq("class" -> "form-control", "id" -> (id + "input"), "style" -> "color: rgb(33, 37, 43);") ++ inputName.map("name" -> _).toSeq

      def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>
          {
          xsh.ajaxSelectElem[T](allOptions, Full(value), attrs: _*)(v => {
            value = v
            modified = true
            onChangeClientSide() & onChangeServerSide(this)
          })((v: T) => labelFor(v))
          }
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }
    }

    case class SelectField[T](
                               labelText: String,
                               allOptions: Seq[T],
                               get: () => T,
                               save: T => JsCmd,
                               labelFor: T => String = (_: T).toString,
                               enabled: () => Boolean = () => true,
                               inputName: Option[String] = None,
                               deps: Seq[Field] = Nil
                               ) extends SelectFieldBase[T]


    trait MultiSelectFieldBase[T] extends Field {

      var modified = false

      def labelText: String

      def labelFor: T => String

      def inputName: Option[String]

      def allOptions: Seq[T]

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

      def errors(): Seq[(Field, String)] = Nil

      def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

      def save: Set[T] => JsCmd

      var value = get()

      def get: () => Set[T]

      override def doSave(): JsCmd = save(value)

      def attrs: Seq[ElemAttr] = Seq("class" -> "form-control", "multiple" -> "multiple", "id" -> (id + "input"), "style" -> "color: rgb(33, 37, 43);") ++ inputName.map("name" -> _).toSeq

      def rendered: NodeSeq = {
        val randomId = Helpers.nextFuncName
        val renderedOptions = allOptions
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>

            <select class="form-control" id={id + "input"} multiple="multiple" style="color: rgb(33, 37, 43);" onchange={xsh.jsonCall(JsRaw(s"$$('#${id + "input"}').val()"), v => v match {
              case JArray(fields: List[JString]) =>
                value = fields.map(v => renderedOptions(v.s.toInt)).toSet
                modified = true
                onChangeServerSide(this)
              case JNothing | JNull =>
                value = Set()
                modified = true
                onChangeServerSide(this)
            }).toJsCmd}>
           {renderedOptions.zipWithIndex.map(tuple => <option value={tuple._2.toString} selected={if (renderedOptions.contains(tuple._1)) "selected" else null}>{labelFor(tuple._1)}</option>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)}
              </select>
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }

    }

    case class MultiSelectField[T](
                                    labelText: String,
                                    allOptions: Seq[T],
                                    get: () => Set[T],
                                    save: Set[T] => JsCmd,
                                    labelFor: T => String = (_: T).toString,
                                    enabled: () => Boolean = () => true,
                                    inputName: Option[String] = None,
                                    deps: Seq[Field] = Nil
                                    ) extends MultiSelectFieldBase[T]


    trait SelectOptFieldBase[T] extends SelectFieldBase[Option[T]] {
      def noneLabel: String
      def someLabel: T => String
      def labelFor: Option[T] => String = _.map(someLabel).getOrElse(noneLabel)

      def allOptOptions: Seq[T]
      def allOptions: Seq[Option[T]] = None +: allOptOptions.map(Some(_))

      def req: Boolean
      override def errors(): Seq[(Field, String)] = super.errors() ++ (if (value.isEmpty) List(this -> "Required") else Nil)
    }

    case class SelectOptField[T](
                                  labelText: String,
                                  allOptOptions: Seq[T],
                                  get: () => Option[T],
                                  save: Option[T] => JsCmd,
                                  noneLabel: String,
                                  someLabel: T => String = (_: T).toString,
                                  enabled: () => Boolean = () => true,
                                  inputName: Option[String] = None,
                                  req: Boolean = false,
                                  deps: Seq[Field] = Nil
                                  ) extends SelectOptFieldBase[T]


    trait DatePickerFieldBase extends Field {

      var modified = false

      def get: () => Long

      def save: Long => JsCmd

      var value = get()

      var datePickerId = UUID.randomUUID().toString

      def labelText: String

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

      def errors(): Seq[(Field, String)] = Nil

      def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

      override def doSave(): JsCmd = save(value)


      override def rendered: NodeSeq = {
        <label id={id}>
          {labelText}
        </label> <br/>
                <div class="form-group">
                  <div class="input-group date" id={datePickerId}>
                    <input type='text'  class="form-control" />
                    <span class="input-group-addon">
                      <span class="glyphicon glyphicon-calendar"></span></span>
                  </div>
                  <span id={id + "errors"}>{errorsNs}</span>
                </div>
          <tail>
          {Script(OnLoad(Run(
            s"""
             |$$('#${datePickerId}')
             |.datetimepicker({
             |  locale: 'en-gb',
             |  defaultDate: new Date($value)
             |})
             |.on('dp.change', function(evt) {
             |  ${xsh.ajaxCall(JsRaw(s"evt.date.valueOf()"), v => {value = v.toLong; onChangeServerSide(this)}).toJsCmd}
             |});
             |""".stripMargin
          )))}
        </tail>
      }
    }

    case class DateTimePickerField(
                                    labelText: String,
                                    get: () => Long,
                                    save: Long => JsCmd,
                                    enabled: () => Boolean = () => true,
                                    req: Boolean = false,
                                    deps: Seq[Field] = Nil
                                    ) extends DatePickerFieldBase

    trait SubmitButtonBase extends Field {

      val modified = false

      def labelText: String

      def btnClass: () => String

      def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & JsCmds.Run(s"""$$('#$id button').attr('class', ${btnClass().encJs});""")

      override def errors(): Seq[(Field, String)] = Nil

      override def doSave(): JsCmd = JsCmds.Noop

      override def rendered: NodeSeq =
        <div id={id}>
          <div class="clearfix"><button style={(if (!enabled()) "display:none;" else "")} onclick={xsh.ajaxInvoke(() => onSave()).toJsCmd} class={btnClass()}>{labelText}</button></div>
        </div>
    }

    case class SubmitButton(labelText: String = "Save", btnClass: () => String = () => TH.Btn().Primary.right.clas, enabled: () => Boolean = () => true, deps: Seq[Field] = Nil) extends SubmitButtonBase

  }

}