package org.slynx.v201510.pages.theme

object Libs {

  type Lib = String

  val jquery_min: Lib = "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
  val bootstrap_min: Lib = "/static/bootstrap/js/bootstrap.min.js"

  val jquery_ui: Lib ="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"
  val jquery_knob: Lib ="/static/plugins/knob/jquery.knob.js"

  val jquery_slimscroll_min: Lib = "/static/plugins/slimScroll/jquery.slimscroll.min.js"
  val fastclick_min: Lib = "/static/plugins/fastclick/fastclick.min.js"

  val sh_core: Lib = "/static/js/shCore.js"
  val sh_brush: Lib = "/static/js/shBrushScala.js"

  val toastr: Lib = "/static/js/toastr.min.js"

  val jquery_data_tables: Lib = "/static/plugins/datatables/jquery.dataTables.min.js"
  val data_tables_bootstrap: Lib = "/static/plugins/datatables/dataTables.bootstrap.min.js"
  val jquery_floatThead: Lib = "/static/plugins/mkoryak-floatThead/jquery.floatThead.min.js"

  val jquery_flot: Lib = "/static/plugins/flot/jquery.flot.min.js"
  val jquery_flot_time: Lib ="/static/plugins/flot/jquery.flot.time.js"
  val jquery_flot_resize: Lib = "/static/plugins/flot/jquery.flot.resize.min.js"
  val jquery_flot_pie: Lib = "/static/plugins/flot/jquery.flot.pie.js"
  val jquery_flot_categories: Lib = "/static/plugins/flot/jquery.flot.categories.min.js"

  val moment = "/static/js/moment-with-locales.js"
  val datepicker: Lib = "/static/js/bootstrap-datetimepicker.js"



  //CKEDITOR
  val ckeditor = "//cdn.ckeditor.com/4.4.3/standard/ckeditor.js"
  val wysiwyg: Lib = "/static/plugins/ckeditor/ckeditor.js"
  val config: Lib = "/static/plugins/ckeditor/config.js"
  val style: Lib = "/static/plugins/ckeditor/styles.js"
  val lang: Lib = "/static/plugins/ckeditor/lang/en.js"

  val app_min: Lib = "/static/js/app.min.js"
  val demo: Lib = "/static/js/demo.js"

  val bootstrap_wysihtml5: Lib = "/static/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"


  lazy val indexOf: Map[Lib, Int] = {
    this.getClass.getDeclaredFields
      .filter(_.getType == classOf[String])
      .zipWithIndex.map(f => {f._1.setAccessible(true); f._1.get(this).asInstanceOf[Lib] -> f._2}).toMap
  }
}
