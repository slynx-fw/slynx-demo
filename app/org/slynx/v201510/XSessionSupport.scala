package org.slynx.v201510

import java.util.{TimerTask, Date, UUID, Timer}

import util.ImplicitHelpers._
import net.liftweb.common.Full
import net.liftweb.http.SHtml.ElemAttr._
import net.liftweb.http.SHtml.{ElemAttr, PairStringPromoter}
import net.liftweb.http.js.JE.JsRaw
import net.liftweb.http.js.JsCmds.{Alert, Replace, Run, SetHtml}
import net.liftweb.http.js.{JsCmd, JsCmds, JsExp}
import net.liftweb.http.{IdMemoizeTransform, NodeSeqFuncOrSeqNodeSeqFunc}
import net.liftweb.json.JsonAST.JValue
import net.liftweb.util.Helpers
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import scala.collection.immutable.ListMap
import scala.concurrent.Future
import scala.xml.{Elem, NodeSeq, Xhtml}

trait XSessionSupport extends Controller {

  type S
  def createState(): S

  def debug(s: String) = println(s)

  lazy val sessionsLock = new {}

  val sessions = collection.mutable.Map[String, XSession]()

  val timer: Timer = new Timer()

  timer.schedule(new TimerTask {
    override def run(): Unit = maintenance()
  }, CleanupMaintenanceFrequency)

  def maintenance(): Unit = {
    // Deletes expired sessions:
    sessionsLock.synchronized {
      sessions.filter(System.currentTimeMillis() > _._2.expires).foreach(xs => sessions.remove(xs._1))
    }
    // Deletes expired handles:
    sessionsLock.synchronized {
      sessions.values.foreach(session => session.synchronized {
        session.handles --= session.handles.filter(_.expires < System.currentTimeMillis())
      })
    }
    // Removes older sessions. Leaves MaxNumOfSessions active sessions.
    sessionsLock.synchronized {
      if (sessions.size > MaxNumOfSessions) {
        sessions --= sessions.toSeq.sortBy(-_._2.expires).drop(MaxNumOfSessions).map(_._1)
      }
    }
    // Enforce that each session can only have MaxPageHandlesPerSession handle
    sessionsLock.synchronized {
      sessions.values.foreach(session => session.synchronized {
        session.handles --= session.handles.sortBy(-_.expires).drop(MaxPageHandlesPerSession)
      })
    }
  }

  def sessionFor(req: Request[AnyContent]) = sessionsLock.synchronized {
    req.session.get("xsessionid").flatMap(id => sessions.get(id)).getOrElse({
      val xs = XSession(this, req)
      debug("CREATING session with id: " + xs.id)
      sessions(xs.id) = xs
      xs
    })
  }

  def CleanupMaintenanceFrequency = 60 * 1000L
  def PageHandleExpirationTimeMillis = 20 * 60 * 1000L
  def SessionExpirationTimeMillis = 20 * 60 * 1000L
  def MaxNumOfSessions = 50
  def MaxPageHandlesPerSession = 15

  def onSessionNotFound(req: Request[AnyContent]): JsCmd = Alert("Session expired.")
  def onFunctionNotFound(req: Request[AnyContent]): JsCmd = Run("Function not found.")

  def handleCall(xsId: String, header: RequestHeader): Action[AnyContent] = {
    import play.api.libs.concurrent.Execution.Implicits._
    Action.async(implicit req => Future(
      sessions.get(xsId).map(xs => xs.onresponse(xs.handle(req))(req)).getOrElse(Ok(onSessionNotFound(req).toJsCmd).as("application/javascript"))
    ))
  }

  def handleAdminArea(header: RequestHeader): Action[AnyContent] = Action(req => Results.NotFound)

  //  def handleWebSocket(xsId: String): Action[AnyContent] = {
  //    Action.async(implicit req => Future(
  //      sessions.get(xsId).map(xs => xs.onresponse(xs.handle(req))(req)).getOrElse(Ok(onSessionNotFound(req).toJsCmd).as("application/javascript"))
  //    ))
  //  }

  case class XSessionReq(
                          req: Request[AnyContent]
                        )

  //TODO: Store handles:
  case class XSession(
                       support: XSessionSupport,
                       initialReq: Request[AnyContent],
                       id: String = UUID.randomUUID().toString,
                       createdAt: Long = System.currentTimeMillis(),
                       var state: S = createState(),
                       var lastReq: Long = 0L
                     ) {

    var expires: Long = System.currentTimeMillis() + support.SessionExpirationTimeMillis

    def onresponse(result: Result)(implicit request: RequestHeader) = result.addingToSession("xsessionid" -> id)

    private[v201510] val functions = collection.mutable.Map[String, Either[Seq[String] => JsCmd, Seq[FilePart[play.api.libs.Files.TemporaryFile]] => Result]]()
    private[v201510] val handles = collection.mutable.ListBuffer[XSPageHandle]()

    def createHandle(req: Request[AnyContent]) = new XSPageHandle(this, req).$$(handles += _)

    def handle(req: Request[AnyContent]): Result = {
      lastReq = System.currentTimeMillis()
      expires = expires + support.SessionExpirationTimeMillis
      handles.map(handle => handle.expires = handle.expires + support.PageHandleExpirationTimeMillis)
      req.body.asFormUrlEncoded.map(params =>
        Ok(
          params.toList.map({
            case (fid, strings) => functions.get(fid).flatMap(_.left.toOption).map(_ (strings)).getOrElse(support.onFunctionNotFound(req))
          }).reduceOption[JsCmd](_ & _).getOrElse(JsCmds.Noop).toJsCmd
        ).as("application/javascript")
      ).orElse(
        req.body.asMultipartFormData.flatMap(files =>
          files.files.headOption.map(f => functions.get(f.key).flatMap(_.right.toOption).map(_ (files.files)).getOrElse(Ok(support.onFunctionNotFound(req).toJsCmd).as("application/javascript")))
        )
      ).getOrElse(Ok(JsCmds.Noop.toJsCmd).as("application/javascript"))
    }

    def resetSession(): Unit = sessionsLock.synchronized {
      functions.clear()
      state = createState()
      sessions -= id
    }

    def nFunctions: Long = synchronized(functions.size)
    def nPageHandles: Long = synchronized(handles.size)
  }

  class XSPageHandle(
                      val xs: XSession,
                      val req: Request[AnyContent],
                      val parent: Option[XSPageHandle] = None,
                      val children: collection.mutable.Map[String, XSPageHandle] = collection.mutable.Map[String, XSPageHandle](),
                      val uid: String = Helpers.nextFuncName
                    ) {

    def s: S = xs.state

    var expires: Long = System.currentTimeMillis() + xs.support.PageHandleExpirationTimeMillis

    def resetExpiration() = expires = System.currentTimeMillis() + xs.support.PageHandleExpirationTimeMillis

    private[v201510] val childFunctions = collection.mutable.Set[String]()

    def makeCall(data: JsExp, dataType: String): JsCmd = {
      Run {
        s"""$$.ajax({
           |  url : '/xscall/${xs.id}',
           |  type : 'POST',
           |  data : ${data.toJsCmd},
           |  dataType: '$dataType',
           |  success : function(data) {},
           |  error : function(request,error) {}
           |});
           |""".stripMargin
      }
    }

    def allChildFunctions(): Set[String] = childFunctions.toSet ++ children.values.toSet.flatMap((_: XSPageHandle).allChildFunctions())

    def deleteHandle(): Unit = synchronized {
      parent.foreach(parent => parent.synchronized {
        parent.children -= this.uid
      })
      xs.synchronized {
        xs.functions --= allChildFunctions()

        if (parent.isEmpty) {
          xs.handles -= this
        }
      }
    }

    def createSubHandle(childUid: String = Helpers.nextFuncName): XSPageHandle = synchronized {
      val child = new XSPageHandle(xs, req, Some(this), uid = childUid)
      children.get(childUid).foreach(_.deleteHandle())
      children += (childUid -> child)
      child
    }

    def ajaxInvoke(func: () => JsCmd): JsCmd = {
      val fid = Helpers.nextFuncName
      childFunctions += fid
      xs.functions(fid) = Left((arg: Seq[String]) => {resetExpiration(); func()})
      makeCall(JsRaw(s"{$fid: ''}"), "script")
    }

    def ajaxFileInputName(func: Seq[FilePart[play.api.libs.Files.TemporaryFile]] => Result): String = {
      val fid = Helpers.nextFuncName
      childFunctions += fid
      xs.functions(fid) = Right((file: Seq[FilePart[play.api.libs.Files.TemporaryFile]]) => {resetExpiration(); func(file)})
      fid
    }

    def ajaxCall(args: JsExp, func: String => JsCmd): JsCmd = {
      val fid = Helpers.nextFuncName
      childFunctions += fid
      xs.functions(fid) = Left((arg: Seq[String]) => {resetExpiration(); func(arg.headOption.getOrElse(""))})
      makeCall(JsRaw(s"{$fid: (${args.toJsCmd} || '')}"), "script")
    }

    def jsonCall(args: JsExp, func: JValue => JsCmd): JsCmd = {
      import net.liftweb.json._
      implicit val formats = DefaultFormats
      ajaxCall(JsRaw("JSON.stringify(" + args.toJsCmd + ")"), str => func(net.liftweb.json.parse(str)))
    }

    def onEvent(func: String => JsCmd): JsCmd = ajaxCall(JsRaw("this.value"), func)

    def ajaxTextarea(value: String, func: String => JsCmd, attrs: ElemAttr*): Elem = {
      (attrs.foldLeft(<textarea>
        {value}
      </textarea>)(_ % _)) %
        pairToBasic("onblur" -> ajaxCall(JsRaw("this.value"), func).toJsCmd)
    }

    def ajaxText(value: String, func: String => JsCmd, attrs: ElemAttr*): Elem = {
      (attrs.foldLeft(<input type="text" value={value}/>)(_ % _)) %
        pairToBasic("onblur" -> ajaxCall(JsRaw("this.value"), func).toJsCmd)
    }

    def ajaxSelectElem[T](options: Seq[T], default: Option[T], attrs: ElemAttr*)(onSubmit: T => JsCmd)(implicit f: PairStringPromoter[T]): Elem = {

      val random: Seq[((T, String), String)] = options.map(v => v -> f(v)).zip((0 until options.size).map(_ => net.liftweb.util.Helpers.nextFuncName))

      val onchange = ajaxCall(
        JsRaw(s"this.options[this.selectedIndex].value"),
        s => random.find(_._2 == s).map(v => onSubmit(v._1._1)).getOrElse(JsCmds.Noop)
      )

      (attrs.foldLeft(
        <select onchange={onchange.toJsCmd}>
          {random.map({
          case ((value, label), randomId) => <option selected={if (default.contains(value)) "selected" else null} value={randomId}>
            {label}
          </option>
        })}
        </select>
      )(_ % _))
    }

    def idMemoize(f: (XSPageHandle, IdMemoizeTransform) => NodeSeqFuncOrSeqNodeSeqFunc): IdMemoizeTransform = {
      new IdMemoizeTransform {
        var latestElem: Elem = <span/>

        var latestKids: NodeSeq = NodeSeq.Empty

        var curHandle: Option[XSPageHandle] = None

        var latestId = Helpers.nextFuncName

        private def fixElem(e: Elem): Elem = {
          import net.liftweb.util.Helpers._

          e.attribute("id") match {
            case Some(id) => latestId = id.text; e
            case None => e % ("id" -> latestId)
          }
        }

        def apply(ns: NodeSeq): NodeSeq =
          Helpers.findBox(ns)(e => {
            latestElem = fixElem(e)
            latestKids = e.child
            Full(e)
          }).map(ignore => {
            applyAgain()
          }).openOr(NodeSeq.Empty)

        def applyAgain(): NodeSeq = {
          curHandle.foreach(_.deleteHandle())
          curHandle = Some(createSubHandle())
          new Elem(latestElem.prefix,
            latestElem.label,
            latestElem.attributes,
            latestElem.scope,
            f(curHandle.get, this)(latestKids): _*)
        }

        def setHtml(): JsCmd = {
          curHandle.foreach(_.deleteHandle())
          curHandle = Some(createSubHandle())
          SetHtml(latestId, f(curHandle.get, this)(latestKids))
        }
      }
    }

    def memoizeElem(f: IdMemoizeTransform => NodeSeqFuncOrSeqNodeSeqFunc): IdMemoizeTransform = new IdMemoizeTransform {

      import net.liftweb.util.Helpers._

      var _latestId = Helpers.nextFuncName + "dflt"
      def latestId = _latestId
      var latestElem: Elem = <span id={latestId}></span>
      def latestKids: NodeSeq = latestElem.child

      def apply(ns: NodeSeq): NodeSeq =
        Helpers.findBox(f(this)(ns))(e => {
          e.attribute("id") match {
            case Some(id) =>
              _latestId = id.text
              latestElem = e
              Full(e)
            case None =>
              latestElem = e % ("id" -> latestId)
              Full(e)
          }
        }).openOr(latestElem)

      def applyAgain(): NodeSeq = {
        Helpers
          .findBox[NodeSeq](f(this)(latestElem))(e => Full((e % ("id" -> latestId)): NodeSeq))
          .openOr(<span id={latestId}></span>)
      }

      def setHtml(): JsCmd = Replace(latestId, f(this)(latestElem))
    }

  }

}
