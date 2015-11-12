package org.slynx.v201510.pages

import controllers.RootController.XSPageHandle
import net.liftweb.util.Helpers._
import net.liftweb.util.Html5Parser
import org.joda.time.{DateTime, DateTimeZone}
import org.slynx.v201510.XSessionSupport
import org.slynx.v201510.monitor.XMonitor
import play.api.Play
import play.api.Play.current

import scala.collection.immutable.::
import scala.collection.mutable.ListBuffer
import scala.xml.{Node, NodeSeq}

trait XMonitorStandardPage {
  self: XMonitor =>

  object Styles {

    type Style = String

    val bootstrap_min: Style = "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
    val font_awesome_min: Style = "//maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"
    val ionicons_min: Style = "//code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"
    val shCore: Style = "//cdnjs.cloudflare.com/ajax/libs/SyntaxHighlighter/3.0.83/styles/shCore.css"
    val shThemeDefault: Style = "//cdnjs.cloudflare.com/ajax/libs/SyntaxHighlighter/3.0.83/styles/shThemeDefault.min.css"
    val toastr: Style = "//cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.2/toastr.min.css"
    val data_tables_bootstrap: Style = "//cdnjs.cloudflare.com/ajax/libs/datatables/1.10.9/css/dataTables.bootstrap.min.css"

    val datepicker: Style = "://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.css"


    //CKEDITOR
    val editor: Style = "//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.5.4/skins/moono/editor.css"
    val content: Style = "//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.5.4/contents.css"


    val bootstrap_wysihtml5: Style = "//bootstrap-wysiwyg.github.io/bootstrap3-wysiwyg/dist/bootstrap3-wysihtml5.min.css"
    val custom: Style = "/"+MonitorPath+"/static/custom.css"
    val adminLTE: Style = "/"+MonitorPath+"/static/adminLTE.css"
    val all_skins: Style = "/"+MonitorPath+"/static/all_skins.css"

    lazy val indexOf: Map[Style, Int] = {
      this.getClass.getDeclaredFields
        .filter(_.getType == classOf[String])
        .zipWithIndex.map(f => {f._1.setAccessible(true); f._1.get(this).asInstanceOf[Style] -> f._2}).toMap
    }
  }

  object Libs {

    type Lib = String

    val jquery_min: Lib = "//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
    val bootstrap_min: Lib = "//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"

    val jquery_ui: Lib ="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"
    val jquery_knob: Lib ="//raw.github.com/aterrien/jQuery-Knob/master/js/jquery.knob.js"

    val jquery_slimscroll_min: Lib = "//cdnjs.cloudflare.com/ajax/libs/jQuery-slimScroll/1.3.6/jquery.slimscroll.js"
    val fastclick_min: Lib = "//cdnjs.cloudflare.com/ajax/libs/fastclick/1.0.6/fastclick.js"

    val sh_core: Lib = "//cdnjs.cloudflare.com/ajax/libs/SyntaxHighlighter/3.0.83/scripts/shCore.min.js"
    val sh_brush: Lib = "//cdnjs.cloudflare.com/ajax/libs/SyntaxHighlighter/3.0.83/scripts/shBrushScala.js"

    val toastr: Lib = "//cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.2/toastr.min.js"

    val jquery_data_tables: Lib = "//cdnjs.cloudflare.com/ajax/libs/datatables/1.10.10/js/jquery.dataTables.js"
    val data_tables_bootstrap: Lib = "//cdnjs.cloudflare.com/ajax/libs/datatables/1.10.10/js/dataTables.bootstrap.min.js"
    val jquery_floatThead: Lib = "//cdnjs.cloudflare.com/ajax/libs/floatthead/1.3.1/jquery.floatThead.js"

    val jquery_flot: Lib = "//cdnjs.cloudflare.com/ajax/flot/jquery.flot.min.js"
    val jquery_flot_time: Lib ="//cdnjs.cloudflare.com/ajax/flot/jquery.flot.time.js"
    val jquery_flot_resize: Lib = "//cdnjs.cloudflare.com/ajax/flot/jquery.flot.resize.min.js"
    val jquery_flot_pie: Lib = "//cdnjs.cloudflare.com/ajax/flot/jquery.flot.pie.js"
    val jquery_flot_categories: Lib = "//cdnjs.cloudflare.com/ajax/flot/jquery.flot.categories.min.js"

    val moment = "//cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment-with-locales.min.js"
    val datepicker: Lib = "//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/js/bootstrap-datepicker.js"



    //CKEDITOR
    val ckeditor: Lib = "//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.5.4/ckeditor.js"
    val config: Lib = "//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.5.4/config.js"
    val style: Lib = "//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.5.4/styles.js"
    val lang: Lib = "//cdnjs.cloudflare.com/ajax/libs/ckeditor/4.5.4/en.js"

    val app_min: Lib ="/"+MonitorPath+"/static/app.js"
    val demo : Lib ="/"+MonitorPath+"/static/demo.js"

    val bootstrap_wysihtml5: Lib = "/"+MonitorPath+"/static/bootstrap3-wysihtml5.all.min.js"


    lazy val indexOf: Map[Lib, Int] = {
      this.getClass.getDeclaredFields
        .filter(_.getType == classOf[String])
        .zipWithIndex.map(f => {f._1.setAccessible(true); f._1.get(this).asInstanceOf[Lib] -> f._2}).toMap
    }
  }


  trait StandardPage extends Function1[NodeSeq, NodeSeq] {

    implicit val xsh: XSPageHandle
    def pageXsh: XSPageHandle = xsh
    def page = this
    def dateTime(millis: Long) = new DateTime(millis).withZone(DateTimeZone.forID("Europe/Lisbon"))

    val html5Parser = new Html5Parser {}
    def @@(path: Seq[String]): NodeSeq = (for {
      url <- Play.resource(s"/public/${path.mkString("/")}.html")
      template <- html5Parser.parse(url.openStream()).toOption
    } yield template).getOrElse(<div style="color:red;">NOT FOUND: /public/{path.mkString("/")}.html</div>)

    def surrundWith: List[String] = List("pages", "default")
    def surroundAt: String = "#page-content"

    def libs: Set[Libs.Lib] = {
      import Libs._
      Set(
        bootstrap_min
        , jquery_slimscroll_min
        , fastclick_min
        , toastr
        , bootstrap_wysihtml5
        , demo
        , app_min
      )
    }
    def libsInit: NodeSeq = libs.toList.sortBy(s => Libs.indexOf(s)).map(lib => <script src={lib} type="text/javascript"></script>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)

    def styles: Set[Styles.Style] = {
      import Styles._
      Set(
        bootstrap_min
        , font_awesome_min
        , ionicons_min
        , toastr
        , bootstrap_wysihtml5
        , adminLTE
        , all_skins
        , custom
      )
    }

    def stylesInit: NodeSeq = {
      styles.toList.sortBy(s => Styles.indexOf(s)).map(style => <link href={style} rel="stylesheet" type="text/css"></link>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty) ++
        scala.xml.Unparsed(
          """<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
            |<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
            |<!--[if lt IE 9]>
            |    <script src="//oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
            |    <script src="//oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
            |<![endif]-->
            |""".stripMargin
        )
    }

    def render: NodeSeq => NodeSeq = {
      "body *+" #> (libsInit ++ <head_merge>{stylesInit}</head_merge>)
    }

    def apply(ns: NodeSeq) = {
      val rslt = scala.xml.Unparsed("<!DOCTYPE html>\n") ++ render(ns)
      var tail = new ListBuffer[Node]
      var head = new ListBuffer[Node]
      val withoutTailAndHead = (
        "head_merge" #> ((ns: NodeSeq) => {head ++= ns.head.child; NodeSeq.Empty}) &
          "tail" #> ((ns: NodeSeq) => {tail ++= ns.head.child; NodeSeq.Empty})
        ).apply(rslt)
      ("head *" #> ((ns: NodeSeq) => ns ++ head) & "body *" #> ((ns: NodeSeq) => ns ++ tail)).apply(withoutTailAndHead)
    }

    def contents(): NodeSeq
    val pageTitle: String = ""
    val pageSubtitle: String = ""

    def rendered = apply(template)

    def template: NodeSeq = {
        <html>
          <head>
            <meta charset="utf-8" />
            <meta content="IE=edge" http-equiv="X-UA-Compatible" />
            <title>SlynX Dashboard</title>
            <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
          </head>
          <body class="sidebar-mini skin-red-light">
          <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
          <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js" type="text/javascript"></script>
          <div class="wrapper">

            <header class="main-header">
              <!-- Logo -->
              <a href="index2.html" class="logo">
                <!-- mini logo for sidebar mini 50x50 pixels -->
                <span class="logo-mini"><b>A</b>LT</span>
                <!-- logo for regular state and mobile devices -->
                <span class="logo-lg"><b>Admin</b>LTE</span>
              </a>
              <!-- Header Navbar: style can be found in header.less -->
              <nav class="navbar navbar-static-top" role="navigation">
                <!-- Sidebar toggle button-->
                <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                  <span class="sr-only">Toggle navigation</span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                </a>

                <div class="navbar-custom-menu">
                  <ul class="nav navbar-nav">

                    <li class="dropdown user user-menu">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">

                        <span class="hidden-xs">Admin</span>
                      </a>
                      <ul class="dropdown-menu">
                        <!-- User image -->

                        <!-- Menu Body -->

                        <!-- Menu Footer-->
                        <li class="user-footer">
                          <div class="pull-left">
                            <a href="#" class="btn btn-default btn-flat">Profile</a>
                          </div>
                          <div class="pull-right">
                            <a href="#" class="btn btn-default btn-flat">Sign out</a>
                          </div>
                        </li>
                      </ul>
                    </li>
                    <!-- Control Sidebar Toggle Button -->

                  </ul>
                </div>
              </nav>
            </header>
            <!-- Left side column. contains the logo and sidebar -->
            <aside class="main-sidebar">
              <!-- sidebar: style can be found in sidebar.less -->
              <section class="sidebar" style="height: auto;">
                <!-- Sidebar user panel -->



                <!-- /.search form -->
                <!-- sidebar menu: : style can be found in sidebar.less -->
                <ul class="sidebar-menu">
                  <li class="header">MAIN NAVIGATION</li>
                  <li class="treeview">
                    <a href="#">
                      <i class="fa fa-dashboard"></i> <span>Dashboard</span> <i class="fa fa-angle-left pull-right"></i>
                    </a>
                    <ul class="treeview-menu">
                      <li><a href="../index.html"><i class="fa fa-circle-o"></i> Dashboard v1</a></li>
                      <li><a href="../index2.html"><i class="fa fa-circle-o"></i> Dashboard v2</a></li>
                    </ul>
                  </li>

                  <li><a href="../documentation/index.html"><i class="fa fa-book"></i> <span>Documentation</span></a></li>

                </ul>
              </section>
              <!-- /.sidebar -->
            </aside>

            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper" style="min-height: 901px;">
              <!-- Content Header (Page header) -->
              <section class="content-header">
                <h1>
                  {pageTitle}
                  <small name="">{pageSubtitle}</small>
                </h1>
                <!--ol class="breadcrumb">
                  <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                  <li class="active">Widgets</li>
                </ol-->
              </section>

              <!-- Main content -->
              <section class="content">
                {contents()}
              </section>
              <!-- /.content -->
            </div>
            <!-- /.content-wrapper -->

            <footer class="main-footer">
              <div class="pull-right hidden-xs">
                <b>Version</b> 2.3.1
              </div>
              <strong>Copyright © 2015 <a href="http://www.slynx.org">Slynx Framework</a>.</strong> All rights
              reserved.
            </footer>

            <!-- Control Sidebar -->
            <aside class="control-sidebar control-sidebar-dark">
              <!-- Create the tabs -->
              <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
                <li class="active"><a href="#control-sidebar-theme-demo-options-tab" data-toggle="tab"><i class="fa fa-wrench"></i></a></li><li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
                <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
              </ul>
              <!-- Tab panes -->
              <div class="tab-content">
                <!-- Home tab content -->
                <div class="tab-pane" id="control-sidebar-home-tab">
                  <h3 class="control-sidebar-heading">Recent Activity</h3>
                  <ul class="control-sidebar-menu">
                    <li>
                      <a href="javascript::;">
                        <i class="menu-icon fa fa-birthday-cake bg-red"></i>

                        <div class="menu-info">
                          <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                          <p>Will be 23 on April 24th</p>
                        </div>
                      </a>
                    </li>
                    <li>
                      <a href="javascript::;">
                        <i class="menu-icon fa fa-user bg-yellow"></i>

                        <div class="menu-info">
                          <h4 class="control-sidebar-subheading">Frodo Updated His Profile</h4>

                          <p>New phone +1(800)555-1234</p>
                        </div>
                      </a>
                    </li>
                    <li>
                      <a href="javascript::;">
                        <i class="menu-icon fa fa-envelope-o bg-light-blue"></i>

                        <div class="menu-info">
                          <h4 class="control-sidebar-subheading">Nora Joined Mailing List</h4>

                          <p>nora@example.com</p>
                        </div>
                      </a>
                    </li>
                    <li>
                      <a href="javascript::;">
                        <i class="menu-icon fa fa-file-code-o bg-green"></i>

                        <div class="menu-info">
                          <h4 class="control-sidebar-subheading">Cron Job 254 Executed</h4>

                          <p>Execution time 5 seconds</p>
                        </div>
                      </a>
                    </li>
                  </ul>
                  <!-- /.control-sidebar-menu -->

                  <h3 class="control-sidebar-heading">Tasks Progress</h3>
                  <ul class="control-sidebar-menu">
                    <li>
                      <a href="javascript::;">
                        <h4 class="control-sidebar-subheading">
                          Custom Template Design
                          <span class="label label-danger pull-right">70%</span>
                        </h4>

                        <div class="progress progress-xxs">
                          <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
                        </div>
                      </a>
                    </li>
                    <li>
                      <a href="javascript::;">
                        <h4 class="control-sidebar-subheading">
                          Update Resume
                          <span class="label label-success pull-right">95%</span>
                        </h4>

                        <div class="progress progress-xxs">
                          <div class="progress-bar progress-bar-success" style="width: 95%"></div>
                        </div>
                      </a>
                    </li>
                    <li>
                      <a href="javascript::;">
                        <h4 class="control-sidebar-subheading">
                          Laravel Integration
                          <span class="label label-warning pull-right">50%</span>
                        </h4>

                        <div class="progress progress-xxs">
                          <div class="progress-bar progress-bar-warning" style="width: 50%"></div>
                        </div>
                      </a>
                    </li>
                    <li>
                      <a href="javascript::;">
                        <h4 class="control-sidebar-subheading">
                          Back End Framework
                          <span class="label label-primary pull-right">68%</span>
                        </h4>

                        <div class="progress progress-xxs">
                          <div class="progress-bar progress-bar-primary" style="width: 68%"></div>
                        </div>
                      </a>
                    </li>
                  </ul>
                  <!-- /.control-sidebar-menu -->

                </div>
                <!-- /.tab-pane -->
                <!-- Stats tab content -->
                <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div>
                <!-- /.tab-pane -->
                <!-- Settings tab content -->
                <div class="tab-pane" id="control-sidebar-settings-tab">
                  <form method="post">
                    <h3 class="control-sidebar-heading">General Settings</h3>

                    <div class="form-group">
                      <label class="control-sidebar-subheading">
                        Report panel usage
                        <input type="checkbox" class="pull-right" checked=""/>
                      </label>

                      <p>
                        Some information about this general settings option
                      </p>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                      <label class="control-sidebar-subheading">
                        Allow mail redirect
                        <input type="checkbox" class="pull-right" checked=""/>
                      </label>

                      <p>
                        Other sets of options are available
                      </p>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                      <label class="control-sidebar-subheading">
                        Expose author name in posts
                        <input type="checkbox" class="pull-right" checked=""/>
                      </label>

                      <p>
                        Allow the user to show his name in blog posts
                      </p>
                    </div>
                    <!-- /.form-group -->

                    <h3 class="control-sidebar-heading">Chat Settings</h3>

                    <div class="form-group">
                      <label class="control-sidebar-subheading">
                        Show me as online
                        <input type="checkbox" class="pull-right" checked=""/>
                      </label>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                      <label class="control-sidebar-subheading">
                        Turn off notifications
                        <input type="checkbox" class="pull-right"/>
                      </label>
                    </div>
                    <!-- /.form-group -->

                    <div class="form-group">
                      <label class="control-sidebar-subheading">
                        Delete chat history
                        <a href="javascript::;" class="text-red pull-right"><i class="fa fa-trash-o"></i></a>
                      </label>
                    </div>
                    <!-- /.form-group -->
                  </form>
                </div>
                <!-- /.tab-pane -->
              </div>
            </aside>
            <!-- /.control-sidebar -->
            <!-- Add the sidebar's background. This div must be placed
                 immediately after the control sidebar -->
            <div class="control-sidebar-bg" style="position: fixed; height: auto;"></div>
          </div>
          </body>
        </html>
    }
  }

}
