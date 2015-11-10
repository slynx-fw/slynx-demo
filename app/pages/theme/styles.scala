package pages.theme

object Styles {

  type Style = String

  val bootstrap_min: Style = "/static/bootstrap/css/bootstrap.min.css"
  val font_awesome_min: Style = "//maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"
  val ionicons_min: Style = "//code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"
  val adminLTE_min: Style = "/static/css/AdminLTE.min.css"
  val all_skins: Style = "/static/css/skins/_all-skins.min.css"
  val shCore: Style = "/static/css/shCore.css"
  val shThemeDefault: Style = "/static/css/shThemeDefault.css"
  val toastr: Style = "/static/css/toastr.min.css"
  val data_tables_bootstrap: Style = "/static/plugins/datatables/dataTables.bootstrap.css"

  val datepicker: Style = "/static/css/bootstrap-datetimepicker.min.css"


  //CKEDITOR
  val editor: Style = "/static/plugins/ckeditor/skins/moono/editor.css"
  val content: Style = "/static/plugins/ckeditor/contents.js"

  val custom: Style = "/static/css/custom.css"

  val bootstrap_wysihtml5: Style = "/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css"

  lazy val indexOf: Map[Style, Int] = {
    this.getClass.getDeclaredFields
      .filter(_.getType == classOf[String])
      .zipWithIndex.map(f => {f._1.setAccessible(true); f._1.get(this).asInstanceOf[Style] -> f._2}).toMap
  }
}
