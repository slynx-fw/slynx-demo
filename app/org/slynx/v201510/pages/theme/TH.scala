package org.slynx.v201510.pages.theme

import java.util.{Date, UUID}


import util.ImplicitHelpers._
import net.liftweb.builtin.snippet.Tail
import net.liftweb.common.{Full, Box}
import net.liftweb.http.SHtml
import net.liftweb.http.SHtml.ElemAttr
import net.liftweb.http.js.JE.{ValById, AnonFunc, JsRaw}
import net.liftweb.http.js.jquery.JqJsCmds.{Hide, Show}
import net.liftweb.http.js.{JsExp, JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds._
import net.liftweb.json._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import play.api.Play
import play.api.mvc.Result
import util.ImplicitHelpers.Pipe
import util.{Props, JsonSerializable, RawJSON}
import scala.concurrent.Future
import scala.xml.{Xhtml, NodeSeq}
import scala.xml.NodeSeq
import org.slynx.v201510.monitor.XMonitor
import org.slynx.v201510.monitor._

trait XTH {
  self: XMonitor =>

  trait SBase {

    object TextAlign {
      type Align = String
      val Left = ("left": Align)
      val Center = ("center": Align)
      val Right = ("right": Align)
    }

  }


  trait SWidgets extends SBase {

    object WidgetStyle extends Enumeration {
      val Default = Value
      val Primary = Value
      val Info = Value
      val Warning = Value
      val Success = Value
      val Danger = Value

      implicit class RichWidgetStyle(v: Value) {

        def toClass: String = v match {
          case Default => ""
          case Primary => " box-primary "
          case Info => " box-info "
          case Warning => " box-warning "
          case Success => " box-success "
          case Danger => " box-danger "
        }
      }

    }

    trait Widget extends Id {
      def style: WidgetStyle.Value = WidgetStyle.Default
      def closable: Boolean = false
      def collapsible: Boolean = false
      def solid: Boolean = false
      def widgetTextAlign: TextAlign.Align = TextAlign.Left

      val widgetId = id('widget)
      val headerId = id('contents)
      val bodyId = id('body)

      def widgetTitle: String
      def widgetActions: NodeSeq = NodeSeq.Empty

      def renderedHeader: NodeSeq = {
      <div class="box-header with-border">
        <h3 class="box-title">{widgetTitle}</h3>
        <div class="box-tools pull-right"> {
          widgetActions ++
            (if (collapsible) <button data-widget="collapse" class="btn btn-box-tool"><i class="fa fa-minus"></i></button> else NodeSeq.Empty) ++
            (if (closable) <button data-widget="remove" class="btn btn-box-tool"><i class="fa fa-times"></i></button> else NodeSeq.Empty)
        } </div>
      </div>
    }

      def widgetBody: NodeSeq

      def renderedBody: NodeSeq = {
      <div class="box-body" id={bodyId} style={s"text-align: ${widgetTextAlign}"}>
        {widgetBody}
      </div>
    }

      def rerenderWidget() = SetHtml(widgetId, renderedWidget)
      def rerenderHeader() = SetHtml(headerId, renderedHeader)
      def rerenderBody() = SetHtml(bodyId, widgetBody)

      def widgetClass = "box " + style.toClass + (if (solid) " box-solid" else "")
      def widgetStyle = ""

      def renderedWidget: NodeSeq = {
      <div id={widgetId} class={widgetClass} style={widgetStyle}>
        {renderedHeader}
        {renderedBody}
      </div>
    }
    }

    case class SimpleWidget(
                             widgetTitle: String,
                             override val style: WidgetStyle.Value = WidgetStyle.Default,
                             override val closable: Boolean = false,
                             override val collapsible: Boolean = false,
                             override val solid: Boolean = false,
                             override val widgetTextAlign: TextAlign.Align = TextAlign.Left,
                             override val widgetActions: NodeSeq = NodeSeq.Empty
                             )(
                             val widgetBody: NodeSeq
                             ) extends Widget

  }


  trait SIcons {

    trait Icon {

      def clas: String

      def icn: NodeSeq = <i class={clas}></i>
    }

    case class FAIcn private[theme](s: String) extends Icon {
      override def clas: String = "fa " + s
    }

    object IcnFA {

      val icn_adjust = FAIcn("fa-adjust")
      val icn_adn = FAIcn("fa-adn")
      val icn_align_center = FAIcn("fa-align-center")
      val icn_align_justify = FAIcn("fa-align-justify")
      val icn_align_left = FAIcn("fa-align-left")
      val icn_align_right = FAIcn("fa-align-right")
      val icn_ambulance = FAIcn("fa-ambulance")
      val icn_anchor = FAIcn("fa-anchor")
      val icn_android = FAIcn("fa-android")
      val icn_angellist = FAIcn("fa-angellist")
      val icn_angle_double_down = FAIcn("fa-angle-double-down")
      val icn_angle_double_left = FAIcn("fa-angle-double-left")
      val icn_angle_double_right = FAIcn("fa-angle-double-right")
      val icn_angle_double_up = FAIcn("fa-angle-double-up")
      val icn_angle_down = FAIcn("fa-angle-down")
      val icn_angle_left = FAIcn("fa-angle-left")
      val icn_angle_right = FAIcn("fa-angle-right")
      val icn_angle_up = FAIcn("fa-angle-up")
      val icn_apple = FAIcn("fa-apple")
      val icn_archive = FAIcn("fa-archive")
      val icn_area_chart = FAIcn("fa-area-chart")
      val icn_arrow_circle_down = FAIcn("fa-arrow-circle-down")
      val icn_arrow_circle_left = FAIcn("fa-arrow-circle-left")
      val icn_arrow_circle_o_down = FAIcn("fa-arrow-circle-o-down")
      val icn_arrow_circle_o_left = FAIcn("fa-arrow-circle-o-left")
      val icn_arrow_circle_o_right = FAIcn("fa-arrow-circle-o-right")
      val icn_arrow_circle_o_up = FAIcn("fa-arrow-circle-o-up")
      val icn_arrow_circle_right = FAIcn("fa-arrow-circle-right")
      val icn_arrow_circle_up = FAIcn("fa-arrow-circle-up")
      val icn_arrow_down = FAIcn("fa-arrow-down")
      val icn_arrow_left = FAIcn("fa-arrow-left")
      val icn_arrow_right = FAIcn("fa-arrow-right")
      val icn_arrow_up = FAIcn("fa-arrow-up")
      val icn_arrows = FAIcn("fa-arrows")
      val icn_arrows_alt = FAIcn("fa-arrows-alt")
      val icn_arrows_h = FAIcn("fa-arrows-h")
      val icn_arrows_v = FAIcn("fa-arrows-v")
      val icn_asterisk = FAIcn("fa-asterisk")
      val icn_at = FAIcn("fa-at")
      val icn_automobile = FAIcn("fa-automobile")
      val icn_backward = FAIcn("fa-backward")
      val icn_ban = FAIcn("fa-ban")
      val icn_bank = FAIcn("fa-bank")
      val icn_bar_chart = FAIcn("fa-bar-chart")
      val icn_bar_chart_o = FAIcn("fa-bar-chart-o")
      val icn_barcode = FAIcn("fa-barcode")
      val icn_bars = FAIcn("fa-bars")
      val icn_bed = FAIcn("fa-bed")
      val icn_beer = FAIcn("fa-beer")
      val icn_behance = FAIcn("fa-behance")
      val icn_behance_square = FAIcn("fa-behance-square")
      val icn_bell = FAIcn("fa-bell")
      val icn_bell_o = FAIcn("fa-bell-o")
      val icn_bell_slash = FAIcn("fa-bell-slash")
      val icn_bell_slash_o = FAIcn("fa-bell-slash-o")
      val icn_bicycle = FAIcn("fa-bicycle")
      val icn_binoculars = FAIcn("fa-binoculars")
      val icn_birthday_cake = FAIcn("fa-birthday-cake")
      val icn_bitbucket = FAIcn("fa-bitbucket")
      val icn_bitbucket_square = FAIcn("fa-bitbucket-square")
      val icn_bitcoin = FAIcn("fa-bitcoin")
      val icn_bold = FAIcn("fa-bold")
      val icn_bolt = FAIcn("fa-bolt")
      val icn_bomb = FAIcn("fa-bomb")
      val icn_book = FAIcn("fa-book")
      val icn_bookmark = FAIcn("fa-bookmark")
      val icn_bookmark_o = FAIcn("fa-bookmark-o")
      val icn_briefcase = FAIcn("fa-briefcase")
      val icn_btc = FAIcn("fa-btc")
      val icn_bug = FAIcn("fa-bug")
      val icn_building = FAIcn("fa-building")
      val icn_building_o = FAIcn("fa-building-o")
      val icn_bullhorn = FAIcn("fa-bullhorn")
      val icn_bullseye = FAIcn("fa-bullseye")
      val icn_bus = FAIcn("fa-bus")
      val icn_buysellads = FAIcn("fa-buysellads")
      val icn_cab = FAIcn("fa-cab")
      val icn_calculator = FAIcn("fa-calculator")
      val icn_calendar = FAIcn("fa-calendar")
      val icn_calendar_o = FAIcn("fa-calendar-o")
      val icn_camera = FAIcn("fa-camera")
      val icn_camera_retro = FAIcn("fa-camera-retro")
      val icn_car = FAIcn("fa-car")
      val icn_caret_down = FAIcn("fa-caret-down")
      val icn_caret_left = FAIcn("fa-caret-left")
      val icn_caret_right = FAIcn("fa-caret-right")
      val icn_caret_square_o_down = FAIcn("fa-caret-square-o-down")
      val icn_caret_square_o_left = FAIcn("fa-caret-square-o-left")
      val icn_caret_square_o_right = FAIcn("fa-caret-square-o-right")
      val icn_caret_square_o_up = FAIcn("fa-caret-square-o-up")
      val icn_caret_up = FAIcn("fa-caret-up")
      val icn_cart_arrow_down = FAIcn("fa-cart-arrow-down")
      val icn_cart_plus = FAIcn("fa-cart-plus")
      val icn_cc = FAIcn("fa-cc")
      val icn_cc_amex = FAIcn("fa-cc-amex")
      val icn_cc_discover = FAIcn("fa-cc-discover")
      val icn_cc_mastercard = FAIcn("fa-cc-mastercard")
      val icn_cc_paypal = FAIcn("fa-cc-paypal")
      val icn_cc_stripe = FAIcn("fa-cc-stripe")
      val icn_cc_visa = FAIcn("fa-cc-visa")
      val icn_certificate = FAIcn("fa-certificate")
      val icn_chain = FAIcn("fa-chain")
      val icn_chain_broken = FAIcn("fa-chain-broken")
      val icn_check = FAIcn("fa-check")
      val icn_check_circle = FAIcn("fa-check-circle")
      val icn_check_circle_o = FAIcn("fa-check-circle-o")
      val icn_check_square = FAIcn("fa-check-square")
      val icn_check_square_o = FAIcn("fa-check-square-o")
      val icn_chevron_circle_down = FAIcn("fa-chevron-circle-down")
      val icn_chevron_circle_left = FAIcn("fa-chevron-circle-left")
      val icn_chevron_circle_right = FAIcn("fa-chevron-circle-right")
      val icn_chevron_circle_up = FAIcn("fa-chevron-circle-up")
      val icn_chevron_down = FAIcn("fa-chevron-down")
      val icn_chevron_left = FAIcn("fa-chevron-left")
      val icn_chevron_right = FAIcn("fa-chevron-right")
      val icn_chevron_up = FAIcn("fa-chevron-up")
      val icn_child = FAIcn("fa-child")
      val icn_circle = FAIcn("fa-circle")
      val icn_circle_o = FAIcn("fa-circle-o")
      val icn_circle_o_notch = FAIcn("fa-circle-o-notch")
      val icn_circle_thin = FAIcn("fa-circle-thin")
      val icn_clipboard = FAIcn("fa-clipboard")
      val icn_clock_o = FAIcn("fa-clock-o")
      val icn_close = FAIcn("fa-close")
      val icn_cloud = FAIcn("fa-cloud")
      val icn_cloud_download = FAIcn("fa-cloud-download")
      val icn_cloud_upload = FAIcn("fa-cloud-upload")
      val icn_cny = FAIcn("fa-cny")
      val icn_code = FAIcn("fa-code")
      val icn_code_fork = FAIcn("fa-code-fork")
      val icn_codepen = FAIcn("fa-codepen")
      val icn_coffee = FAIcn("fa-coffee")
      val icn_cog = FAIcn("fa-cog")
      val icn_cogs = FAIcn("fa-cogs")
      val icn_columns = FAIcn("fa-columns")
      val icn_comment = FAIcn("fa-comment")
      val icn_comment_o = FAIcn("fa-comment-o")
      val icn_comments = FAIcn("fa-comments")
      val icn_comments_o = FAIcn("fa-comments-o")
      val icn_compass = FAIcn("fa-compass")
      val icn_compress = FAIcn("fa-compress")
      val icn_connectdevelop = FAIcn("fa-connectdevelop")
      val icn_copy = FAIcn("fa-copy")
      val icn_copyright = FAIcn("fa-copyright")
      val icn_credit_card = FAIcn("fa-credit-card")
      val icn_crop = FAIcn("fa-crop")
      val icn_crosshairs = FAIcn("fa-crosshairs")
      val icn_css3 = FAIcn("fa-css3")
      val icn_cube = FAIcn("fa-cube")
      val icn_cubes = FAIcn("fa-cubes")
      val icn_cut = FAIcn("fa-cut")
      val icn_cutlery = FAIcn("fa-cutlery")
      val icn_dashboard = FAIcn("fa-dashboard")
      val icn_dashcube = FAIcn("fa-dashcube")
      val icn_database = FAIcn("fa-database")
      val icn_dedent = FAIcn("fa-dedent")
      val icn_delicious = FAIcn("fa-delicious")
      val icn_desktop = FAIcn("fa-desktop")
      val icn_deviantart = FAIcn("fa-deviantart")
      val icn_diamond = FAIcn("fa-diamond")
      val icn_digg = FAIcn("fa-digg")
      val icn_dollar = FAIcn("fa-dollar")
      val icn_dot_circle_o = FAIcn("fa-dot-circle-o")
      val icn_download = FAIcn("fa-download")
      val icn_dribbble = FAIcn("fa-dribbble")
      val icn_dropbox = FAIcn("fa-dropbox")
      val icn_drupal = FAIcn("fa-drupal")
      val icn_edit = FAIcn("fa-edit")
      val icn_eject = FAIcn("fa-eject")
      val icn_ellipsis_h = FAIcn("fa-ellipsis-h")
      val icn_ellipsis_v = FAIcn("fa-ellipsis-v")
      val icn_empire = FAIcn("fa-empire")
      val icn_envelope = FAIcn("fa-envelope")
      val icn_envelope_o = FAIcn("fa-envelope-o")
      val icn_envelope_square = FAIcn("fa-envelope-square")
      val icn_eraser = FAIcn("fa-eraser")
      val icn_eur = FAIcn("fa-eur")
      val icn_euro = FAIcn("fa-euro")
      val icn_exchange = FAIcn("fa-exchange")
      val icn_exclamation = FAIcn("fa-exclamation")
      val icn_exclamation_circle = FAIcn("fa-exclamation-circle")
      val icn_exclamation_triangle = FAIcn("fa-exclamation-triangle")
      val icn_expand = FAIcn("fa-expand")
      val icn_external_link = FAIcn("fa-external-link")
      val icn_external_link_square = FAIcn("fa-external-link-square")
      val icn_eye = FAIcn("fa-eye")
      val icn_eye_slash = FAIcn("fa-eye-slash")
      val icn_eyedropper = FAIcn("fa-eyedropper")
      val icn_facebook = FAIcn("fa-facebook")
      val icn_facebook_f = FAIcn("fa-facebook-f")
      val icn_facebook_official = FAIcn("fa-facebook-official")
      val icn_facebook_square = FAIcn("fa-facebook-square")
      val icn_fast_backward = FAIcn("fa-fast-backward")
      val icn_fast_forward = FAIcn("fa-fast-forward")
      val icn_fax = FAIcn("fa-fax")
      val icn_female = FAIcn("fa-female")
      val icn_fighter_jet = FAIcn("fa-fighter-jet")
      val icn_file = FAIcn("fa-file")
      val icn_file_archive_o = FAIcn("fa-file-archive-o")
      val icn_file_audio_o = FAIcn("fa-file-audio-o")
      val icn_file_code_o = FAIcn("fa-file-code-o")
      val icn_file_excel_o = FAIcn("fa-file-excel-o")
      val icn_file_image_o = FAIcn("fa-file-image-o")
      val icn_file_movie_o = FAIcn("fa-file-movie-o")
      val icn_file_o = FAIcn("fa-file-o")
      val icn_file_pdf_o = FAIcn("fa-file-pdf-o")
      val icn_file_photo_o = FAIcn("fa-file-photo-o")
      val icn_file_picture_o = FAIcn("fa-file-picture-o")
      val icn_file_powerpoint_o = FAIcn("fa-file-powerpoint-o")
      val icn_file_sound_o = FAIcn("fa-file-sound-o")
      val icn_file_text = FAIcn("fa-file-text")
      val icn_file_text_o = FAIcn("fa-file-text-o")
      val icn_file_video_o = FAIcn("fa-file-video-o")
      val icn_file_word_o = FAIcn("fa-file-word-o")
      val icn_file_zip_o = FAIcn("fa-file-zip-o")
      val icn_files_o = FAIcn("fa-files-o")
      val icn_film = FAIcn("fa-film")
      val icn_filter = FAIcn("fa-filter")
      val icn_fire = FAIcn("fa-fire")
      val icn_fire_extinguisher = FAIcn("fa-fire-extinguisher")
      val icn_flag = FAIcn("fa-flag")
      val icn_flag_checkered = FAIcn("fa-flag-checkered")
      val icn_flag_o = FAIcn("fa-flag-o")
      val icn_flash = FAIcn("fa-flash")
      val icn_flask = FAIcn("fa-flask")
      val icn_flickr = FAIcn("fa-flickr")
      val icn_floppy_o = FAIcn("fa-floppy-o")
      val icn_folder = FAIcn("fa-folder")
      val icn_folder_o = FAIcn("fa-folder-o")
      val icn_folder_open = FAIcn("fa-folder-open")
      val icn_folder_open_o = FAIcn("fa-folder-open-o")
      val icn_font = FAIcn("fa-font")
      val icn_forumbee = FAIcn("fa-forumbee")
      val icn_forward = FAIcn("fa-forward")
      val icn_foursquare = FAIcn("fa-foursquare")
      val icn_frown_o = FAIcn("fa-frown-o")
      val icn_futbol_o = FAIcn("fa-futbol-o")
      val icn_gamepad = FAIcn("fa-gamepad")
      val icn_gavel = FAIcn("fa-gavel")
      val icn_gbp = FAIcn("fa-gbp")
      val icn_ge = FAIcn("fa-ge")
      val icn_gear = FAIcn("fa-gear")
      val icn_gears = FAIcn("fa-gears")
      val icn_genderless = FAIcn("fa-genderless")
      val icn_gift = FAIcn("fa-gift")
      val icn_git = FAIcn("fa-git")
      val icn_git_square = FAIcn("fa-git-square")
      val icn_github = FAIcn("fa-github")
      val icn_github_alt = FAIcn("fa-github-alt")
      val icn_github_square = FAIcn("fa-github-square")
      val icn_gittip = FAIcn("fa-gittip")
      val icn_glass = FAIcn("fa-glass")
      val icn_globe = FAIcn("fa-globe")
      val icn_google = FAIcn("fa-google")
      val icn_google_plus = FAIcn("fa-google-plus")
      val icn_google_plus_square = FAIcn("fa-google-plus-square")
      val icn_google_wallet = FAIcn("fa-google-wallet")
      val icn_graduation_cap = FAIcn("fa-graduation-cap")
      val icn_gratipay = FAIcn("fa-gratipay")
      val icn_group = FAIcn("fa-group")
      val icn_h_square = FAIcn("fa-h-square")
      val icn_hacker_news = FAIcn("fa-hacker-news")
      val icn_hand_o_down = FAIcn("fa-hand-o-down")
      val icn_hand_o_left = FAIcn("fa-hand-o-left")
      val icn_hand_o_right = FAIcn("fa-hand-o-right")
      val icn_hand_o_up = FAIcn("fa-hand-o-up")
      val icn_hdd_o = FAIcn("fa-hdd-o")
      val icn_header = FAIcn("fa-header")
      val icn_headphones = FAIcn("fa-headphones")
      val icn_heart = FAIcn("fa-heart")
      val icn_heart_o = FAIcn("fa-heart-o")
      val icn_heartbeat = FAIcn("fa-heartbeat")
      val icn_history = FAIcn("fa-history")
      val icn_home = FAIcn("fa-home")
      val icn_hospital_o = FAIcn("fa-hospital-o")
      val icn_hotel = FAIcn("fa-hotel")
      val icn_html5 = FAIcn("fa-html5")
      val icn_ils = FAIcn("fa-ils")
      val icn_image = FAIcn("fa-image")
      val icn_inbox = FAIcn("fa-inbox")
      val icn_indent = FAIcn("fa-indent")
      val icn_info = FAIcn("fa-info")
      val icn_info_circle = FAIcn("fa-info-circle")
      val icn_inr = FAIcn("fa-inr")
      val icn_instagram = FAIcn("fa-instagram")
      val icn_institution = FAIcn("fa-institution")
      val icn_ioxhost = FAIcn("fa-ioxhost")
      val icn_italic = FAIcn("fa-italic")
      val icn_joomla = FAIcn("fa-joomla")
      val icn_jpy = FAIcn("fa-jpy")
      val icn_jsfiddle = FAIcn("fa-jsfiddle")
      val icn_key = FAIcn("fa-key")
      val icn_keyboard_o = FAIcn("fa-keyboard-o")
      val icn_krw = FAIcn("fa-krw")
      val icn_language = FAIcn("fa-language")
      val icn_laptop = FAIcn("fa-laptop")
      val icn_lastfm = FAIcn("fa-lastfm")
      val icn_lastfm_square = FAIcn("fa-lastfm-square")
      val icn_leaf = FAIcn("fa-leaf")
      val icn_leanpub = FAIcn("fa-leanpub")
      val icn_legal = FAIcn("fa-legal")
      val icn_lemon_o = FAIcn("fa-lemon-o")
      val icn_level_down = FAIcn("fa-level-down")
      val icn_level_up = FAIcn("fa-level-up")
      val icn_life_bouy = FAIcn("fa-life-bouy")
      val icn_life_buoy = FAIcn("fa-life-buoy")
      val icn_life_ring = FAIcn("fa-life-ring")
      val icn_life_saver = FAIcn("fa-life-saver")
      val icn_lightbulb_o = FAIcn("fa-lightbulb-o")
      val icn_line_chart = FAIcn("fa-line-chart")
      val icn_link = FAIcn("fa-link")
      val icn_linkedin = FAIcn("fa-linkedin")
      val icn_linkedin_square = FAIcn("fa-linkedin-square")
      val icn_linux = FAIcn("fa-linux")
      val icn_list = FAIcn("fa-list")
      val icn_list_alt = FAIcn("fa-list-alt")
      val icn_list_ol = FAIcn("fa-list-ol")
      val icn_list_ul = FAIcn("fa-list-ul")
      val icn_location_arrow = FAIcn("fa-location-arrow")
      val icn_lock = FAIcn("fa-lock")
      val icn_long_arrow_down = FAIcn("fa-long-arrow-down")
      val icn_long_arrow_left = FAIcn("fa-long-arrow-left")
      val icn_long_arrow_right = FAIcn("fa-long-arrow-right")
      val icn_long_arrow_up = FAIcn("fa-long-arrow-up")
      val icn_magic = FAIcn("fa-magic")
      val icn_magnet = FAIcn("fa-magnet")
      val icn_mail_forward = FAIcn("fa-mail-forward")
      val icn_mail_reply = FAIcn("fa-mail-reply")
      val icn_mail_reply_all = FAIcn("fa-mail-reply-all")
      val icn_male = FAIcn("fa-male")
      val icn_map_marker = FAIcn("fa-map-marker")
      val icn_mars = FAIcn("fa-mars")
      val icn_mars_double = FAIcn("fa-mars-double")
      val icn_mars_stroke = FAIcn("fa-mars-stroke")
      val icn_mars_stroke_h = FAIcn("fa-mars-stroke-h")
      val icn_mars_stroke_v = FAIcn("fa-mars-stroke-v")
      val icn_maxcdn = FAIcn("fa-maxcdn")
      val icn_meanpath = FAIcn("fa-meanpath")
      val icn_medium = FAIcn("fa-medium")
      val icn_medkit = FAIcn("fa-medkit")
      val icn_meh_o = FAIcn("fa-meh-o")
      val icn_mercury = FAIcn("fa-mercury")
      val icn_microphone = FAIcn("fa-microphone")
      val icn_microphone_slash = FAIcn("fa-microphone-slash")
      val icn_minus = FAIcn("fa-minus")
      val icn_minus_circle = FAIcn("fa-minus-circle")
      val icn_minus_square = FAIcn("fa-minus-square")
      val icn_minus_square_o = FAIcn("fa-minus-square-o")
      val icn_mobile = FAIcn("fa-mobile")
      val icn_mobile_phone = FAIcn("fa-mobile-phone")
      val icn_money = FAIcn("fa-money")
      val icn_moon_o = FAIcn("fa-moon-o")
      val icn_mortar_board = FAIcn("fa-mortar-board")
      val icn_motorcycle = FAIcn("fa-motorcycle")
      val icn_music = FAIcn("fa-music")
      val icn_navicon = FAIcn("fa-navicon")
      val icn_neuter = FAIcn("fa-neuter")
      val icn_newspaper_o = FAIcn("fa-newspaper-o")
      val icn_openid = FAIcn("fa-openid")
      val icn_outdent = FAIcn("fa-outdent")
      val icn_pagelines = FAIcn("fa-pagelines")
      val icn_paint_brush = FAIcn("fa-paint-brush")
      val icn_paper_plane = FAIcn("fa-paper-plane")
      val icn_paper_plane_o = FAIcn("fa-paper-plane-o")
      val icn_paperclip = FAIcn("fa-paperclip")
      val icn_paragraph = FAIcn("fa-paragraph")
      val icn_paste = FAIcn("fa-paste")
      val icn_pause = FAIcn("fa-pause")
      val icn_paw = FAIcn("fa-paw")
      val icn_paypal = FAIcn("fa-paypal")
      val icn_pencil = FAIcn("fa-pencil")
      val icn_pencil_square = FAIcn("fa-pencil-square")
      val icn_pencil_square_o = FAIcn("fa-pencil-square-o")
      val icn_phone = FAIcn("fa-phone")
      val icn_phone_square = FAIcn("fa-phone-square")
      val icn_photo = FAIcn("fa-photo")
      val icn_picture_o = FAIcn("fa-picture-o")
      val icn_pie_chart = FAIcn("fa-pie-chart")
      val icn_pied_piper = FAIcn("fa-pied-piper")
      val icn_pied_piper_alt = FAIcn("fa-pied-piper-alt")
      val icn_pinterest = FAIcn("fa-pinterest")
      val icn_pinterest_p = FAIcn("fa-pinterest-p")
      val icn_pinterest_square = FAIcn("fa-pinterest-square")
      val icn_plane = FAIcn("fa-plane")
      val icn_play = FAIcn("fa-play")
      val icn_play_circle = FAIcn("fa-play-circle")
      val icn_play_circle_o = FAIcn("fa-play-circle-o")
      val icn_plug = FAIcn("fa-plug")
      val icn_plus = FAIcn("fa-plus")
      val icn_plus_circle = FAIcn("fa-plus-circle")
      val icn_plus_square = FAIcn("fa-plus-square")
      val icn_plus_square_o = FAIcn("fa-plus-square-o")
      val icn_power_off = FAIcn("fa-power-off")
      val icn_print = FAIcn("fa-print")
      val icn_puzzle_piece = FAIcn("fa-puzzle-piece")
      val icn_qq = FAIcn("fa-qq")
      val icn_qrcode = FAIcn("fa-qrcode")
      val icn_question = FAIcn("fa-question")
      val icn_question_circle = FAIcn("fa-question-circle")
      val icn_quote_left = FAIcn("fa-quote-left")
      val icn_quote_right = FAIcn("fa-quote-right")
      val icn_ra = FAIcn("fa-ra")
      val icn_random = FAIcn("fa-random")
      val icn_rebel = FAIcn("fa-rebel")
      val icn_recycle = FAIcn("fa-recycle")
      val icn_reddit = FAIcn("fa-reddit")
      val icn_reddit_square = FAIcn("fa-reddit-square")
      val icn_refresh = FAIcn("fa-refresh")
      val icn_remove = FAIcn("fa-remove")
      val icn_renren = FAIcn("fa-renren")
      val icn_reorder = FAIcn("fa-reorder")
      val icn_repeat = FAIcn("fa-repeat")
      val icn_reply = FAIcn("fa-reply")
      val icn_reply_all = FAIcn("fa-reply-all")
      val icn_retweet = FAIcn("fa-retweet")
      val icn_rmb = FAIcn("fa-rmb")
      val icn_road = FAIcn("fa-road")
      val icn_rocket = FAIcn("fa-rocket")
      val icn_rotate_left = FAIcn("fa-rotate-left")
      val icn_rotate_right = FAIcn("fa-rotate-right")
      val icn_rouble = FAIcn("fa-rouble")
      val icn_rss = FAIcn("fa-rss")
      val icn_rss_square = FAIcn("fa-rss-square")
      val icn_rub = FAIcn("fa-rub")
      val icn_ruble = FAIcn("fa-ruble")
      val icn_rupee = FAIcn("fa-rupee")
      val icn_save = FAIcn("fa-save")
      val icn_scissors = FAIcn("fa-scissors")
      val icn_search = FAIcn("fa-search")
      val icn_search_minus = FAIcn("fa-search-minus")
      val icn_search_plus = FAIcn("fa-search-plus")
      val icn_sellsy = FAIcn("fa-sellsy")
      val icn_send = FAIcn("fa-send")
      val icn_send_o = FAIcn("fa-send-o")
      val icn_server = FAIcn("fa-server")
      val icn_share = FAIcn("fa-share")
      val icn_share_alt = FAIcn("fa-share-alt")
      val icn_share_alt_square = FAIcn("fa-share-alt-square")
      val icn_share_square = FAIcn("fa-share-square")
      val icn_share_square_o = FAIcn("fa-share-square-o")
      val icn_shekel = FAIcn("fa-shekel")
      val icn_sheqel = FAIcn("fa-sheqel")
      val icn_shield = FAIcn("fa-shield")
      val icn_ship = FAIcn("fa-ship")
      val icn_shirtsinbulk = FAIcn("fa-shirtsinbulk")
      val icn_shopping_cart = FAIcn("fa-shopping-cart")
      val icn_sign_in = FAIcn("fa-sign-in")
      val icn_sign_out = FAIcn("fa-sign-out")
      val icn_signal = FAIcn("fa-signal")
      val icn_simplybuilt = FAIcn("fa-simplybuilt")
      val icn_sitemap = FAIcn("fa-sitemap")
      val icn_skyatlas = FAIcn("fa-skyatlas")
      val icn_skype = FAIcn("fa-skype")
      val icn_slack = FAIcn("fa-slack")
      val icn_sliders = FAIcn("fa-sliders")
      val icn_slideshare = FAIcn("fa-slideshare")
      val icn_smile_o = FAIcn("fa-smile-o")
      val icn_soccer_ball_o = FAIcn("fa-soccer-ball-o")
      val icn_sort = FAIcn("fa-sort")
      val icn_sort_alpha_asc = FAIcn("fa-sort-alpha-asc")
      val icn_sort_alpha_desc = FAIcn("fa-sort-alpha-desc")
      val icn_sort_amount_asc = FAIcn("fa-sort-amount-asc")
      val icn_sort_amount_desc = FAIcn("fa-sort-amount-desc")
      val icn_sort_asc = FAIcn("fa-sort-asc")
      val icn_sort_desc = FAIcn("fa-sort-desc")
      val icn_sort_down = FAIcn("fa-sort-down")
      val icn_sort_numeric_asc = FAIcn("fa-sort-numeric-asc")
      val icn_sort_numeric_desc = FAIcn("fa-sort-numeric-desc")
      val icn_sort_up = FAIcn("fa-sort-up")
      val icn_soundcloud = FAIcn("fa-soundcloud")
      val icn_space_shuttle = FAIcn("fa-space-shuttle")
      val icn_spinner = FAIcn("fa-spinner")
      val icn_spoon = FAIcn("fa-spoon")
      val icn_spotify = FAIcn("fa-spotify")
      val icn_square = FAIcn("fa-square")
      val icn_square_o = FAIcn("fa-square-o")
      val icn_stack_exchange = FAIcn("fa-stack-exchange")
      val icn_stack_overflow = FAIcn("fa-stack-overflow")
      val icn_star = FAIcn("fa-star")
      val icn_star_half = FAIcn("fa-star-half")
      val icn_star_half_empty = FAIcn("fa-star-half-empty")
      val icn_star_half_full = FAIcn("fa-star-half-full")
      val icn_star_half_o = FAIcn("fa-star-half-o")
      val icn_star_o = FAIcn("fa-star-o")
      val icn_steam = FAIcn("fa-steam")
      val icn_steam_square = FAIcn("fa-steam-square")
      val icn_step_backward = FAIcn("fa-step-backward")
      val icn_step_forward = FAIcn("fa-step-forward")
      val icn_stethoscope = FAIcn("fa-stethoscope")
      val icn_stop = FAIcn("fa-stop")
      val icn_street_view = FAIcn("fa-street-view")
      val icn_strikethrough = FAIcn("fa-strikethrough")
      val icn_stumbleupon = FAIcn("fa-stumbleupon")
      val icn_stumbleupon_circle = FAIcn("fa-stumbleupon-circle")
      val icn_subscript = FAIcn("fa-subscript")
      val icn_subway = FAIcn("fa-subway")
      val icn_suitcase = FAIcn("fa-suitcase")
      val icn_sun_o = FAIcn("fa-sun-o")
      val icn_superscript = FAIcn("fa-superscript")
      val icn_support = FAIcn("fa-support")
      val icn_table = FAIcn("fa-table")
      val icn_tablet = FAIcn("fa-tablet")
      val icn_tachometer = FAIcn("fa-tachometer")
      val icn_tag = FAIcn("fa-tag")
      val icn_tags = FAIcn("fa-tags")
      val icn_tasks = FAIcn("fa-tasks")
      val icn_taxi = FAIcn("fa-taxi")
      val icn_tencent_weibo = FAIcn("fa-tencent-weibo")
      val icn_terminal = FAIcn("fa-terminal")
      val icn_text_height = FAIcn("fa-text-height")
      val icn_text_width = FAIcn("fa-text-width")
      val icn_th = FAIcn("fa-th")
      val icn_th_large = FAIcn("fa-th-large")
      val icn_th_list = FAIcn("fa-th-list")
      val icn_thumb_tack = FAIcn("fa-thumb-tack")
      val icn_thumbs_down = FAIcn("fa-thumbs-down")
      val icn_thumbs_o_down = FAIcn("fa-thumbs-o-down")
      val icn_thumbs_o_up = FAIcn("fa-thumbs-o-up")
      val icn_thumbs_up = FAIcn("fa-thumbs-up")
      val icn_ticket = FAIcn("fa-ticket")
      val icn_times = FAIcn("fa-times")
      val icn_times_circle = FAIcn("fa-times-circle")
      val icn_times_circle_o = FAIcn("fa-times-circle-o")
      val icn_tint = FAIcn("fa-tint")
      val icn_toggle_down = FAIcn("fa-toggle-down")
      val icn_toggle_left = FAIcn("fa-toggle-left")
      val icn_toggle_off = FAIcn("fa-toggle-off")
      val icn_toggle_on = FAIcn("fa-toggle-on")
      val icn_toggle_right = FAIcn("fa-toggle-right")
      val icn_toggle_up = FAIcn("fa-toggle-up")
      val icn_train = FAIcn("fa-train")
      val icn_transgender = FAIcn("fa-transgender")
      val icn_transgender_alt = FAIcn("fa-transgender-alt")
      val icn_trash = FAIcn("fa-trash")
      val icn_trash_o = FAIcn("fa-trash-o")
      val icn_tree = FAIcn("fa-tree")
      val icn_trello = FAIcn("fa-trello")
      val icn_trophy = FAIcn("fa-trophy")
      val icn_truck = FAIcn("fa-truck")
      val icn_try = FAIcn("fa-try")
      val icn_tty = FAIcn("fa-tty")
      val icn_tumblr = FAIcn("fa-tumblr")
      val icn_tumblr_square = FAIcn("fa-tumblr-square")
      val icn_turkish_lira = FAIcn("fa-turkish-lira")
      val icn_twitch = FAIcn("fa-twitch")
      val icn_twitter = FAIcn("fa-twitter")
      val icn_twitter_square = FAIcn("fa-twitter-square")
      val icn_umbrella = FAIcn("fa-umbrella")
      val icn_underline = FAIcn("fa-underline")
      val icn_undo = FAIcn("fa-undo")
      val icn_university = FAIcn("fa-university")
      val icn_unlink = FAIcn("fa-unlink")
      val icn_unlock = FAIcn("fa-unlock")
      val icn_unlock_alt = FAIcn("fa-unlock-alt")
      val icn_unsorted = FAIcn("fa-unsorted")
      val icn_upload = FAIcn("fa-upload")
      val icn_usd = FAIcn("fa-usd")
      val icn_user = FAIcn("fa-user")
      val icn_user_md = FAIcn("fa-user-md")
      val icn_user_plus = FAIcn("fa-user-plus")
      val icn_user_secret = FAIcn("fa-user-secret")
      val icn_user_times = FAIcn("fa-user-times")
      val icn_users = FAIcn("fa-users")
      val icn_venus = FAIcn("fa-venus")
      val icn_venus_double = FAIcn("fa-venus-double")
      val icn_venus_mars = FAIcn("fa-venus-mars")
      val icn_viacoin = FAIcn("fa-viacoin")
      val icn_video_camera = FAIcn("fa-video-camera")
      val icn_vimeo_square = FAIcn("fa-vimeo-square")
      val icn_vine = FAIcn("fa-vine")
      val icn_vk = FAIcn("fa-vk")
      val icn_volume_down = FAIcn("fa-volume-down")
      val icn_volume_off = FAIcn("fa-volume-off")
      val icn_volume_up = FAIcn("fa-volume-up")
      val icn_warning = FAIcn("fa-warning")
      val icn_wechat = FAIcn("fa-wechat")
      val icn_weibo = FAIcn("fa-weibo")
      val icn_weixin = FAIcn("fa-weixin")
      val icn_whatsapp = FAIcn("fa-whatsapp")
      val icn_wheelchair = FAIcn("fa-wheelchair")
      val icn_wifi = FAIcn("fa-wifi")
      val icn_windows = FAIcn("fa-windows")
      val icn_won = FAIcn("fa-won")
      val icn_wordpress = FAIcn("fa-wordpress")
      val icn_wrench = FAIcn("fa-wrench")
      val icn_xing = FAIcn("fa-xing")
      val icn_xing_square = FAIcn("fa-xing-square")
      val icn_yahoo = FAIcn("fa-yahoo")
      val icn_yelp = FAIcn("fa-yelp")
      val icn_yen = FAIcn("fa-yen")
      val icn_youtube = FAIcn("fa-youtube")
      val icn_youtube_play = FAIcn("fa-youtube-play")
      val icn_youtube_square = FAIcn("fa-youtube-square")
    }

  }


  trait SGrid {

    def Row(col: NodeSeq*): NodeSeq = <div class="row">{col.reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)}</div>

    def Row(style: String = "")(col: NodeSeq*): NodeSeq = <div class="row" style={style}>{col.reduceOption(_ ++ _).getOrElse(NodeSeq.Empty)}</div>


    case class Col(s: String, ns: Col => NodeSeq = _ => NodeSeq.Empty, id: String = Helpers.nextFuncName, style: String = "") {

      def xs1: Col = copy(s = s + " col-xs-1 ")
      def xs2: Col = copy(s = s + " col-xs-2 ")
      def xs3: Col = copy(s = s + " col-xs-3 ")
      def xs4: Col = copy(s = s + " col-xs-4 ")
      def xs5: Col = copy(s = s + " col-xs-5 ")
      def xs6: Col = copy(s = s + " col-xs-6 ")
      def xs7: Col = copy(s = s + " col-xs-7 ")
      def xs8: Col = copy(s = s + " col-xs-8 ")
      def xs9: Col = copy(s = s + " col-xs-9 ")
      def xs10: Col = copy(s = s + " col-xs-10 ")
      def xs11: Col = copy(s = s + " col-xs-11 ")
      def xs12: Col = copy(s = s + " col-xs-12 ")

      def sm1: Col = copy(s = s + " col-sm-1 ")
      def sm2: Col = copy(s = s + " col-sm-2 ")
      def sm3: Col = copy(s = s + " col-sm-3 ")
      def sm4: Col = copy(s = s + " col-sm-4 ")
      def sm5: Col = copy(s = s + " col-sm-5 ")
      def sm6: Col = copy(s = s + " col-sm-6 ")
      def sm7: Col = copy(s = s + " col-sm-7 ")
      def sm8: Col = copy(s = s + " col-sm-8 ")
      def sm9: Col = copy(s = s + " col-sm-9 ")
      def sm10: Col = copy(s = s + " col-sm-10 ")
      def sm11: Col = copy(s = s + " col-sm-11 ")
      def sm12: Col = copy(s = s + " col-sm-12 ")

      def md1: Col = copy(s = s + " col-md-1 ")
      def md2: Col = copy(s = s + " col-md-2 ")
      def md3: Col = copy(s = s + " col-md-3 ")
      def md4: Col = copy(s = s + " col-md-4 ")
      def md5: Col = copy(s = s + " col-md-5 ")
      def md6: Col = copy(s = s + " col-md-6 ")
      def md7: Col = copy(s = s + " col-md-7 ")
      def md8: Col = copy(s = s + " col-md-8 ")
      def md9: Col = copy(s = s + " col-md-9 ")
      def md10: Col = copy(s = s + " col-md-10 ")
      def md11: Col = copy(s = s + " col-md-11 ")
      def md12: Col = copy(s = s + " col-md-12 ")

      def lg1: Col = copy(s = s + " col-lg-1 ")
      def lg2: Col = copy(s = s + " col-lg-2 ")
      def lg3: Col = copy(s = s + " col-lg-3 ")
      def lg4: Col = copy(s = s + " col-lg-4 ")
      def lg5: Col = copy(s = s + " col-lg-5 ")
      def lg6: Col = copy(s = s + " col-lg-6 ")
      def lg7: Col = copy(s = s + " col-lg-7 ")
      def lg8: Col = copy(s = s + " col-lg-8 ")
      def lg9: Col = copy(s = s + " col-lg-9 ")
      def lg10: Col = copy(s = s + " col-lg-10 ")
      def lg11: Col = copy(s = s + " col-lg-11 ")
      def lg12: Col = copy(s = s + " col-lg-12 ")

      def xs_offset1: Col = copy(s = s + "col-xs-offset-1 ")
      def xs_offset2: Col = copy(s = s + "col-xs-offset-2 ")
      def xs_offset3: Col = copy(s = s + "col-xs-offset-3 ")
      def xs_offset4: Col = copy(s = s + "col-xs-offset-4 ")
      def xs_offset5: Col = copy(s = s + "col-xs-offset-5 ")
      def xs_offset6: Col = copy(s = s + "col-xs-offset-6 ")
      def xs_offset7: Col = copy(s = s + "col-xs-offset-7 ")
      def xs_offset8: Col = copy(s = s + "col-xs-offset-8 ")
      def xs_offset9: Col = copy(s = s + "col-xs-offset-9 ")
      def xs_offset10: Col = copy(s = s + "col-xs-offset-10 ")
      def xs_offset11: Col = copy(s = s + "col-xs-offset-11 ")
      def xs_offset12: Col = copy(s = s + "col-xs-offset-12 ")

      def sm_offset1: Col = copy(s = s + "col-sm-offset-1 ")
      def sm_offset2: Col = copy(s = s + "col-sm-offset-2 ")
      def sm_offset3: Col = copy(s = s + "col-sm-offset-3 ")
      def sm_offset4: Col = copy(s = s + "col-sm-offset-4 ")
      def sm_offset5: Col = copy(s = s + "col-sm-offset-5 ")
      def sm_offset6: Col = copy(s = s + "col-sm-offset-6 ")
      def sm_offset7: Col = copy(s = s + "col-sm-offset-7 ")
      def sm_offset8: Col = copy(s = s + "col-sm-offset-8 ")
      def sm_offset9: Col = copy(s = s + "col-sm-offset-9 ")
      def sm_offset10: Col = copy(s = s + "col-sm-offset-10 ")
      def sm_offset11: Col = copy(s = s + "col-sm-offset-11 ")
      def sm_offset12: Col = copy(s = s + "col-sm-offset-12 ")

      def md_offset1: Col = copy(s = s + "col-md-offset-1 ")
      def md_offset2: Col = copy(s = s + "col-md-offset-2 ")
      def md_offset3: Col = copy(s = s + "col-md-offset-3 ")
      def md_offset4: Col = copy(s = s + "col-md-offset-4 ")
      def md_offset5: Col = copy(s = s + "col-md-offset-5 ")
      def md_offset6: Col = copy(s = s + "col-md-offset-6 ")
      def md_offset7: Col = copy(s = s + "col-md-offset-7 ")
      def md_offset8: Col = copy(s = s + "col-md-offset-8 ")
      def md_offset9: Col = copy(s = s + "col-md-offset-9 ")
      def md_offset10: Col = copy(s = s + "col-md-offset-10 ")
      def md_offset11: Col = copy(s = s + "col-md-offset-11 ")
      def md_offset12: Col = copy(s = s + "col-md-offset-12 ")

      def lg_offset1: Col = copy(s = s + "col-lg-offset-1 ")
      def lg_offset2: Col = copy(s = s + "col-lg-offset-2 ")
      def lg_offset3: Col = copy(s = s + "col-lg-offset-3 ")
      def lg_offset4: Col = copy(s = s + "col-lg-offset-4 ")
      def lg_offset5: Col = copy(s = s + "col-lg-offset-5 ")
      def lg_offset6: Col = copy(s = s + "col-lg-offset-6 ")
      def lg_offset7: Col = copy(s = s + "col-lg-offset-7 ")
      def lg_offset8: Col = copy(s = s + "col-lg-offset-8 ")
      def lg_offset9: Col = copy(s = s + "col-lg-offset-9 ")
      def lg_offset10: Col = copy(s = s + "col-lg-offset-10 ")
      def lg_offset11: Col = copy(s = s + "col-lg-offset-11 ")
      def lg_offset12: Col = copy(s = s + "col-lg-offset-12 ")

      def rendered = <div id={id} class={s}>{ns(this)}</div>

      def rerender() = Replace(id, rendered)

      def withContents(contents: => NodeSeq): Col = copy(ns = _ => contents)
      def withContents(contents: Col => NodeSeq): Col = copy(ns = contents)

      def apply(contents: => NodeSeq): NodeSeq = copy(ns = _ => contents).rendered
      def apply(contents: Col => NodeSeq): NodeSeq = copy(ns = contents).rendered
      def hide(): JsCmd = Run(s"$$('#$id').hide()")
      def show(): JsCmd = Run(s"$$('#$id').show()")

      def hidden = copy(style = "display: none;")

      def updateSize(): JsCmd = Run(s"$$('#$id').attr('class', ${s.encJs})")

    }

    object Col {
      def apply() = new Col("", _ => NodeSeq.Empty, Helpers.nextFuncName)
    }

  }


  trait SSyntaxHighlighter {

    case class Highlight(
                          code: String,
                          autoLinks: Option[Boolean] = None,
                          className: Option[String] = None,
                          collapse: Option[Boolean] = None,
                          firstLine: Option[Int] = None,
                          highlight: Option[Array[Int]] = None,
                          gutter: Option[Boolean] = None,
                          htmlScript: Option[Boolean] = None,
                          smartTabs: Option[Boolean] = None,
                          toolSize: Option[Int] = None,
                          toolBar: Option[Boolean] = None
                          ) {

      def autoLinks(autoLinks_value: Boolean): Highlight = copy(autoLinks = Some(autoLinks_value))
      def className(className_value: String): Highlight = copy(className = Some(className_value))
      def collapse(collapse_value: Boolean): Highlight = copy(collapse = Some(collapse_value))
      def firstLine(firstLine_value: Int): Highlight = copy(firstLine = Some(firstLine_value))
      def highlight(highlight_value: Array[Int]): Highlight = copy(highlight = Some(highlight_value))
      def gutter(gutter_value: Boolean): Highlight = copy(gutter = Some(gutter_value))
      def htmlScript(htmlScript_value: Boolean): Highlight = copy(htmlScript = Some(htmlScript_value))
      def smartTabs(smartTabs_value: Boolean): Highlight = copy(smartTabs = Some(smartTabs_value))
      def toolSize(toolSize_value: Int): Highlight = copy(toolSize = Some(toolSize_value))
      def toolBar(toolbar_value: Boolean): Highlight = copy(toolBar = Some(toolbar_value))

      def show(): String = {
        val options = List(
          autoLinks.map(v => "auto-links: " + v)
          , className.map(v => "class-name: " + v.toString)
          , collapse.map(v => "collapse: " + v)
          , firstLine.map(v => "first-line: " + v)
          , highlight.map(v => "highlight: " + v)
          , gutter.map(v => "gutter: " + v)
          , htmlScript.map(v => "html-scriptv: " + v)
          , smartTabs.map(v => "smart-tabs" + v)
          , toolSize.map(v => "tool-size: " + v)
          , toolBar.map(v => "toolbar: " + v)
        ).flatten.reduceOption(_ + ",\n" + _).getOrElse("")
        s"""brush: scala; $options;"""
      }


      def render(): NodeSeq = {
        <script type="syntaxhighlighter" class={show()}>{scala.xml.PCData(code)}</script> ++
          Tail.render(Script(OnLoad(Run("SyntaxHighlighter.highlight();"))))
      }
    }

  }

  trait SKnob {

    case class Knob(
                     _lblOpt: Option[String],
                     _min: Integer,
                     _max: Integer,
                     _step: Integer,
                     _angleOffset: Integer,
                     _angleArc: Integer,
                     _stopper: Boolean,
                     _readOnly: Boolean,
                     _clockwise: Boolean,
                     _tickness: Double,
                     _lineCapButt: Boolean,
                     _width: Integer,
                     _height: Integer,
                     _displayInput: Boolean,
                     _displayPrevious: Boolean,
                     _fgColor: String,
                     _inputColor: String,
                     _bgColor: String,
                     _initialValue: Integer,
                     // Events:
                     _onRelease: AnonFunc,
                     _onReleaseAjax: Option[(Int) => JsCmd],
                     _onChange: AnonFunc,
                     _onChangeAjax: Option[(Int) => JsCmd],
                     _onDraw: JsCmd,
                     _onCancel: AnonFunc,
                     _onCancelAjax: Option[(Int) => JsCmd],
                     _formatFunction: AnonFunc
                     )(implicit xsh: XSPageHandle) {

      def lbl(lbl: String): Knob = copy(_lblOpt = Some(lbl))

      def min(min_value: Integer): Knob = copy(_min = min_value)
      def max(max_value: Integer): Knob = copy(_max = max_value)
      def step(step_value: Integer): Knob = copy(_step = step_value)
      def angleOffset(angleOffset_value: Integer): Knob = copy(_angleOffset = angleOffset_value)
      def angleArc(angleArc_value: Integer): Knob = copy(_angleArc = angleArc_value)
      def stopper(stopper_value: Boolean): Knob = copy(_stopper = stopper_value)
      def readOnly(readOnly_value: Boolean): Knob = copy(_readOnly = readOnly_value)
      def clockwise(clockwise_value: Boolean): Knob = copy(_clockwise = clockwise_value)

      def thickness(tickness_value: Double): Knob = copy(_tickness = tickness_value)
      def lineCapButt(lineCapButt_value: Boolean): Knob = copy(_lineCapButt = lineCapButt_value)
      def width(width_value: Integer): Knob = copy(_width = width_value)
      def height(height_value: Integer): Knob = copy(_height = height_value)
      def displayInput(displayInput_value: Boolean): Knob = copy(_displayInput = displayInput_value)
      def displayPrevious(displayPrevious_value: Boolean): Knob = copy(_displayPrevious = displayPrevious_value)
      def fgColor(fgColor_value: String): Knob = copy(_fgColor = fgColor_value)
      def inputColor(inputColor_value: String): Knob = copy(_inputColor = inputColor_value)
      def bgColor(bgColor_value: String): Knob = copy(_bgColor = bgColor_value)

      def initialValue(initial_value: Integer) = copy(_initialValue = initial_value)

      def onReleaseClientSide(f: AnonFunc) = copy(_onRelease = f)
      def onReleaseAjax(f: (Int) => JsCmd): Knob = copy(_onReleaseAjax = Some(f))

      def onChangeClientSide(f: AnonFunc) = copy(_onChange = f)
      def onChangeAjax(f: (Int) => JsCmd): Knob = copy(_onChangeAjax = Some(f))

      def onDrawClientSide(f: JsCmd) = copy(_onDraw = f)

      def onCancelClientSide(f: AnonFunc) = copy(_onCancel = f)
      def onCancelAjax(f: (Int) => JsCmd): Knob = copy(_onCancelAjax = Some(f))

      def formatFunction(f: AnonFunc) = copy(_onCancel = f)

      lazy val knobId = Helpers.nextFuncName

      def rendered: NodeSeq = {
        <input type="text" id={knobId} class="knob" value={_initialValue.toString}></input>++
          _lblOpt.map(lbl => <div class="knob-label">{lbl}</div>).getOrElse(NodeSeq.Empty) ++
      <tail>{Script(OnLoad(Run(
        s"""$$("#$knobId").knob({
           |  'min':${_min},
           |  'max':${_max},
           |  'step':${_step},
           |  'angleOffset':${_angleOffset},
           |  'angleArc':${_angleArc},
           |  'stopper':${_stopper},
           |  'readOnly':${_readOnly},
           |  'rotation':"${if (_clockwise) "clockwise" else "anticlockwise"}",
           |  'thickness':${_tickness},
           |  'lineCapButt':"${if (_lineCapButt) "butt" else "round"}",
           |  'width':${_width},
           |  'height':${_height},
           |  'displayInput':${_displayInput},
           |  'displayPrevious':${_displayPrevious},
           |  'fgColor':${_fgColor.encJs},
           |  'inputColor':${_inputColor.encJs},
           |  'bgColor':${_bgColor.encJs},
           |
           |  'release': function(v) { (${_onRelease.toJsCmd})(v); ${_onReleaseAjax.map(_onReleaseAjax => xsh.ajaxCall(JsRaw("v"), str => _onReleaseAjax(str.toInt)).toJsCmd).getOrElse("")}},
           |  'change': function(v) { (${_onChange.toJsCmd})(v); ${_onChangeAjax.map(_onChangeAjax => xsh.ajaxCall(JsRaw("v"), str => _onChangeAjax(str.toInt)).toJsCmd).getOrElse("")}},
           |  'draw': function() { ${_onDraw.toJsCmd} },
           |  'cancel': function() { (${_onCancel.toJsCmd})(v); ${_onCancelAjax.map(_onCancelAjax => xsh.ajaxCall(JsRaw("v"), str => _onCancelAjax(str.toInt)).toJsCmd).getOrElse("")}},
           |  'format': ${_formatFunction.toJsCmd}
           |});
           """.stripMargin
      )))}</tail>
      }
    }

    object Knob {
      def apply()(implicit xsh: XSPageHandle) = new Knob(
        _min = 0,
        _max = 100,
        _step = 1,
        _angleOffset = 0,
        _angleArc = 360,
        _stopper = false,
        _readOnly = false,
        _clockwise = true,
        _tickness = 0.33,
        _lineCapButt = true,
        _width = 90,
        _height = 90,
        _displayInput = true,
        _displayPrevious = false,
        _fgColor = "#3c8dbc",
        _inputColor = "black",
        _bgColor = "#E6E6FA",
        _initialValue = 0,
        _lblOpt = None,
        _onRelease = AnonFunc(),
        _onReleaseAjax = None,
        _onChange = AnonFunc(),
        _onChangeAjax = None,
        _onDraw = JsCmds.Noop,
        _onCancel = AnonFunc(),
        _onCancelAjax = None,
        _formatFunction = AnonFunc("v", Run("return v;"))
      )
    }

  }
  trait SToastr {

    case class Toastr private[theme](
                                      protected val clas: String,
                                      protected val title: String = "",
                                      protected val mesg: String = "",
                                      protected val pos: Option[String] = None,
                                      protected val showEasing: Option[String] = None,
                                      protected val hideEasing: Option[String] = None,
                                      protected val showMethod: Option[String] = None,
                                      protected val hideMethod: Option[String] = None,
                                      protected val _newestOnTop: Option[Boolean] = None,
                                      protected val _preventDuplicates: Option[Boolean] = None,
                                      protected val _closeBtn: Option[Boolean] = None,
                                      protected val _progressBar: Option[Boolean] = None,
                                      protected val duration: Option[Long] = None,
                                      protected val hideDuration: Option[Long] = None,
                                      protected val timeout: Option[Long] = None,
                                      protected val extendedTimeout: Option[Long] = None
                                      ) {

      def withTitle(title: String): Toastr = copy(title = title)

      def withMesg(s: String): Toastr = copy(mesg = s)

      def withPosTopRight: Toastr = copy(pos = Some("toast-top-right"))
      def withPosBottomRight: Toastr = copy(pos = Some("toast-bottom-right"))
      def withPosBottomLeft: Toastr = copy(pos = Some("toast-bottom-left"))
      def withPosTopLeft: Toastr = copy(pos = Some("toast-top-left"))
      def withPosTopFullWidth: Toastr = copy(pos = Some("toast-top-full-width"))
      def withPosBottomFullWidth: Toastr = copy(pos = Some("toast-bottom-full-width"))
      def withPosTopCenter: Toastr = copy(pos = Some("toast-top-center"))
      def withPosBottomCenter: Toastr = copy(pos = Some("toast-bottom-center"))


      def closeBtn(v: Boolean): Toastr = copy(_closeBtn = Some(v))
      def progressBar(v: Boolean): Toastr = copy(_progressBar = Some(v))
      def newestOnTop(v: Boolean): Toastr = copy(_newestOnTop = Some(v))
      def preventDuplicates(v: Boolean): Toastr = copy(_preventDuplicates = Some(v))

      def withDuration(v: Long): Toastr = copy(duration = Some(v))
      def withTimeout(v: Long): Toastr = copy(timeout = Some(v))
      def withHideDuration(v: Long): Toastr = copy(hideDuration = Some(v))
      def withExtendedTimeout(v: Long): Toastr = copy(extendedTimeout = Some(v))

      def showEasingLinear: Toastr = copy(showEasing = Some("linear"))
      def showEasingSwing: Toastr = copy(showEasing = Some("swing"))

      def hideEasingLinear: Toastr = copy(hideEasing = Some("linear"))
      def hideEasingSwing: Toastr = copy(hideEasing = Some("swing"))

      def showMethodFadeIn: Toastr = copy(showMethod = Some("fadeIn"))
      def showMethodSlideDown: Toastr = copy(showMethod = Some("slideDown"))
      def showMethodShow: Toastr = copy(showMethod = Some("show"))

      def hideMethodFadeOut: Toastr = copy(hideMethod = Some("fadeOut"))
      def hideMethodSlideUp: Toastr = copy(hideMethod = Some("slideUp"))
      def hideMethodShow: Toastr = copy(hideMethod = Some("hide"))

      def show(): JsCmd = {
        Run({
          val options = List(
            _newestOnTop.map(v => "newestOnTop: " + v)
            , _closeBtn.map(v => "closeButton: " + v)
            , _progressBar.map(v => "progressBar: " + v)
            , pos.map(v => "positionClass: " + v.toString.encJs)
            , _preventDuplicates.map(v => "preventDuplicates: " + v)
            , duration.map(v => "showDuration: " + v)
            , hideDuration.map(v => "hideDuration: " + v)
            , timeout.map(v => "timeOut: " + v)
            , extendedTimeout.map(v => "extendedTimeOut: " + v)
            , showEasing.map(v => "showEasing: " + v.toString.encJs)
            , hideEasing.map(v => "hideEasing: " + v.toString.encJs)
            , showMethod.map(v => "showMethod: " + v.toString.encJs)
            , hideMethod.map(v => "hideMethod: " + v.toString.encJs)
          ).flatten.reduceOption(_ + ",\n" + _).getOrElse("")

          s"""toastr.options = {$options}; toastr[${clas.encJs}](${mesg.encJs},${title.encJs});"""
        })
      }
    }

    object Toastr {
      def Info = Toastr("info")
      def Info(title: String, mesg: String) = Toastr("info", title, mesg)

      def Warning = Toastr("warning")
      def Warning(title: String, mesg: String) = Toastr("warning", title, mesg)

      def Success = Toastr("success")
      def Success(title: String, mesg: String) = Toastr("success", title, mesg)

      def Error = Toastr("error")
      def Error(title: String, mesg: String) = Toastr("error", title, mesg)

    }

  }

  trait SModularTables {

    trait Table extends Id {

      val stopPropagation = Run("""; var e = e ? e : window.event; if (typeof e.stopPropagation != "undefined") { e.stopPropagation(); } else if (typeof e.cancelBubble != "undefined") { e.cancelBubble = true; } ;""")

      implicit def xsh: XSPageHandle

      /** Row Type */
      type R
      /** Col Type */
      type C

      // ================================= COLUMNS: =================================
      def thClasses(col: C, colId: String, colIdx: Int): List[String] = Nil
      def thStyle(col: C, colId: String, colIdx: Int): List[String] = Nil

      def tdClasses(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): List[String] = Nil
      def tdStyle(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): List[String] = Nil

      def colTransformTh(col: C, colId: String, colIdx: Int): NodeSeq => NodeSeq =
        "th [class+]" #> thClasses(col, colId, colIdx).mkString(" ") &
          "th [col-table]" #> id('table) &
          "th [style+]" #> thStyle(col, colId, colIdx).mkString(";", ";", ";")

      def colTransformFootTh(col: C, colId: String, colIdx: Int): NodeSeq => NodeSeq = colTransformTh(col, colId, colIdx)

      def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): NodeSeq => NodeSeq =
        "td [class+]" #> tdClasses(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols).mkString(" ") &
          "td [style+]" #> tdStyle(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols).mkString(";", ";", ";") &
          "td [id]" #> colId

      def colIdFor(col: C): String

      // ================================= END OF COLUMNS =================================


      def columns(): Seq[C]
      def rows(): Seq[R]

      def table = this

      val wrapperId = id('wrapper)
      val tableId = id('table)
      val theadId = id('thead)
      val tbodyId = id('tbody)
      val tfootId = id('tfoot)

      def tableClasses: List[String] = Nil

      val template: NodeSeq

      def keepClasses: List[String] = Nil

      def transformTr(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): NodeSeq => NodeSeq = {
        "tr [id]" #> rowId andThen
          "td" #> _cols.zipWithIndex.map({ case ((colId, col), colIdx) => colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) })
      }

      def transformHeadTr(_cols: Seq[(String, C)]): NodeSeq => NodeSeq = {
        "th" #> _cols.zipWithIndex.map({ case ((colId, col), colIdx) => colTransformTh(col, colId, colIdx) })
      }

      def transformFootTr(_cols: Seq[(String, C)]): NodeSeq => NodeSeq = {
        "th" #> _cols.zipWithIndex.map({ case ((colId, col), colIdx) => colTransformFootTh(col, colId, colIdx) })
      }

      def transformPage(_rows: Seq[(String, R)], _cols: Seq[(String, C)]): NodeSeq => NodeSeq = {
        ((ns: NodeSeq) =>
          (".mdl-clearable" #> ClearNodes).apply(
            (keepClasses.map(clas => s".$clas [class!]" #> "mdl-clearable").reduceOption(_ & _).getOrElse(PassThru)).apply(ns)
          )) andThen
          ".mdl-wrapper [id]" #> wrapperId &
            "table [id]" #> tableId &
            "thead [id]" #> theadId &
            "tbody [id]" #> tbodyId &
            "tfoot [id]" #> tfootId andThen
          "thead tr" #> transformHeadTr(_cols) andThen
          "tbody tr" #> _rows.zipWithIndex.map({ case ((rowId, row), rowIdx) => transformTr(row, rowId, rowIdx, _rows, _cols) }) andThen
          "tfoot tr" #> transformFootTr(_cols)
      }

      def transformPage(rows: Seq[R], cols: Seq[C], _arg3: Unit = Unit): NodeSeq => NodeSeq = {
        val _rows = rows.zipAll(Nil, null.asInstanceOf[R], Helpers.nextFuncName).zipWithIndex.map(e => ((e._1._2 + e._2), e._1._1))
        val _cols = cols.zipAll(Nil, null.asInstanceOf[C], Helpers.nextFuncName).zipWithIndex.map(e => ((e._1._2 + e._2), e._1._1))
        transformPage(_rows, _cols)
      }

      def transformPage(rows: Seq[R]): NodeSeq => NodeSeq = transformPage(rows, columns())

      def transformPage(): NodeSeq => NodeSeq = transformPage(rows())

      def renderedTable: NodeSeq = transformPage.apply(template)

      def onRerenderTable(): JsCmd = Noop

      def rerenderTable(): JsCmd = onRerenderTable() & Replace(wrapperId, renderedTable)

      lazy val props: Props = Props.InMemory
    }

    trait SortableTable extends Table {

      private val p = props / "sortable"

      def sortableDefaultSortCol(): Option[C]
      def sortableDefaultSortAsc(col: C) = true
      def sortableIsSortable(col: C) = true

      val sortableSortColId = p.CusOpt[String]("sortColId", sortableDefaultSortCol().map(colIdFor(_)), id => Some(id).filter(_ => columns().exists(c => colIdFor(c) == id)), c => c)
      def sortableSortCol: Option[C] = sortableSortColId().flatMap(id => columns().find(c => colIdFor(c) == id))
      val sortableSortAsc = p.Bool("sortAsc", sortableSortCol.map(sortableDefaultSortAsc).getOrElse(true))

      def sortableSortAscClass: String
      def sortableSortDescClass: String
      def sortableSortNeutralClass: String

      override def thClasses(col: C, colId: String, colIdx: Int): List[String] = super.thClasses(col, colId, colIdx) ::: {
        if (sortableSortColId() == Some(colIdFor(col))) List(if (sortableSortAsc()) sortableSortAscClass else sortableSortDescClass) else if (sortableIsSortable(col)) List(sortableSortNeutralClass) else Nil
      }

      override def colTransformTh(col: C, colId: String, colIdx: Int): (NodeSeq) => NodeSeq = super.colTransformTh(col, colId, colIdx) andThen {
        if (sortableIsSortable(col)) {
          "th [onclick]" #> xsh.ajaxInvoke(() => {
            if (sortableSortColId() == Some(colIdFor(col))) {
              sortableSortAsc() = !sortableSortAsc()
            }
            else {
              sortableSortAsc() = sortableDefaultSortAsc(col)
              sortableSortColId() = Some(colIdFor(col))
            }
            rerenderTable()
          })
        } else {
          PassThru
        }
      }
    }

    trait PagTable extends Table {

      def pagBtnsCurrentClass: String
      def pagBtnsDisabledClass: String
      def pagBtnsShowFirstAndLast: Boolean = true
      def pagBtnsShowPrevAndNext: Boolean = true
      def pagHideBtnsForOnePage: Boolean = false
      def pagNBtns = 5
      def pagPageSizes = 3 :: 10 :: 20 :: 40 :: 60 :: 100 :: Nil
      def pagInitPageSize = 3

      override def keepClasses: List[String] = "mdl-pag-keep" :: super.keepClasses

      var pagCurPage = 0
      var pagCurPageSize = pagInitPageSize
      def pagCurOffset = pagCurPage * pagCurPageSize

      def nPages(rowsSize: Long) = math.max(1, math.ceil(rowsSize / pagCurPageSize.toDouble).toInt)

      def firstPage()(implicit xSHandle: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = 0; rerenderTable()}) & Run("return false;")
      def prevPage()(implicit xSHandle: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = math.max(0, pagCurPage - 1); rerenderTable()}) & Run("return false;")
      def toPage(n: Int)(implicit xSHandle: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = n; rerenderTable()}) & Run("return false;")
      def nextPage(rowsSize: Long)(implicit xSHandle: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = math.min(nPages(rowsSize) - 1, pagCurPage + 1); rerenderTable()}) & Run("return false;")
      def lastPage(rowsSize: Long)(implicit xSHandle: XSPageHandle) = xsh.ajaxInvoke(() => {pagCurPage = nPages(rowsSize) - 1; rerenderTable()}) & Run("return false;")

      def currentButtons(rowsSize: Long) = {
        val side = pagNBtns - 1
        val all = ((pagCurPage - side) until pagCurPage) ++ ((pagCurPage + 1) to (pagCurPage + side))
        all.filter(_ >= 0).filter(_ < nPages(rowsSize)).sortBy(n => math.abs(pagCurPage - n)).take(side).:+(pagCurPage).sorted
      }

      def transformPag(rowsSize: Long): NodeSeq => NodeSeq = {
        if (nPages(rowsSize) == 1 && pagHideBtnsForOnePage) ClearNodes
        else {
          (if (!pagBtnsShowFirstAndLast) ".mdl-pag-first" #> ClearNodes & ".mdl-pag-last" #> ClearNodes else PassThru) andThen
            (if (!pagBtnsShowPrevAndNext) ".mdl-pag-prev" #> ClearNodes & ".mdl-pag-next" #> ClearNodes else PassThru) andThen
            ".mdl-pag-first [class+]" #> (if (pagCurPage == 0) pagBtnsDisabledClass else "") &
              ".mdl-pag-first" #> {".mdl-pag-btn [onclick]" #> firstPage()} &
              ".mdl-pag-prev [class+]" #> (if (pagCurPage == 0) pagBtnsDisabledClass else "") &
              ".mdl-pag-prev" #> {".mdl-pag-btn [onclick]" #> prevPage()} &
              ".mdl-pag-next [class+]" #> (if (pagCurPage == nPages(rowsSize) - 1) pagBtnsDisabledClass else "") &
              ".mdl-pag-next" #> {".mdl-pag-btn [onclick]" #> nextPage(rowsSize)} &
              ".mdl-pag-last [class+]" #> (if (pagCurPage == nPages(rowsSize) - 1) pagBtnsDisabledClass else "") &
              ".mdl-pag-last" #> {".mdl-pag-btn [onclick]" #> lastPage(rowsSize)} andThen
            ".mdl-pag-n" #> currentButtons(rowsSize).map(n => {
              ".mdl-pag-n [class+]" #> (if (pagCurPage == n) pagBtnsCurrentClass else "") &
                ".mdl-pag-btn *" #> (n + 1).toString &
                ".mdl-pag-btn [onclick]" #> toPage(n) &
                ".mdl-pag-btn [href]" #> "javascript: void(0)"
            })
        } andThen
          ".mdl-pag-info" #> pagInfo(math.min(rowsSize, (pagCurPage * pagCurPageSize + 1)), math.min(rowsSize, ((pagCurPage + 1) * pagCurPageSize)), rowsSize) andThen
          ".mdl-pag-sizes" #> xsh.ajaxSelectElem[Int](pagPageSizes, Box(pagPageSizes.find(_ == pagCurPageSize)))(s => {pagCurPageSize = s; rerenderTable()})
      }

      def pagInfo(from: Long, to: Long, of: Long): NodeSeq = <span>Showing {from} to {to} of {of} entries</span>

      @scala.deprecated(message = "Use pagRows instead")
      override def rows(): Seq[R] = ???

      def pagRows(pagOffset: Int, pagSize: Int): (Seq[R], Long)

      override def transformPage(): NodeSeq => NodeSeq = {
        val (rows, rowsSize) = pagRows(pagCurOffset, pagCurPageSize)
        transformPage(rows) andThen
          transformPag(rowsSize)
      }
    }

    trait SearchableTable extends Table {

      var searchQuery = Option.empty[String]

      override def keepClasses: List[String] = "mdl-search-keep" :: super.keepClasses

      @scala.deprecated(message = "Use searchRows instead")
      override def rows(): Seq[R] = ???

      def searchRows(query: Option[String]): Seq[R]

      def searchInputAttrs: Seq[ElemAttr] = Nil

      def transformSearch(): NodeSeq => NodeSeq = {
        ".mdl-search-input" #> xsh.ajaxText(searchQuery.getOrElse(""), v => {
          val before = searchQuery
          searchQuery = Some(v.trim).filter(_ != "")
          (if (before != searchQuery) rerenderTable().P else JsCmds.Noop)
        }, searchInputAttrs: _*)
      }

      override def transformPage(): NodeSeq => NodeSeq = {
        val rows = searchRows(searchQuery)
        transformPage(rows) andThen
          transformSearch()
      }
    }

    trait PagSearchableTable extends Table
                                     with PagTable
                                     with SearchableTable {


      @scala.deprecated(message = "Use searchAndPagRows instead")
      override final def pagRows(pagOffset: Int, pagSize: Int): (Seq[R], Long) = ???

      @scala.deprecated(message = "Use searchAndPagRows instead")
      override final def searchRows(query: Option[String]): Seq[R] = ???

      def searchAndPagRows(pagOffset: Int, pagSize: Int, query: Option[String]): (Seq[R], Long)

      override def transformPage(): NodeSeq => NodeSeq = {
        val (rows, rowsSize) = searchAndPagRows(pagCurOffset, pagCurPageSize, searchQuery)
        transformPage(rows) andThen
          transformPag(rowsSize) andThen
          transformSearch()
      }
    }

    trait SelColsTable extends Table {

      override def keepClasses: List[String] = "mdl-selcols-keep" :: super.keepClasses

      def selColsInitialSelection: Set[C]
      lazy val selCols = collection.mutable.Set[C](selColsInitialSelection.toVector: _*)

      def allColumns(): Seq[C]
      override def columns(): Seq[C] = allColumns().filter(selCols.contains)

      def selColsColName(col: C): String

      def selColSelected: NodeSeq
      def selColUnselected: NodeSeq

      override def transformPage(): NodeSeq => NodeSeq = {
        super.transformPage() andThen
          ".mdl-selcols-select" #> {
            ".mdl-selcols-col" #> allColumns().map(col => {
              ".mdl-selcols-btn [onclick]" #> xsh.ajaxInvoke(() => {
                if (selCols.size > 1 || !selCols.contains(col)) {
                  if (selCols.contains(col)) selCols -= col else selCols += col
                  rerenderTable()
                } else JsCmds.Noop
              }) &
                ".mdl-selcols-btn *" #> <span>{if (selCols.contains(col)) selColSelected else selColUnselected} {selColsColName(col)}</span>
            })
          }
      }

      override def colTransformTh(col: C, colId: String, colIdx: Int): (NodeSeq) => NodeSeq =
        super.colTransformTh(col, colId, colIdx) andThen
          ".mdl-selcols-closebtn [onclick]" #> (xsh.ajaxInvoke(() => {if (selCols.size > 1) {selCols -= col; rerenderTable()} else JsCmds.Noop}) & stopPropagation)
    }

    trait ClickableRowsTable extends Table {

      def clickableRowsIsClickable(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): Boolean = true

      def clickableRowsOnClick(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): JsCmd = JsCmds.Noop

      override def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): (NodeSeq) => NodeSeq =
        super.colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen
          (if (clickableRowsIsClickable(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols))
            "td [onclick+]" #> xsh.ajaxInvoke(() => clickableRowsOnClick(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols))
          else PassThru)
    }

    trait RowDetailsTable extends Table {

      def rowDetailsRender(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): NodeSeq

      def rowDetailsOpenRow(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): JsCmd = Run {
        val ns = <tr id={rowId + "-details"} class="mdl-rowdetails-tr"><td colspan={_cols.size + ""}><div style="display:none;" class="mdl-rowdetails-content">{rowDetailsRender(row, rowId, rowIdx, _rows, _cols)}</div></td></tr>
        sel(rowId, s".after(${ns.toString().encJs});") +
          sel(s"$rowId-details .mdl-rowdetails-content", ".slideDown(400);")
      }

      protected def rowDetailsCloseRow(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): JsCmd = Run {
        s"""${sel(s"$rowId-details .mdl-rowdetails-content")}.slideUp(400, function() {${sel(s"$rowId-details")}.remove();});"""
      }
    }

    trait RowDetailsOnClickTable extends Table
                                         with RowDetailsTable
                                         with ClickableRowsTable {

      var rowDetailsOnClickCurOpen: Option[(R, JsCmd)] = None

      override def clickableRowsOnClick(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): JsCmd =
        super.clickableRowsOnClick(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) & {
          rowDetailsOnClickCurOpen match {
            case Some((prev, close)) if prev == row =>
              rowDetailsOnClickCurOpen = None
              close
            case None =>
              rowDetailsOnClickCurOpen = Some((row, rowDetailsCloseRow(row, rowId, rowIdx, _rows, _cols)))
              rowDetailsOpenRow(row, rowId, rowIdx, _rows, _cols)
            case Some((_, close)) =>
              rowDetailsOnClickCurOpen = Some((row, rowDetailsCloseRow(row, rowId, rowIdx, _rows, _cols)))
              close & rowDetailsOpenRow(row, rowId, rowIdx, _rows, _cols)
          }
        }
    }

    trait FixedHeaderTable extends Table {

      case class FixedHeaderOptions(
                                     position: Option[String] = Some("fixed"),
                                     scrollContainer: Option[RawJSON] = None,
                                     headerCellSelector: Option[String] = None,
                                     floatTableClass: Option[String] = None,
                                     floatContainerClass: Option[String] = None,
                                     top: Option[RawJSON] = None,
                                     bottom: Option[RawJSON] = None,
                                     zIndex: Option[Int] = None,
                                     debug: Option[Boolean] = None,
                                     getSizingRow: Option[RawJSON] = None,
                                     copyTableClass: Option[Boolean] = None,
                                     enableAria: Option[Boolean] = None,
                                     autoReflow: Option[Boolean] = None
                                     )

      def fixedHeaderOptions: FixedHeaderOptions = FixedHeaderOptions()

      override def renderedTable: NodeSeq =
        super.renderedTable ++
          Tail.render(Script(OnLoad(Run(s"""$$('#$tableId').floatThead(${ _root_.util.JSON.L.writeValueAsString(fixedHeaderOptions)});"""))))

      override def onRerenderTable(): JsCmd = Run(s"""$$('#$tableId').floatThead('destroy');""") & super.onRerenderTable()
    }

    trait LazyLoadingTable extends Table {

      override def rerenderTable(): JsCmd = Run(s"""$$('#$tableId').floatThead('destroy');""") & super.rerenderTable()

      def lazyLoadingPlaceholder = <div style="text-align:center; margin: 20px 0;"><img src="/static/img/ajax-loader.gif"></img></div>

      override def renderedTable: NodeSeq =
        <div id={wrapperId}>{lazyLoadingPlaceholder}</div> ++
          Tail.render(Script(OnLoad(xsh.ajaxInvoke(() => Replace(wrapperId, super.renderedTable)))))
    }

    trait SortableRowsTable extends Table {

      def sortableRowsIsSortable(row: R): Boolean = true

      def sortableRowsOnSort(row: R, idx: Int): JsCmd = Noop

      override def tableClasses: List[String] = "mdl-sortablerows" :: super.tableClasses

      override def tdClasses(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]) =
        if (sortableRowsIsSortable(row)) super.tdClasses(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols)
        else "mdl-no-rowsort" :: super.tdClasses(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols)

      //    override protected def rowTransforms(row: R, rowId: String, rowIdx: Int, rrdRow: () => JsCmd, rows: Seq[(String, R)]): NodeSeq => NodeSeq =
      //      super.rowTransforms(row, rowId, rowIdx, rrdRow, rows) andThen
      //        "tr [reorder]" #> SHtml.ajaxCall(JsRaw("idx"), idx => {
      //          sortedRow(row, idx.toInt) & rerenderTable()
      //        }).toJsCmd

      override def transformTr(row: R, rowId: String, rowIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): (NodeSeq) => NodeSeq =
        super.transformTr(row, rowId, rowIdx, _rows, _cols) andThen
          "tr [reorder]" #> xsh.ajaxCall(JsRaw("idx"), idx => sortableRowsOnSort(row, idx.toInt))

      def sortableRowsReorderRowMinDistancePx = 15

      override def renderedTable: NodeSeq =
        super.renderedTable ++ Tail.render(Script(Run(
          s"""$$('#$tbodyId').sortable({
           |  axis: 'y',
           |  items: 'tr:not(.mdl-no-rowsort)',
           |  distance: $sortableRowsReorderRowMinDistancePx,
           |  update:
           |    function( event, ui ) {
           |      var sorted =
           |        $$.map(
           |          $$.map($$('#$tbodyId tr'),
           |          function(r) {return {id:$$(r).attr('id'), offset: r.offsetTop};}
           |        )
           |          .sort(function(a,b){return a.offset-b.offset;}), function(r) {return r.id;});
           |      var idx = sorted.indexOf(ui.item.attr('id'));
           |      eval('0, ' + ui.item.attr('reorder'));
           |  },
           |  helper:
           |    function(e, ui) {
           |      ui.children().each(function() { $$(this).width($$(this).width()); });
           |      return ui;
           |    }
           |})""".stripMargin
        )))
    }

    trait SimpleTable
      extends Table
              with SortableTable
              with PagSearchableTable
              with SelColsTable
              with RowDetailsOnClickTable
              with FixedHeaderTable
              with LazyLoadingTable
              with SortableRowsTable {

      val template: NodeSeq = (new Html5Parser {}).parse(play.api.Play.current.resource(s"/public/templates/simple-table.html").get.openStream()).get

      def allRows(): Seq[R]

      def selColSelected: NodeSeq = TH.IcnFA.icn_check_square_o.icn
      def selColUnselected: NodeSeq = TH.IcnFA.icn_square_o.icn

      override def searchAndPagRows(pagOffset: Int, pagSize: Int, query: Option[String]): (Seq[R], Long) = {
        val all: Seq[R] = allRows().filter(r => query.isEmpty || columns().exists(_.matchWith.map(_ (r, query.get)).getOrElse(false)))
        val rows =
          sortableSortCol.map(sortRowsBy => sortRowsBy.sortBy.get(all).|>(rows => if (sortableSortAsc()) rows else rows.reverse)).getOrElse(all)
            .drop(pagOffset).take(pagSize)
        (rows, all.size)
      }

      override def pagBtnsCurrentClass: String = "active"
      override def pagBtnsDisabledClass: String = "disabled"

      override def sortableDefaultSortCol(): Option[Column] = columns().filter(sortableIsSortable).headOption

      override def sortableSortAscClass: String = "sorting_asc"
      override def sortableSortDescClass: String = "sorting_desc"
      override def sortableSortNeutralClass: String = "sorting"

      override def colTransformTh(col: C, colId: String, colIdx: Int): (NodeSeq) => NodeSeq = super.colTransformTh(col, colId, colIdx) andThen col.colRenderHead(colId, colIdx)
      override def colTransformTd(col: C, row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): (NodeSeq) => NodeSeq = super.colTransformTd(col, row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen col.colRenderRow(row, rowId, rowIdx, colId, colIdx, _rows, _cols)

      override def sortableIsSortable(col: C) = col.sortBy.isDefined

      override type C = Column

      override def selColsInitialSelection: Set[Column] = allColumns().toSet
      override def selColsColName(col: Column): String = col.name


      override def colIdFor(col: Column): String = col.name

      trait Column {

        def name: String
        def sortBy: Option[Seq[R] => Seq[R]]
        def matchWith: Option[(R, String) => Boolean]
        def colRenderHead(colId: String, colIdx: Int): (NodeSeq) => NodeSeq = PassThru
        def colRenderFoot(colId: String, colIdx: Int): (NodeSeq) => NodeSeq = PassThru
        def colRenderRow(row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): (NodeSeq) => NodeSeq = PassThru
      }

      def ColStr(colName: String, contentstd: R => (String), sorter: Option[Seq[R] => Seq[R]] = None, matches: Option[(R, String) => Boolean] = None): C = new Column {

        override def name: String = colName

        override def sortBy: Option[Seq[R] => Seq[R]] = sorter

        override def matchWith: Option[(R, String) => Boolean] = matches

        override def colRenderRow(row: R, rowId: String, rowIdx: Int, colId: String, colIdx: Int, _rows: Seq[(String, R)], _cols: Seq[(String, C)]): (NodeSeq) => NodeSeq =
          super.colRenderRow(row, rowId, rowIdx, colId, colIdx, _rows, _cols) andThen
            "td *" #> contentstd(row)

        override def colRenderHead(colId: String, colIdx: Int): (NodeSeq) => NodeSeq =
          super.colRenderHead(colId, colIdx) andThen
            ".mdl-th-title" #> name

        override def colRenderFoot(colId: String, colIdx: Int): (NodeSeq) => NodeSeq =
          super.colRenderFoot(colId, colIdx) andThen
            ".mdl-th-title" #> name
        override def toString: String = name
      }
    }

  }

  trait SModals extends Id {


    object ModalStyle extends Enumeration {
      type Style = String

      val Default: Style = ""
      val Primary: Style = "modal-primary"
      val Info: Style = "modal-info"
      val Warning: Style = "modal-warning"
      val Success: Style = "modal-success"
      val Danger: Style = "modal-danger"
    }

    trait Modal extends Id {

      implicit val xsh: XSPageHandle

      val modalId = id('modal)
      val headerId = id('header)
      val bodyId = id('body)
      val footerId = id('footer)

      // Global:
      def modalStyle: ModalStyle.Style
      def modalClasses: List[String] = (if (modalFullscreen) List("fullscreen") else Nil) ::: List("modal", modalStyle + "")
      def modalFullscreen: Boolean = false
      def modalCloseOnEsc: Boolean = true
      def modalCloseOnClickOutside: Boolean = true
      // Header:
      def modalEnableCloseBtn: Boolean = true
      def modalEnableTopRightCloseBtn: Boolean = true
      def modalTitleStr: String
      def modalTitleNs: NodeSeq = scala.xml.Text(modalTitleStr)
      // Body:
      def modalContents: NodeSeq
      // Footer:
      def modalCloseBtnLbl: String = "Close"
      def modalSaveBtnLbl: String = "Save"
      def modalCloseBtn: NodeSeq = if (modalEnableCloseBtn) <button type="button" onclick={modalOnCloseClientSide.toJsCmd} class="btn btn-default pull-left">{modalCloseBtnLbl}</button> else NodeSeq.Empty
      def modalSaveBtn: NodeSeq = <button type="button" onclick={modalOnSaveClientSide.toJsCmd} class="btn btn-primary">{modalSaveBtnLbl}</button>

      def modalOnCloseClientSide: JsCmd = hideAndDestroy()
      def modalOnSaveServerSide(): JsCmd = JsCmds.Noop
      def modalOnSaveClientSide: JsCmd = xsh.ajaxInvoke(() => modalOnSaveServerSide())

      def renderedHeader: NodeSeq = {
      <div id={headerId} class="modal-header">
        {if (modalEnableTopRightCloseBtn) <button type="button" class="close" onclick={modalOnCloseClientSide.toJsCmd} aria-label="Close"><span aria-hidden="true">×</span></button> else NodeSeq.Empty}
        <h4 class="modal-title">{modalTitleNs}</h4>
      </div>
    }

      def rerenderHeader(): JsCmd = Replace(headerId, renderedHeader)

      def renderedBody: NodeSeq = {
      <div id={bodyId} class="modal-body">
        {modalContents}
      </div>
    }

      def rerenderBody(): JsCmd = Replace(bodyId, renderedBody)

      def renderedFooter: NodeSeq = {
      <div id={footerId} class="modal-footer">
        {modalCloseBtn}
        {modalSaveBtn}
      </div>
    }

      def rerenderFooter(): JsCmd = Replace(footerId, renderedFooter)

      def renderedModal: NodeSeq = {
      <div id={modalId} class={modalClasses.mkString(" ")} tabindex="-1">
        <div class="modal-dialog">
          <div class="modal-content">
            {renderedHeader}
            {renderedBody}
            {renderedFooter}
          </div>
        </div>
      </div>
    }

      def rerenderModal(): JsCmd = Replace(modalId, renderedModal)

      def showAndInstall(): JsCmd = Run(s"""$$('body').append(${renderedModal.toString().encJs});""") & showOnly()

      def showOnly(): JsCmd = Run(s"""$$("#$modalId").modal({backdrop: ${if (modalCloseOnClickOutside) modalCloseOnClickOutside else s"'static'"}, keyboard: $modalCloseOnEsc});""")

      def hideAndDestroy(): JsCmd = Run(s"""$$("#$modalId").modal('hide'); $$("#$modalId").on('hidden.bs.modal', function () { $$("#$modalId").remove(); });""")

      def hideOnly(): JsCmd = Run(s"""$$("#$modalId").modal('hide');""")
    }

    abstract class DefaultModal()(implicit val xsh: XSPageHandle) extends Modal

    case class SimpleModal(
                            modalTitleStr: String,
                            modalContentsF: (SimpleModal, XSPageHandle) => NodeSeq,
                            modalStyle: ModalStyle.Style = ModalStyle.Default,
                            override val modalCloseBtnLbl: String = "Close",
                            override val modalSaveBtnLbl: String = "Close",
                            override val modalEnableCloseBtn: Boolean = false,
                            override val modalEnableTopRightCloseBtn: Boolean = true,
                            modalOnSaveServerSideF: (SimpleModal, XSPageHandle) => JsCmd = (_, _) => JsCmds.Noop,
                            modalOnSaveClientSideF: (SimpleModal, XSPageHandle) => JsCmd = (modal, xsh) => xsh.ajaxInvoke(() => modal.modalOnSaveServerSide())
                            )(implicit val xsh: XSPageHandle) extends Modal {

      override def modalContents: NodeSeq = modalContentsF(this, xsh)

      override def modalOnSaveServerSide(): JsCmd = modalOnSaveServerSideF(this, xsh)
      override def modalOnSaveClientSide: JsCmd = modalOnSaveClientSideF(this, xsh)
    }
  }

  trait SDashborardWidgets extends SBase {


    case class DashboardWidgetSmall(
                                     icn: TH.Icon,
                                     topText: String = "",
                                     mainText: String = "",
                                     progressBar: Option[(String, Double)] = None,
                                     fillColor: Boolean = false,
                                     styleClass: String = ""
                                     ) extends Id {

      val widgetId = id('widget)

      def rerender() = SetHtml(widgetId, rendered)

      def styleAqua = copy(styleClass = "bg-aqua")
      def styleGreen = copy(styleClass = "bg-green")
      def styleYellow = copy(styleClass = "bg-yellow")
      def styleRed = copy(styleClass = "bg-red")

      def rendered: NodeSeq = {
        <div id={widgetId} class={"info-box " + (if (fillColor) styleClass else "")}>
          <div class={"info-box-icon " + (if (!fillColor) styleClass else "")}><i class={icn.clas}></i></div>
           <div class="info-box-content">
             <span class="info-box-text">{topText}</span>
             <span class="info-box-number">{mainText}</span>
             {
               progressBar.map({
                 case (label, percent) =>
                   <div class="progress">
                     <div class="progress-bar" style= {"width:" + percent * 100 + "%;"}></div>
                   </div>
                   <span class="progress-description">{label}</span>
               }).getOrElse(NodeSeq.Empty)
             }
           </div>
        </div>
      }
    }

    case class DashboardWidgetLarge(
                                     icn: TH.Icon,
                                     bottomText: String = "",
                                     mainText: String = "",
                                     refSmallBox: Option[String] = None,
                                     styleClass: String = ""
                                     ) extends Id {
      val widgetId = id('widget)

      def rerender() = SetHtml(widgetId, rendered)

      def styleAqua = copy(styleClass = "bg-aqua")
      def styleGreen = copy(styleClass = "bg-green")
      def styleYellow = copy(styleClass = "bg-yellow")
      def styleRed = copy(styleClass = "bg-red")

      def rendered: NodeSeq = {
       <div id={widgetId} class={"small-box " + styleClass}>
          <div class="inner"><h3>{mainText}</h3><p>{bottomText}</p></div>
          <div class="icon"><i class={icn.clas}></i></div>
          <a href={refSmallBox.getOrElse("#")} class="small-box-footer">
            More info <i class="fa fa-arrow-circle-right"></i>
          </a>
        </div>
    }
    }

  }
  trait SFCharts {

    /**
     * @param options
     * @param data
     * @param style
     * @param plotClick Run on plot click: event, pos, item are available
     * @param plotHover Run on plot hover: event, pos, item are available
     */
    case class FChart(
                       options: FCharts.Options,
                       data: List[FCharts.Data],
                       style: String = "height: 300px;",

                       /**
                        * event, pos, item are available
                        * item: {
                        * datapoint: the point, e.g. [0, 2]
                        * dataIndex: the index of the point in the data array
                        * series: the series object
                        * seriesIndex: the index of the series
                        * pageX, pageY: the global screen coordinates of the point
                        * }
                        */
                       plotClick: JsCmd = JsCmds.Noop,

                       /**
                        * event, pos, item are available
                        * item: {
                        * datapoint: the point, e.g. [0, 2]
                        * dataIndex: the index of the point in the data array
                        * series: the series object
                        * seriesIndex: the index of the series
                        * pageX, pageY: the global screen coordinates of the point
                        * }
                        */
                       plotHover: JsCmd = JsCmds.Noop
                       ) extends Id {

      val chartId = id('chart)
      import util.JSON
      def renderChart(): NodeSeq = {
        implicit val formats = Serialization.formats(NoTypeHints)
        val optionsJson = _root_.util.JSON.L.writeValueAsString(options)
        val dataJson = _root_.util.JSON.L.writeValueAsString(data)
        <div id={chartId} style={style}></div> ++
        <tail>{Script(OnLoad(Run(s"""
          $$.plot('#$chartId', $dataJson, $optionsJson);
          $$('#$chartId').bind("plotclick", function (event, pos, item) { ${plotClick.toJsCmd} });
          $$('#$chartId').bind("plothover", function (event, pos, item) { ${plotHover.toJsCmd} });
          """)))}</tail>
      }
    }

    object FChartsImplicits {
      implicit def toOption[T](v: T): Option[T] = Some(v)
      implicit def toList[T](v: T): List[T] = List(v)
    }

    object FCharts {

      case class Options(
                          grid: Option[Grid] = None,
                          series: Option[Series] = None,
                          xaxis: Option[Axis] = None,
                          yaxis: Option[Axis] = None,
                          xaxes: List[Axis] = Nil,
                          yaxes: List[Axis] = Nil,
                          color: Option[List[String]] = None,
                          interaction: Option[Interaction] = None,
                          legend: Option[Legend] = None
                          )

      case class Interaction(redrawOverlayInterval: Option[Double] = None)

      case class Legend(
                         show: Option[Boolean] = None,
                         noColumns: Option[Int] = None,
                         labelFormatter: Option[RawJSON] = None,
                         labelBoxBorderColor: Option[String] = None,
                         container: Option[RawJSON] = None,
                         position: Option[String] = None,
                         margin: Option[Int] = None,
                         backgroundColor: Option[String] = None,
                         backgroundOpacity: Option[Int] = None,
                         sorted: Option[String] = None
                         )

      case class Range(
                        from: Option[Int] = None,
                        to: Option[Int] = None
                        )

      case class Marking(
                          color: Option[String] = None,
                          lineWidth: Option[Int] = None,
                          xaxis: Option[Range] = None,
                          yaxis: Option[Range] = None
                          )

      case class Grid(
                       borderColor: Option[String] = None,
                       borderWidth: Option[Int] = None,
                       tickColor: Option[String] = None,
                       show: Option[Boolean] = None,
                       aboveData: Option[Boolean] = None,
                       color: Option[String] = None,
                       backgroundColor: Option[String] = None,
                       margin: Option[Int] = None,
                       labelMargin: Option[Int] = None,
                       axisMargin: Option[Int] = None,
                       markingsLineWidth: Option[Int] = None,
                       markingsColor: Option[String] = None,
                       markings: Option[List[Marking]] = None,
                       minBorderMargin: Option[Int] = None,
                       clickable: Option[Boolean] = None,
                       hoverable: Option[Boolean] = None,
                       autoHighlight: Option[Boolean] = None,
                       mouseActiveRadius: Option[Int] = None
                       )

      case class Points(
                         show: Option[Boolean] = None,
                         color: Option[String] = None,
                         lineWidth: Option[Int] = None,
                         fill: Option[Boolean] = None,
                         fillColor: Option[String] = None,
                         radius: Option[Int] = None,
                         symbol: Option[String] = None
                         )

      case class Series(
                         shadowSize: Option[Int] = None,
                         highlightColor: Option[String] = None,
                         lines: Option[Lines] = None,
                         bars: Option[Bars] = None,
                         points: Option[Points] = None,
                         pie: Option[Pie] = None
                         )

      case class Lines(
                        show: Option[Boolean] = None,
                        zero: Option[Boolean] = None,
                        color: Option[String] = None,
                        lineWidth: Option[Int] = None,
                        fill: Option[Boolean] = None,
                        fillColor: Option[String] = None,
                        steps: Option[Boolean] = None
                        )

      case class Bars(
                       show: Option[Boolean] = None,
                       zero: Option[Boolean] = None,
                       color: Option[String] = None,
                       lineWidth: Option[Int] = None,
                       fill: Option[Boolean] = None,
                       fillColor: Option[String] = None,
                       barWidth: Option[Double] = None,
                       align: Option[String] = None,
                       horizontal: Option[Boolean] = None
                       )

      case class Like(
                       size: Option[Int] = None,
                       lineHeight: Option[Int] = None,
                       style: Option[String] = None,
                       weight: Option[String] = None,
                       family: Option[String] = None,
                       variant: Option[String] = None
                       )


      case class MinTickSize(value: Int, unit: String) extends JsonSerializable {override def json(): Option[String] = Some(s"[$value, ${unit.encJs}]")}

      case class Axis(
                       show: Option[Boolean] = None,
                       mode: Option[String] = None,
                       position: Option[String] = None,
                       font: Option[Like] = None,
                       color: Option[String] = None,
                       tickColor: Option[String] = None,
                       transform: Option[RawJSON] = None,
                       inverseTransform: Option[RawJSON] = None,
                       min: Option[Long] = None,
                       max: Option[Long] = None,
                       autoscaleMargin: Option[Double] = None,
                       //ticks: Option[RawJSON] = None, // either [1, 3] or [[1, "a"], 3] or (fn: axis info -> ticks) or app. number of ticks for auto-ticks
                       tickFormatter: Option[RawJSON] = None,
                       labelWidth: Option[Int] = None,
                       labelHeight: Option[Int] = None,
                       reserveSpace: Option[Boolean] = None,
                       tickLength: Option[Int] = None,
                       alignTicksWithAxis: Option[Int] = None,
                       tickDecimals: Option[Double] = None,
                       tickSize: Option[Int] = None,
                       minTickSize: Option[MinTickSize] = None,
                       timeformat: Option[String] = None
                       )

      case class Data(
                       data: List[List[Double]],
                       color: Option[String] = None,
                       label: Option[String] = None,
                       xaxis: Option[Int] = None,
                       yaxis: Option[Int] = None
                       )

      case class Shadow(
                         left: Option[Int] = None,
                         top: Option[Int] = None,
                         alpha: Option[Double] = None
                         )

      case class Pie(
                      show: Option[Boolean] = None,
                      radius: Option[Double] = None,
                      innerRadius: Option[Double] = None,
                      startAngle: Option[Int] = None,
                      tilt: Option[Double] = None,
                      offset: Option[Offset] = None,
                      combine: Option[Combine] = None,
                      stroke: Option[Stroke] = None,
                      label: Option[Label] = None,
                      highlight: Option[Highlight] = None,
                      shadow: Option[Shadow] = None
                      )


      case class Offset(
                         top: Option[Int] = None,
                         left: Option[Int] = None
                         )

      case class Stroke(
                         color: Option[String] = None,
                         width: Option[Int] = None
                         )

      /**
       *
       * @param show
       * @param formatter
       * @param radius radius at which to place the labels (based on full calculated radius if <=1, or hard pixel value)
       * @param background
       * @param threshold
       */
      case class Label(
                        show: Option[Boolean] = None,
                        formatter: Option[RawJSON] = None,
                        radius: Option[Double] = None,
                        background: Option[Background] = None,
                        threshold: Option[Double] = None
                        )

      case class Background(
                             color: Option[String] = None,
                             opacity: Option[Double] = None
                             )

      case class Combine(
                          threshold: Option[Double] = None,
                          color: Option[String] = None,
                          label: Option[String] = None
                          )

      case class Highlight(opacity: Option[Double] = None)

    }

  }

  trait SWysiwyg {


    case class BootstrapWysiwyg(
                                 get: () => String,
                                 fontStyles: Boolean = true,
                                 emphasis: Boolean = true,
                                 lists: Boolean = true,
                                 html: Boolean = true,
                                 link: Boolean = true,
                                 image: Boolean = true,
                                 color: Boolean = true,
                                 style: String = "",
                                 onLoadOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onBeforeloadOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onFocusOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onFocusComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onFocusTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onBlurOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onBlurComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onBlurTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onChangeOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onChangeComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onChangeTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onPasteOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onPasteComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onPasteTextareaOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onNewwordComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onDestroyComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onChange_viewOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onShowDialogOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onSaveDialogOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onCancelDialogOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onUndoComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onRedoComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onBeforecommandComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 onAftercommandComposerOpt: Option[JsCmd] = Some(JsCmds.Noop),
                                 textareaId: String = Helpers.nextFuncName,
                                 attrs: Seq[ElemAttr] = Nil
                                 )(implicit xsh: XSPageHandle) {

      def onLoad(cmd: JsCmd) = copy(onLoadOpt = Some(cmd))
      def onBeforeload(cmd: JsCmd) = copy(onBeforeloadOpt = Some(cmd))
      def onFocus(cmd: JsCmd) = copy(onFocusOpt = Some(cmd))
      def onFocusComposer(cmd: JsCmd) = copy(onFocusComposerOpt = Some(cmd))
      def onFocusTextarea(cmd: JsCmd) = copy(onFocusTextareaOpt = Some(cmd))
      def onBlur(cmd: JsCmd) = copy(onBlurOpt = Some(cmd))
      def onBlurComposer(cmd: JsCmd) = copy(onBlurComposerOpt = Some(cmd))
      def onBlurTextarea(cmd: JsCmd) = copy(onBlurTextareaOpt = Some(cmd))
      def onChange(cmd: JsCmd) = copy(onChangeOpt = Some(cmd))
      def onChangeComposer(cmd: JsCmd) = copy(onChangeComposerOpt = Some(cmd))
      def onChangeTextarea(cmd: JsCmd) = copy(onChangeTextareaOpt = Some(cmd))
      def onPaste(cmd: JsCmd) = copy(onPasteOpt = Some(cmd))
      def onPasteComposer(cmd: JsCmd) = copy(onPasteComposerOpt = Some(cmd))
      def onPasteTextarea(cmd: JsCmd) = copy(onPasteTextareaOpt = Some(cmd))
      def onNewwordComposer(cmd: JsCmd) = copy(onNewwordComposerOpt = Some(cmd))
      def onDestroyComposer(cmd: JsCmd) = copy(onDestroyComposerOpt = Some(cmd))
      def onChange_view(cmd: JsCmd) = copy(onChange_viewOpt = Some(cmd))
      def onShowDialog(cmd: JsCmd) = copy(onShowDialogOpt = Some(cmd))
      def onSaveDialog(cmd: JsCmd) = copy(onSaveDialogOpt = Some(cmd))
      def onCancelDialog(cmd: JsCmd) = copy(onCancelDialogOpt = Some(cmd))
      def onUndoComposer(cmd: JsCmd) = copy(onUndoComposerOpt = Some(cmd))
      def onRedoComposer(cmd: JsCmd) = copy(onRedoComposerOpt = Some(cmd))
      def onBeforecommandComposer(cmd: JsCmd) = copy(onBeforecommandComposerOpt = Some(cmd))
      def onAftercommandComposer(cmd: JsCmd) = copy(onAftercommandComposerOpt = Some(cmd))

      lazy val eventsJson = List(
        onLoadOpt.map(cmd => s""""load": function() { ${cmd.toJsCmd} }""")
        , onBeforeloadOpt.map(cmd => s""""beforeload": function() { ${cmd.toJsCmd} }""")
        , onFocusOpt.map(cmd => s""""focus": function() { ${cmd.toJsCmd} }""")
        , onFocusComposerOpt.map(cmd => s""""focus:composer": function() { ${cmd.toJsCmd} }""")
        , onFocusTextareaOpt.map(cmd => s""""focus:textarea": function() { ${cmd.toJsCmd} }""")
        , onBlurOpt.map(cmd => s""""blur": function() { ${cmd.toJsCmd} }""")
        , onBlurComposerOpt.map(cmd => s""""blur:composer": function() { ${cmd.toJsCmd} }""")
        , onBlurTextareaOpt.map(cmd => s""""blur:textarea": function() { ${cmd.toJsCmd} }""")
        , onChangeOpt.map(cmd => s""""change": function() { ${cmd.toJsCmd} }""")
        , onChangeComposerOpt.map(cmd => s""""change:composer": function() { ${cmd.toJsCmd} }""")
        , onChangeTextareaOpt.map(cmd => s""""change:textarea": function() { ${cmd.toJsCmd} }""")
        , onPasteOpt.map(cmd => s""""paste": function() { ${cmd.toJsCmd} }""")
        , onPasteComposerOpt.map(cmd => s""""paste:composer": function() { ${cmd.toJsCmd} }""")
        , onPasteTextareaOpt.map(cmd => s""""paste:textarea": function() { ${cmd.toJsCmd} }""")
        , onNewwordComposerOpt.map(cmd => s""""newword:composer": function() { ${cmd.toJsCmd} }""")
        , onDestroyComposerOpt.map(cmd => s""""destroy:composer": function() { ${cmd.toJsCmd} }""")
        , onChange_viewOpt.map(cmd => s""""change_view": function() { ${cmd.toJsCmd} }""")
        , onShowDialogOpt.map(cmd => s""""show:dialog": function() { ${cmd.toJsCmd} }""")
        , onSaveDialogOpt.map(cmd => s""""save:dialog": function() { ${cmd.toJsCmd} }""")
        , onCancelDialogOpt.map(cmd => s""""cancel:dialog": function() { ${cmd.toJsCmd} }""")
        , onUndoComposerOpt.map(cmd => s""""undo:composer": function() { ${cmd.toJsCmd} }""")
        , onRedoComposerOpt.map(cmd => s""""redo:composer": function() { ${cmd.toJsCmd} }""")
        , onBeforecommandComposerOpt.map(cmd => s""""beforecommand:composer": function() { ${cmd.toJsCmd} }""")
        , onAftercommandComposerOpt.map(cmd => s""""aftercommand:composer": function() { ${cmd.toJsCmd} }""")
      ).flatten.mkString(",\n")

      def rendered: NodeSeq =
        attrs.foldLeft(<textarea class="textarea" style={style} id={textareaId}>{get()}</textarea>)(_ % _) ++
      <tail>{
        Script(OnLoad(Run(
          s"""$$('#$textareaId').wysihtml5({
             |  "toolbar": {
             |    "font-styles": $fontStyles,
             |    "emphasis": $emphasis,
             |    "lists": $lists,
             |    "html": $html,
             |    "link": $link,
             |    "image": $image,
             |    "color": $color
             |  },
             |  "events": {
             |    $eventsJson
             |  }
             |});
             |""".stripMargin)))
        }</tail>

      def jsValue(): JsExp = JsRaw(s"""$$('#$textareaId').val()""")
    }

  }

  trait SForms extends SBtns {

    abstract class DefaultForm()(implicit val xsh: XSPageHandle) extends Form

    trait Form extends Id {

      implicit val xsh: XSPageHandle

      val field: Field

      var saveFailed = false

      def onChangeClientSide(): JsCmd = JsCmds.Noop

      def onChangeServerSide(f: Field): JsCmd = field.update() & field.onChangedField(f)

      def afterSucessfullSave(): JsCmd = JsCmds.Noop

      def onSave(): JsCmd = {
        val errors = field.errors()
        if (errors.isEmpty) {
          saveFailed = false
          field.doSave() & field.update() & afterSucessfullSave()
        } else {
          saveFailed = true
          field.update()
        }
      }

      def rendered: NodeSeq = {
      <form method="POST" action="javascript:void(0);" role="form">
        {field.rendered}
      </form>
  }

      trait Field {

        val id = Helpers.nextFuncName

        def update(): JsCmd

        def children: Seq[Field] = Nil

        def onChangedField(f: Field): JsCmd = if (deps.contains(f)) Replace(id, rendered) else {children.map(_.onChangedField(f)).reduceOption(_ & _).getOrElse(JsCmds.Noop)}

        def modified: Boolean

        def doSave(): JsCmd

        def errors(): Seq[(Field, String)]

        def rendered: NodeSeq

        def enabled: () => Boolean

        def deps: Seq[Field]
      }

      trait VerticalGroupBase extends Field {

        def fields: Seq[Field]

        override def children: Seq[Field] = fields

        def modified: Boolean = fields.exists(_.modified)

        def errors(): Seq[(Field, String)] = fields.flatMap(_.errors())

        def doSave(): JsCmd = fields.map(_.doSave()).foldLeft[JsCmd](JsCmds.Noop)(_ & _)

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & fields.map(_.update()).reduceOption(_ & _).getOrElse(JsCmds.Noop)

        def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id}>{fields.map(_.rendered).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)}</div>
      }
      }

      case class VerticalGroup(enabled: () => Boolean = () => true, deps: Seq[Field] = Nil)(val fields: Field*) extends VerticalGroupBase

      trait HorizontalGroupBase extends Field {

        def fields: Seq[Field]

        override def children: Seq[Field] = fields

        def modified: Boolean = fields.exists(_.modified)

        def errors(): Seq[(Field, String)] = fields.flatMap(_.errors())

        def doSave(): JsCmd = fields.map(_.doSave()).foldLeft[JsCmd](JsCmds.Noop)(_ & _)

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & fields.map(_.update()).reduceOption(_ & _).getOrElse(JsCmds.Noop)

        def size = "md"

        def rendered: NodeSeq = {
          val width = fields.filter(_.enabled()).size match {
            case 0 => "12"
            case 1 => "12"
            case 2 => "6"
            case 3 => "4"
            case 4 => "3"
          }
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="row">
          {fields.map(f => <div class={s"col-$size-$width"}>{f.rendered}</div>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)}
        </div>
      }
      }

      case class HorizontalGroup(
                                  enabled: () => Boolean = () => true,
                                  deps: Seq[Field] = Nil,
                                  override val size: String = "md"
                                  )(val fields: Field*) extends HorizontalGroupBase


      trait RadioFieldBase[T] extends Field {

        var modified = false
        def labelText: String
        def labelFor: T => String
        def allOptions: Seq[T]
        def save: T => JsCmd
        var value = get()
        def get: () => T
        def errors(): Seq[(Field, String)] = Nil

        def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

        override def doSave(): JsCmd = { modified = false; save(value) }

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

        override def rendered: NodeSeq = {
          val renderedOptions = allOptions
          <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label>{labelText}:</label>
          {
          renderedOptions.zipWithIndex.map(tuple => {
            <div class="radio">
              <label>
                <input type="radio" name={id + "input"} value={tuple._2.toString} onchange="" checked={if (tuple._1 == value) "checked" else null }/> {labelFor(tuple._1)}
              </label>
            </div>
          }).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)
          }
          <span id={id + "errors"}>{errorsNs}</span>
        </div> ++
            Tail.render(Script(OnLoad(Run(
              s"""
               |$$('input[type=radio][name=${id + "input"}]')
               |  .on('change', function() {
               |    ${xsh.ajaxCall(JsRaw("$(this).val()"), v => {value = renderedOptions(v.toInt); modified = true; onChangeServerSide(this)}).toJsCmd}
               |  });
               |""".stripMargin
            ))))
        }

      }

      case class RadioField[T](
                                labelText: String,
                                allOptions: Seq[T],
                                get: () => T,
                                save: T => JsCmd,
                                labelFor: T => String = (_: T).toString,
                                enabled: () => Boolean = () => true,
                                req: Boolean = false,
                                deps: Seq[Field] = Nil
                                ) extends RadioFieldBase[T]

      trait MultiCheckboxFieldBase[T] extends Field {

        var modified = false
        def labelText: String
        def labelFor: T => String
        def allOptions: Seq[T]
        def save: Set[T] => JsCmd
        var value = get()
        def get: () => Set[T]
        def errors(): Seq[(Field, String)] = Nil


        def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

        override def doSave(): JsCmd = { modified = false; save(value) }

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

        override def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>
          {
          allOptions.map(option => {
            val idcheckbox = Helpers.nextFuncName
            <div class="checkbox">
              <label>
                <input type="checkbox" id={idcheckbox} value={Helpers.nextFuncName} onchange={xsh.ajaxCall(JsRaw(s"$$('#${idcheckbox}').is(':checked')"), v => {
                if (v == "true") {value = value + option} else {value = value - option}
                modified = true;
                onChangeServerSide(this)
              }).toJsCmd}/> {labelFor(option)}
              </label>
            </div>
          }).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)
          }
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }

      }

      case class MultiCheckboxField[T](
                                        labelText: String,
                                        allOptions: Seq[T],
                                        get: () => Set[T],
                                        save: Set[T] => JsCmd,
                                        labelFor: T => String = (_: T).toString,
                                        enabled: () => Boolean = () => true,
                                        req: Boolean = false,
                                        deps: Seq[Field] = Nil
                                        ) extends MultiCheckboxFieldBase[T]


      trait GenTextFieldBase[T] extends Field {

        def toStr(v: T): String
        def fromStr(s: String): Option[T]

        var modified = false

        def placeholderText: String

        def labelText: String

        def inputName: Option[String]

        def get: () => T

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

        var value = get()

        def errors(): Seq[(Field, String)] = Nil

        def save: T => JsCmd

        override def doSave(): JsCmd = { modified = false; save(value) }

        def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

        def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>
          <input name={inputName.orNull} placeholder={placeholderText} value={toStr(value)} onchange={(onChangeClientSide() & xsh.ajaxCall(ValById(id + "input"), (v: String) => {fromStr(v).map(v => {value = v; modified = true; onChangeServerSide(this)}).getOrElse(JsCmds.Noop)})).toJsCmd} id={id + "input"} class="form-control" type="text"/>
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }
      }


      case class TextField(labelText: String, get: () => String, save: String => JsCmd, placeholderText: String = "", enabled: () => Boolean = () => true, inputName: Option[String] = None, req: Boolean = false, deps: Seq[Field] = Nil) extends GenTextFieldBase[String] {

        def toStr(v: String): String = v
        def fromStr(s: String): Option[String] = Some(s)

        override def errors(): Seq[(Field, String)] = if (req && value == "") List(this -> "Required") else Nil
      }

      case class DoubleField(
                              labelText: String,
                              get: () => Double,
                              save: Double => JsCmd,
                              placeholderText: String = "",
                              enabled: () => Boolean = () => true,
                              inputName: Option[String] = None,
                              req: Boolean = false,
                              fmt: String = "%.2f",
                              deps: Seq[Field] = Nil) extends GenTextFieldBase[Double] {

        def toStr(v: Double): String = v.formatted(fmt)
        def fromStr(s: String): Option[Double] = scala.util.Try(s.toDouble).toOption
      }

      case class IntField(
                           labelText: String,
                           get: () => Int,
                           save: Int => JsCmd,
                           placeholderText: String = "",
                           enabled: () => Boolean = () => true,
                           inputName: Option[String] = None,
                           req: Boolean = false,
                           deps: Seq[Field] = Nil) extends GenTextFieldBase[Int] {

        def toStr(v: Int): String = v.toString
        def fromStr(s: String): Option[Int] = scala.util.Try(s.toInt).toOption
      }

      case class LongField(
                            labelText: String,
                            get: () => Long,
                            save: Long => JsCmd,
                            placeholderText: String = "",
                            enabled: () => Boolean = () => true,
                            inputName: Option[String] = None,
                            req: Boolean = false,
                            deps: Seq[Field] = Nil) extends GenTextFieldBase[Long] {

        def toStr(v: Long): String = v.toString
        def fromStr(s: String): Option[Long] = scala.util.Try(s.toLong).toOption
      }

      case class EmptyField() extends Field {
        override def doSave(): JsCmd = JsCmds.Noop
        override def enabled: () => Boolean = () => true
        override def rendered: NodeSeq = <span id={id}></span>
        override def errors(): Seq[(Field, String)] = Nil
        override def update(): JsCmd = JsCmds.Noop
        override def modified: Boolean = false
        override def deps: Seq[Field] = Nil
      }

      trait SelectFieldBase[T] extends Field {

        var modified = false

        def labelText: String

        def labelFor: T => String

        def inputName: Option[String]

        def allOptions: Seq[T]

        def get: () => T

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

        def errors(): Seq[(Field, String)] = Nil

        def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

        var value: T = get()

        def save: T => JsCmd

        override def doSave(): JsCmd = save(value)

        def attrs: Seq[ElemAttr] = Seq("class" -> "form-control", "id" -> (id + "input"), "style" -> "color: rgb(33, 37, 43);") ++ inputName.map("name" -> _).toSeq

        def rendered: NodeSeq = {
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>
          {
          xsh.ajaxSelectElem[T](allOptions, Full(value), attrs: _*)(v => {
            value = v
            modified = true
            onChangeClientSide() & onChangeServerSide(this)
          })((v: T) => labelFor(v))
          }
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }
      }

      case class SelectField[T](
                                 labelText: String,
                                 allOptions: Seq[T],
                                 get: () => T,
                                 save: T => JsCmd,
                                 labelFor: T => String = (_: T).toString,
                                 enabled: () => Boolean = () => true,
                                 inputName: Option[String] = None,
                                 deps: Seq[Field] = Nil
                                 ) extends SelectFieldBase[T]


      trait MultiSelectFieldBase[T] extends Field {

        var modified = false

        def labelText: String

        def labelFor: T => String

        def inputName: Option[String]

        def allOptions: Seq[T]

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

        def errors(): Seq[(Field, String)] = Nil

        def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

        def save: Set[T] => JsCmd

        var value = get()

        def get: () => Set[T]

        override def doSave(): JsCmd = save(value)

        def attrs: Seq[ElemAttr] = Seq("class" -> "form-control", "multiple" -> "multiple", "id" -> (id + "input"), "style" -> "color: rgb(33, 37, 43);") ++ inputName.map("name" -> _).toSeq

        def rendered: NodeSeq = {
          val randomId = Helpers.nextFuncName
          val renderedOptions = allOptions
        <div style={if (!enabled()) "display:none;" else ""} id={id} class="form-group pi-padding-bottom-10">
          <label for={id + "input"}>{labelText}:</label>

            <select class="form-control" id={id + "input"} multiple="multiple" style="color: rgb(33, 37, 43);" onchange={xsh.jsonCall(JsRaw(s"$$('#${id + "input"}').val()"), v => v match {
              case JArray(fields: List[JString]) =>
                value = fields.map(v => renderedOptions(v.s.toInt)).toSet
                modified = true
                onChangeServerSide(this)
              case JNothing | JNull =>
                value = Set()
                modified = true
                onChangeServerSide(this)
            }).toJsCmd}>
           {renderedOptions.zipWithIndex.map(tuple => <option value={tuple._2.toString} selected={if (renderedOptions.contains(tuple._1)) "selected" else null}>{labelFor(tuple._1)}</option>).reduceOption[NodeSeq](_ ++ _).getOrElse(NodeSeq.Empty)}
              </select>
          <span id={id + "errors"}>{errorsNs}</span>
        </div>
      }

      }

      case class MultiSelectField[T](
                                      labelText: String,
                                      allOptions: Seq[T],
                                      get: () => Set[T],
                                      save: Set[T] => JsCmd,
                                      labelFor: T => String = (_: T).toString,
                                      enabled: () => Boolean = () => true,
                                      inputName: Option[String] = None,
                                      deps: Seq[Field] = Nil
                                      ) extends MultiSelectFieldBase[T]


      trait SelectOptFieldBase[T] extends SelectFieldBase[Option[T]] {
        def noneLabel: String
        def someLabel: T => String
        def labelFor: Option[T] => String = _.map(someLabel).getOrElse(noneLabel)

        def allOptOptions: Seq[T]
        def allOptions: Seq[Option[T]] = None +: allOptOptions.map(Some(_))

        def req: Boolean
        override def errors(): Seq[(Field, String)] = super.errors() ++ (if (value.isEmpty) List(this -> "Required") else Nil)
      }

      case class SelectOptField[T](
                                    labelText: String,
                                    allOptOptions: Seq[T],
                                    get: () => Option[T],
                                    save: Option[T] => JsCmd,
                                    noneLabel: String,
                                    someLabel: T => String = (_: T).toString,
                                    enabled: () => Boolean = () => true,
                                    inputName: Option[String] = None,
                                    req: Boolean = false,
                                    deps: Seq[Field] = Nil
                                    ) extends SelectOptFieldBase[T]


      trait DatePickerFieldBase extends Field {

        var modified = false

        def get: () => Long

        def save: Long => JsCmd

        var value = get()

        var datePickerId = UUID.randomUUID().toString

        def labelText: String

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & SetHtml(id + "errors", errorsNs())

        def errors(): Seq[(Field, String)] = Nil

        def errorsNs() = errors().filter(_ => modified || saveFailed).headOption.map(error => <span class="text-red help-block">{error._2}</span>).getOrElse(NodeSeq.Empty)

        override def doSave(): JsCmd = save(value)


        override def rendered: NodeSeq = {
        <label id={id}>
          {labelText}
        </label> <br/>
                <div class="form-group">
                  <div class="input-group date" id={datePickerId}>
                    <input type='text'  class="form-control" />
                    <span class="input-group-addon">
                      <span class="glyphicon glyphicon-calendar"></span></span>
                  </div>
                  <span id={id + "errors"}>{errorsNs}</span>
                </div>
          <tail>
          {Script(OnLoad(Run(
            s"""
             |$$('#${datePickerId}')
             |.datetimepicker({
             |  locale: 'en-gb',
             |  defaultDate: new Date($value)
             |})
             |.on('dp.change', function(evt) {
             |  ${xsh.ajaxCall(JsRaw(s"evt.date.valueOf()"), v => {value = v.toLong; onChangeServerSide(this)}).toJsCmd}
             |});
             |""".stripMargin
          )))}
        </tail>
      }
      }

      case class DateTimePickerField(
                                      labelText: String,
                                      get: () => Long,
                                      save: Long => JsCmd,
                                      enabled: () => Boolean = () => true,
                                      req: Boolean = false,
                                      deps: Seq[Field] = Nil
                                      ) extends DatePickerFieldBase

      trait SubmitButtonBase extends Field {

        val modified = false

        def labelText: String

        def btnClass: () => String

        def update(): JsCmd = (if (enabled()) Show(id) else Hide(id)) & JsCmds.Run(s"""$$('#$id button').attr('class', ${btnClass().encJs});""")

        override def errors(): Seq[(Field, String)] = Nil

        override def doSave(): JsCmd = JsCmds.Noop

        override def rendered: NodeSeq =
        <div id={id}>
          <div class="clearfix"><button style={(if (!enabled()) "display:none;" else "")} onclick={xsh.ajaxInvoke(() => onSave()).toJsCmd} class={btnClass()}>{labelText}</button></div>
        </div>
      }


      case class SubmitButton(labelText: String = "Save", btnClass: () => String = () => TH.Btn().Primary.right.clas, enabled: () => Boolean = () => true, deps: Seq[Field] = Nil) extends SubmitButtonBase

    }

  }

  trait SBtns extends SIcons {

    case class Btn(
                    clas: String,
                    style: String = "",
                    cont: String = "",
                    onclickJs: JsCmd = JsCmds.Noop,
                    ajaxJsOpt: Option[() => JsCmd] = None,
                    id: String = UUID.randomUUID().toString,
                    icnOpt: Option[Icon] = None,
                    hrefOpt: Option[String] = None,
                    center: Boolean = false
                    )(implicit val xsh: XSPageHandle) {

      def App: Btn = copy(clas = clas + " btn btn-app")
      def Default: Btn = copy(clas = clas + " btn btn-default")
      def Primary: Btn = copy(clas = clas + " btn btn-primary ")
      def Info: Btn = copy(clas = clas + " btn btn-info ")
      def Warning: Btn = copy(clas = clas + " btn btn-warning ")
      def Success: Btn = copy(clas = clas + " btn btn-success ")
      def Danger: Btn = copy(clas = clas + " btn btn-danger ")

      def withClass(cs: String): Btn = copy(clas = clas + cs)

      def lg: Btn = copy(clas = clas + " btn-lg")
      def sm: Btn = copy(clas = clas + " btn-sm")
      def xs: Btn = copy(clas = clas + " btn-xs")
      def flat: Btn = copy(clas = clas + " btn-flat")
      def block: Btn = copy(clas = clas + " btn-block")
      def disabled: Btn = copy(clas = clas + " disabled")
      def margin: Btn = copy(clas = clas + " margin")
      def right: Btn = copy(clas = clas + " pull-right")

      def Maroon: Btn = copy(clas = clas + " btn bg-maroon")
      def Purple: Btn = copy(clas = clas + " btn bg-purple")
      def Navy: Btn = copy(clas = clas + " btn bg-navy")
      def Orange: Btn = copy(clas = clas + " btn bg-orange")
      def Olive: Btn = copy(clas = clas + " btn bg-olive")


      def withStyle(sstyle: String): Btn = copy(style = sstyle)

      def lbl(scont: String): Btn = copy(cont = scont)

      def onclick(cmd: JsCmd): Btn = copy(onclickJs = cmd & onclickJs)

      def href(to: String): Btn = copy(hrefOpt = Some(to))

      def ajax(ajaxf: => JsCmd): Btn = copy(ajaxJsOpt = Some(() => ajaxf))

      def icn(icn: Icon): Btn = copy(icnOpt = Some(icn))

      def centered: Btn = copy(center = true)

      def btn: NodeSeq = {
        <button id={id}  onclick={(onclickJs & ajaxJsOpt.map(ajaxJs => xsh.ajaxInvoke(ajaxJs)).getOrElse(JsCmds.Noop)).toJsCmd} style={(if (center) "margin: 0 auto; display: block" else "") + style} class={clas}>{icnOpt.map(_.icn ++ <span> </span>).getOrElse(NodeSeq.Empty)}{cont}</button>
      }

      def link: NodeSeq = {
        <a href={hrefOpt.getOrElse("javascript:void(0)")} id={id} onclick={(onclickJs & ajaxJsOpt.map(ajaxJs => xsh.ajaxInvoke(ajaxJs)).getOrElse(JsCmds.Noop)).toJsCmd} style={style} class={clas}>{icnOpt.map(_.icn ++ <span> </span>).getOrElse(NodeSeq.Empty)}{cont}</a>
      }

      def replace() = Replace(id, btn)

      def toggle(get: => Boolean, set: Boolean => JsCmd, selected: Btn => Btn, unselected: Btn => Btn): NodeSeq = {
        val isSelected = get
        (if (isSelected) selected(this) else unselected(this)).ajax({
          set(!isSelected) & Replace(id, toggle(get, set, selected, unselected))
        }).btn
      }

      def changeToDefault: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger')")
      def changeToPrimary: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-primary')")
      def changeToInfo: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-info')")
      def changeToWarning: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-warning')")
      def changeToSuccess: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-success')")
      def changeToDanger: JsCmd = Run(s"$$('#$id').removeClass('btn-primary btn-info btn-warning btn-success btn-danger').addClass('btn-danger')")
    }

    object Btn {
      def apply()(implicit xsh: XSPageHandle) = new Btn("")
    }

  }

  object TH extends SBase
                    with SWidgets
                    with SIcons
                    with SGrid
                    with SBtns
                    with SSyntaxHighlighter
                    with SModals
                    with SKnob
                    with SToastr
                    with SModularTables
                    with SDashborardWidgets
                    with SFCharts
                    with SWysiwyg
                    with SForms {

  }

}
