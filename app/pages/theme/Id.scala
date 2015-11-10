package pages.theme

import net.liftweb.http.S

trait Id {

  private val _id = S.formFuncName
  def id(part: Symbol) = _id + "_" + part.name

  def sel(id: String): String = s"$$('#$id')"

  def sel(id: String, rest: String): String = sel(id) + rest

}
