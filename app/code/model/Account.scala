package code.model

import java.util.{Properties, UUID}
import javax.activation.{DataHandler, FileDataSource}
import javax.mail._
import javax.mail.internet._

import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl

class Account(var accountName: String = "Account") extends Entity[Account] {

  def table = DBS.accounts

  def usrs: dsl.OneToMany[User] = DBS.account2Users.left(this)
}
