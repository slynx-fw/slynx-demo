package code.model

import org.squeryl.PrimitiveTypeMode._

class Sys(
           var dbVersion: Long = 0
           ) extends Entity[Sys] {

  import net.liftweb.json._

  implicit val d = DefaultFormats

  def table = DBS.sys
}

object Sys {
  def apply() = inTransaction(DBS.sys.single)
}