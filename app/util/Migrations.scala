package util

import code.model.Sys
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Session

object Migrations {

  def run() = {

    def sysVersion: Long = inTransaction {
      val conn = Session.currentSession.connection
      val stmt = conn.createStatement()
      val rs = stmt.executeQuery("select db_version from sys")
      if (rs.next()) {
        rs.getLong(1)
      } else {
        new Sys().save().id
      }
    }

    def sysVersion_=(v: Long): Unit = inTransaction {
      val conn = Session.currentSession.connection
      val stmt = conn.createStatement()
      stmt.execute(s"update sys set db_version = $v;")
    }

    while (migrate.isDefinedAt(sysVersion + 1)) {
      val to = sysVersion + 1

      println(s"MIGRATING FROM ${to - 1} to $to")
      try {
        inTransaction {
          migrate(to)
          sysVersion_=(to)
        }
        println(s"MIGRATED FROM ${to - 1} to $to")
      } catch {
        case e: Throwable =>
          println("MIGRATION FAILED")
          System.exit(1)
      }
    }
  }

  import SqlHelpers._

  private def migrate: PartialFunction[Long, Unit] = {
    case 1 =>
  }
}
