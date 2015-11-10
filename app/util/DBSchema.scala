package util

import java.io.{FileWriter, PrintWriter}

import code.model.DBS
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{Session, SessionFactory}

object DBSchema {

  def main(args: Array[String]) {

    SessionFactory.concreteFactory = Some(() => {
      Session.create(DBS.conn, new org.squeryl.adapters.PostgreSqlAdapter())
    })

    transaction {
      println("DUMPING SCHEMA...")
      val bw = new PrintWriter(new FileWriter("schema.sql"))
      DBS.printDdl(bw)
      bw.close()
    }
  }
}
