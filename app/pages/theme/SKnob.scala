package pages.theme

import controllers.RootController.XSPageHandle
import net.liftweb.http.js.JE.{AnonFunc, JsRaw}
import net.liftweb.http.js.JsCmds.{OnLoad, Run, Script}
import net.liftweb.http.js.{JsCmd, JsCmds}
import net.liftweb.util.Helpers
import net.liftweb.util.Helpers._

import scala.xml.NodeSeq

trait SKnob {

  case class Knob(
                   _lblOpt: Option[String],
                   _min: Integer,
                   _max: Integer,
                   _step: Integer,
                   _angleOffset: Integer,
                   _angleArc: Integer,
                   _stopper: Boolean,
                   _readOnly: Boolean,
                   _clockwise: Boolean,
                   _tickness: Double,
                   _lineCapButt: Boolean,
                   _width: Integer,
                   _height: Integer,
                   _displayInput: Boolean,
                   _displayPrevious: Boolean,
                   _fgColor: String,
                   _inputColor: String,
                   _bgColor: String,
                   _initialValue: Integer,
                   // Events:
                   _onRelease: AnonFunc,
                   _onReleaseAjax: Option[(Int) => JsCmd],
                   _onChange: AnonFunc,
                   _onChangeAjax: Option[(Int) => JsCmd],
                   _onDraw: JsCmd,
                   _onCancel: AnonFunc,
                   _onCancelAjax: Option[(Int) => JsCmd],
                   _formatFunction: AnonFunc
                   )(implicit xsh: XSPageHandle) {

    def lbl(lbl: String): Knob = copy(_lblOpt = Some(lbl))

    def min(min_value: Integer): Knob = copy(_min = min_value)
    def max(max_value: Integer): Knob = copy(_max = max_value)
    def step(step_value: Integer): Knob = copy(_step = step_value)
    def angleOffset(angleOffset_value: Integer): Knob = copy(_angleOffset = angleOffset_value)
    def angleArc(angleArc_value: Integer): Knob = copy(_angleArc = angleArc_value)
    def stopper(stopper_value: Boolean): Knob = copy(_stopper = stopper_value)
    def readOnly(readOnly_value: Boolean): Knob = copy(_readOnly = readOnly_value)
    def clockwise(clockwise_value: Boolean): Knob = copy(_clockwise = clockwise_value)

    def thickness(tickness_value: Double): Knob = copy(_tickness = tickness_value)
    def lineCapButt(lineCapButt_value: Boolean): Knob = copy(_lineCapButt = lineCapButt_value)
    def width(width_value: Integer): Knob = copy(_width = width_value)
    def height(height_value: Integer): Knob = copy(_height = height_value)
    def displayInput(displayInput_value: Boolean): Knob = copy(_displayInput = displayInput_value)
    def displayPrevious(displayPrevious_value: Boolean): Knob = copy(_displayPrevious = displayPrevious_value)
    def fgColor(fgColor_value: String): Knob = copy(_fgColor = fgColor_value)
    def inputColor(inputColor_value: String): Knob = copy(_inputColor = inputColor_value)
    def bgColor(bgColor_value: String): Knob = copy(_bgColor = bgColor_value)

    def initialValue(initial_value: Integer) = copy(_initialValue = initial_value)

    def onReleaseClientSide(f: AnonFunc) = copy(_onRelease = f)
    def onReleaseAjax(f: (Int) => JsCmd): Knob = copy(_onReleaseAjax = Some(f))

    def onChangeClientSide(f: AnonFunc) = copy(_onChange = f)
    def onChangeAjax(f: (Int) => JsCmd): Knob = copy(_onChangeAjax = Some(f))

    def onDrawClientSide(f: JsCmd) = copy(_onDraw = f)

    def onCancelClientSide(f: AnonFunc) = copy(_onCancel = f)
    def onCancelAjax(f: (Int) => JsCmd): Knob = copy(_onCancelAjax = Some(f))

    def formatFunction(f: AnonFunc) = copy(_onCancel = f)

    lazy val knobId = Helpers.nextFuncName

    def rendered: NodeSeq = {
      <input type="text" id={knobId} class="knob" value={_initialValue.toString}></input>++
        _lblOpt.map(lbl => <div class="knob-label">{lbl}</div>).getOrElse(NodeSeq.Empty) ++
      <tail>{Script(OnLoad(Run(
        s"""$$("#$knobId").knob({
           |  'min':${_min},
           |  'max':${_max},
           |  'step':${_step},
           |  'angleOffset':${_angleOffset},
           |  'angleArc':${_angleArc},
           |  'stopper':${_stopper},
           |  'readOnly':${_readOnly},
           |  'rotation':"${if (_clockwise) "clockwise" else "anticlockwise"}",
           |  'thickness':${_tickness},
           |  'lineCapButt':"${if (_lineCapButt) "butt" else "round"}",
           |  'width':${_width},
           |  'height':${_height},
           |  'displayInput':${_displayInput},
           |  'displayPrevious':${_displayPrevious},
           |  'fgColor':${_fgColor.encJs},
           |  'inputColor':${_inputColor.encJs},
           |  'bgColor':${_bgColor.encJs},
           |
           |  'release': function(v) { (${_onRelease.toJsCmd})(v); ${_onReleaseAjax.map(_onReleaseAjax => xsh.ajaxCall(JsRaw("v"), str => _onReleaseAjax(str.toInt)).toJsCmd).getOrElse("")}},
           |  'change': function(v) { (${_onChange.toJsCmd})(v); ${_onChangeAjax.map(_onChangeAjax => xsh.ajaxCall(JsRaw("v"), str => _onChangeAjax(str.toInt)).toJsCmd).getOrElse("")}},
           |  'draw': function() { ${_onDraw.toJsCmd} },
           |  'cancel': function() { (${_onCancel.toJsCmd})(v); ${_onCancelAjax.map(_onCancelAjax => xsh.ajaxCall(JsRaw("v"), str => _onCancelAjax(str.toInt)).toJsCmd).getOrElse("")}},
           |  'format': ${_formatFunction.toJsCmd}
           |});
           """.stripMargin
      )))}</tail>
    }
  }

  object Knob {
    def apply()(implicit xsh: XSPageHandle) = new Knob(
      _min = 0,
      _max = 100,
      _step = 1,
      _angleOffset = 0,
      _angleArc = 360,
      _stopper = false,
      _readOnly = false,
      _clockwise = true,
      _tickness = 0.33,
      _lineCapButt = true,
      _width = 90,
      _height = 90,
      _displayInput = true,
      _displayPrevious = false,
      _fgColor = "#3c8dbc",
      _inputColor = "black",
      _bgColor = "#E6E6FA",
      _initialValue = 0,
      _lblOpt = None,
      _onRelease = AnonFunc(),
      _onReleaseAjax = None,
      _onChange = AnonFunc(),
      _onChangeAjax = None,
      _onDraw = JsCmds.Noop,
      _onCancel = AnonFunc(),
      _onCancelAjax = None,
      _formatFunction = AnonFunc("v", Run("return v;"))
    )
  }

}

