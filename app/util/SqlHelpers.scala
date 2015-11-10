package util

import java.sql.ResultSet

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Session


object SqlHelpers {

  def sqlQuery1[T](sql: String)(f: ResultSet => T): T = inTransaction {

    val conn = Session.currentSession.connection
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery(sql)
    if (!rs.next()) throw new Exception("No lines returned!!")
    f(rs)
  }

  def sqlUpdate[T](sql: String): Int = inTransaction {

    val conn = Session.currentSession.connection
    val stmt = conn.createStatement()
    stmt.executeUpdate(sql)
  }

  def sqlQueryOpt[T](sql: String)(f: ResultSet => T): Option[T] = inTransaction {

    val conn = Session.currentSession.connection
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery(sql)
    if (rs.next()) Some(f(rs)) else None
  }

  def sqlQueryN[T](sql: String)(f: ResultSet => T): List[T] = inTransaction {

    try {
      val conn = Session.currentSession.connection
      val stmt = conn.createStatement()
      val rs = stmt.executeQuery(sql)
      var rslt: List[T] = Nil
      while (rs.next()) rslt = f(rs) :: rslt
      rslt.reverse
    } catch {
      case e: Exception =>
        println("ERROR ON QUERY:\n" + sql)
        throw e
    }
  }

}
