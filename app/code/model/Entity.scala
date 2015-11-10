package code.model

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, Table}
import scala.xml.NodeSeq

trait BaseEntity[+T] extends KeyedEntity[Long] {

  var id: Long

  def dbTable: Table[_]

  def dbTableName: String

  def save(): T

  def update(f: T => Unit): T

  def delete(): Boolean

  def reload: T

  def reloadOpt: Option[T]

  def validate: List[NodeSeq] = Nil
}

trait Entity[T <: Entity[T] with KeyedEntity[Long]] extends BaseEntity[T] with KeyedEntity[Long] {
  self: T =>

  var id: Long = 0

  def dbTable: Table[_] = table
  def table: Table[T]

  def dbTableName: String = table.schema.tableNameFromClassName(this.getClass.getSimpleName.replaceAll("[^A-Za-z0-9]", ""))

  private def query[R](f: Table[T] => R): R = inTransaction(f(table))

  def save(): T = query(_.insertOrUpdate(this))

  def update(f: T => Unit): T = {
    val toSave =
      if (isPersisted) {
        val ins = reload
        f(ins)
        ins
      } else {
        this
      }
    f(this)
    toSave.save()
  }

  def delete(): Boolean = query(_.delete[Long](this.id))

  def reload: T = query(_.where(_.id === id).single)

  def reloadOpt: Option[T] = query(_.where(_.id === id).headOption)
}
