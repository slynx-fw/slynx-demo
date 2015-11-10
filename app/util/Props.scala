package util

import controllers.RootController.XSPageHandle
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.util.FatLazy


class PropOptV[T](dflt: Option[T], get: () => Option[T], set: Option[T] => Unit) {

  private var value = get().orElse(dflt)

  def apply(): Option[T] = value
  def update(v: Option[T]): Option[T] = {
    value = v
    set(v)
    value
  }

  def :=(v: Option[T]): Option[T] = update(v)
}

class PropV[T](dflt: T, get: () => Option[T], set: Option[T] => Unit) {

  private var value = get().getOrElse(dflt)

  def apply(): T = value
  def update(v: T): T = {
    value = v
    set(Some(v))
    value
  }

  def :=(v: T): T = update(v)

  class View[U](toT: U => T, toU: T => U) {

    def apply() = toU(value)
    def update(u: U): U = { set(Some(toT(u))); u }
    def :=(u: U): U = update(u)
  }

  def view[U](toT: U => T, toU: T => U) = new View[U](toT, toU)
}

trait Props {

  def get(key: String): Option[String]
  def set(key: String, value: Option[String]): Unit

  def set(key: String, value: String): Unit = set(key, Some(value))

  def CusOpt[T](key: String, dflt: Option[T], toT: String => Option[T], fromT: T => String) = new PropV[Option[T]](dflt, () => get(key).map(toT), v => set(key, v.flatten.map(fromT)))
  def Str(key: String, dflt: String) = new PropV[String](dflt, () => get(key), v => set(key, v))
  def Long(key: String, dflt: Long) = new PropV[Long](dflt, () => get(key).map(_.toLong), v => set(key, v.map(_ + "")))
  def Int(key: String, dflt: Int) = new PropV[Int](dflt, () => get(key).map(_.toInt), v => set(key, v.map(_ + "")))
  def Double(key: String, dflt: Double) = new PropV[Double](dflt, () => get(key).map(_.toDouble), v => set(key, v.map(_ + "")))
  def Bool(key: String, dflt: Boolean) = new PropV[Boolean](dflt, () => get(key).map(_.toBoolean), v => set(key, v.map(_ + "")))

  def /(prefix: String): Props = {
    val parent = this
    new Props {
      def get(key: String): Option[String] = parent.get(prefix + "." + key)
      def set(key: String, value: Option[String]): Unit = parent.set(prefix + "." + key, value)
    }
  }
}

object Props {

  lazy val InMemory = new Props {
    private val map = collection.mutable.Map[String, String]()
    override def set(key: String, value: Option[String]): Unit = {
      value match {
        case Some(v) => map(key) = v
        case None => map.remove(key)
      }
    }
    override def get(key: String): Option[String] = map.get(key)
  }

  def session()(implicit xsh: XSPageHandle) = new Props {
    override def get(key: String): Option[String] = xsh.s.props.get(key)
    override def set(key: String, value: Option[String]): Unit = {
      value match {
        case Some(v) => xsh.s.props(key) = v
        case None => xsh.s.props.remove(key)
      }
    }
  }

}