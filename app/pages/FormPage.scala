package pages


import net.liftweb.http.js.{JsCmd, JsCmds}
import net.liftweb.util.Helpers._
import pages.theme.{Styles, Libs}
import pages.theme.Libs.Lib
import pages.theme.Styles.Style
import controllers.RootController.XSPageHandle

import scala.xml.NodeSeq

class FormPage()(implicit val xsh: XSPageHandle) extends MenuPage with DemoCodePage {

  override def pageTitle: String = "Forms"
  override def pageSubtitle: String = "Form Examples"
  override def pageCodeFile: String = "app/pages/FormPage.scala"

  override def styles: Set[Style] = super.styles + Styles.datepicker

  override def libs: Set[Lib] = super.libs + Libs.moment + Libs.datepicker

  override def render: (NodeSeq) => NodeSeq = super.render andThen (
    ".page-contents" #> {
      TH.Row(
        TH.Col().md6 {
          new TH.SimpleWidget("Minimal Form Example", style = TH.WidgetStyle.Primary)(
            // formExample1
            new TH.DefaultForm() {

              var name = ""
              var email = ""

              lazy val nameF = TextField("Name", () => name, s => {name = s; JsCmds.Noop})
              lazy val emailF = TextField("Email", () => email, s => {email = s; JsCmds.Noop})

              override val field: Field =
                VerticalGroup()(
                  nameF,
                  emailF,
                  SubmitButton()
                )

              override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                TH.Toastr.Success("Creating User", s"Creating user $name with email $email").show()
              }
            }.rendered
            // formExample1
          ).renderedWidget ++
            codeWidget("formExample1")
        },
        TH.Col().md6 {
          new TH.SimpleWidget("Required Inputs", style = TH.WidgetStyle.Primary)(
            <p>Use <code>req = true</code> to mark a field as required.</p> ++
              // formExample2
              new TH.DefaultForm() {

                var name = ""
                var email = ""

                lazy val nameF = TextField("Name*", () => name, s => {name = s; JsCmds.Noop}, req = true)
                lazy val emailF = TextField("Email*", () => email, s => {email = s; JsCmds.Noop}, req = true)

                override val field: Field =
                  VerticalGroup()(
                    nameF,
                    emailF,
                    SubmitButton()
                  )

                override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                  TH.Toastr.Success("Creating User", s"Creating user $name with email $email").show()
                }
              }.rendered
            // formExample2
          ).renderedWidget ++
            codeWidget("formExample2")
        }
      ) ++
        TH.Row(
          TH.Col().md6 {
            new TH.SimpleWidget("Specifying the name attribute", style = TH.WidgetStyle.Primary)(
              <p>Use <code>inputName = Some("full_name")</code> include a "name" attribute in your tag, so that the browser
              will complete the form fields:</p> ++
                new TH.DefaultForm() {

                  var firstName = ""
                  var lastName = ""
                  var email = ""
                  // formExample3
                  lazy val firstNameF = TextField("First Name", () => firstName, s => {firstName = s; JsCmds.Noop}, inputName = Some("first_name"))
                  lazy val lastNameF = TextField("Last Name", () => lastName, s => {lastName = s; JsCmds.Noop}, inputName = Some("last_name"))
                  lazy val emailF = TextField("Email*", () => email, s => {email = s; JsCmds.Noop}, inputName = Some("email"))

                  override val field: Field =
                    VerticalGroup()(
                      firstNameF,
                      lastNameF,
                      emailF,
                      SubmitButton()
                    )
                  // formExample3
                  override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                    TH.Toastr.Success("Creating User", s"Creating user $firstName $lastName with email $email").show()
                  }
                }.rendered ++
              <p>Browsers will complete the fields:</p> ++
              <center><img src="/static/img/autocomplete.png" style="margin: 10px 0;  box-shadow: 4px 7px 8px #eeeeee;"></img></center>
            ).renderedWidget ++
              codeWidget("formExample3")
          },
          TH.Col().md6 {
            new TH.SimpleWidget("Horizontal Group Example", style = TH.WidgetStyle.Primary)(
              <p>Use <code>HorizontalGroup()(field1, field2, ...)</code> to group form elements horizontally:</p> ++
                new TH.DefaultForm() {

                  var firstName = ""
                  var lastName = ""
                  var email = ""

                  lazy val firstNameF = TextField("First Name", () => firstName, s => {firstName = s; JsCmds.Noop})
                  lazy val lastNameF = TextField("Last Name", () => lastName, s => {lastName = s; JsCmds.Noop})
                  lazy val emailF = TextField("Email", () => email, s => {email = s; JsCmds.Noop})

                  // formExample4
                  override val field: Field =
                    VerticalGroup()(
                      // Groups the elements horizontally:
                      HorizontalGroup(size = "xs")(firstNameF, lastNameF),
                      emailF,
                      SubmitButton()
                    )
                  // formExample4

                  override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                    TH.Toastr.Success("Creating User", s"Creating user $firstName $lastName with email $email").show()
                  }
                }.rendered
            ).renderedWidget ++
              codeWidget("formExample4")
          }) ++ TH.Row(
        TH.Col().md6 {
          new TH.SimpleWidget("Int and Double Inputs Example", style = TH.WidgetStyle.Primary)(
            new TH.DefaultForm() {

              var age: Int = 0
              var height: Double = 0.0
              // formExample5
              lazy val ageF = IntField("Age", () => age, s => {age = s; JsCmds.Noop})
              lazy val heightF = DoubleField("Height", () => height, s => {height = s; JsCmds.Noop})

              override val field: Field =
                VerticalGroup()(
                  ageF,
                  heightF,
                  SubmitButton(labelText = "Send", btnClass = () => TH.Btn().Info.clas)
                )
              // formExample5
              override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                TH.Toastr.Success("Creating User", s"Creating user $age $height").show()
              }
            }.rendered ++
              <p>You can easily configure the Save button, like this: <code>SubmitButton("Send", TH.Btn().Info.clas)</code></p>
          ).renderedWidget ++
            codeWidget("formExample5")
        },
        TH.Col().md6 {
          new TH.SimpleWidget("Select Input Example", style = TH.WidgetStyle.Primary)(
            new TH.DefaultForm() {

              var countryLive = ""
              var countryBorn: Option[String] = None
              // formExample6
              lazy val countryLiveF = SelectField[String]("Residence", TH.DemoData.countries, () => countryLive, v => {countryLive = v; JsCmds.Noop})
              lazy val countryBornF = SelectOptField[String]("Born in*", TH.DemoData.countries, () => countryBorn, countryBorn = _, "Unspecified", req = true)

              override val field: Field =
                VerticalGroup()(
                  countryLiveF,
                  countryBornF,
                  SubmitButton()
                )
              // formExample6

              override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                TH.Toastr.Success("Saved", "").show()
              }
            }.rendered
          ).renderedWidget ++
            codeWidget("formExample6")
        }) ++
        TH.Row(
          TH.Col().md3 {
            new TH.SimpleWidget("Radio Example", style = TH.WidgetStyle.Primary)(
              new TH.DefaultForm() {

                val sizes = List("XS", "S", "M", "L", "XL")
                var size = "M"
                // formExample7
                lazy val sizeGuideF = RadioField[String]("T-Shirt Size", sizes, () => size, v => {size = v; JsCmds.Noop})

                override val field: Field =
                  VerticalGroup()(
                    sizeGuideF,
                    SubmitButton()
                  )
                // formExample7
                override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                  TH.Toastr.Success("Ordered T-Shirt", s"Ordered T-Shirt with size $size").show()
                }
              }.rendered
            ).renderedWidget ++
              codeWidget("formExample7")
          },
          TH.Col().md3 {
            new TH.SimpleWidget("Multi checkbox example", style = TH.WidgetStyle.Primary)(
              new TH.DefaultForm() {
                // formExample8
                val sport = List("Soccer", "Football", "Baseball", "Basketball")
                var sportChoose = Set[String]()
                lazy val sportChooseF = MultiCheckboxField[String]("Please select every sport that you play", sport, () => sportChoose, v => {sportChoose = v; JsCmds.Noop})

                override val field: Field =
                  VerticalGroup()(
                    sportChooseF,
                    SubmitButton()
                  )
                // formExample8
                override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                  TH.Toastr.Success("User Hobbies:", s"User play ${sportChoose.mkString(", ")}").show()
                }
              }.rendered
            ).renderedWidget ++
              codeWidget("formExample8")
          },
          TH.Col().md6 {
          new TH.SimpleWidget("Multi select example", style = TH.WidgetStyle.Primary)(
            new TH.DefaultForm() {
              // formExample9
              val sport = List("Soccer", "Football", "Baseball", "Basketball")
              var sportChoose = Set[String]()
              lazy val sportChooseF = MultiSelectField[String]("Please select every sport that you play", sport, () => sportChoose, v => {sportChoose = v; JsCmds.Noop})

              override val field: Field =
                VerticalGroup()(
                  sportChooseF,
                  SubmitButton()
                )
              // formExample9
              override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                TH.Toastr.Success("User Hobbies:", s"User play ${sportChoose.mkString(", ")}").show()
              }
            }.rendered
          ).renderedWidget ++
            codeWidget("formExample9")}
        )++
        TH.Row(
          TH.Col().md6 {  new TH.SimpleWidget("Event Date Example", style = TH.WidgetStyle.Primary)(
            new TH.DefaultForm() {
              var dateEvent= 0L
              // formExample10
              lazy val dateEventF = DateTimePickerField("Event Date:", ()=>dateEvent, v => {dateEvent = v; JsCmds.Noop})

              override val field: Field =
                VerticalGroup()(
                  dateEventF,
                  SubmitButton()
                )
              // formExample10
              override def afterSucessfullSave(): JsCmd = super.afterSucessfullSave() & {
                TH.Toastr.Success("User have :", s"a event on ${dateEvent.toDateTime.toString("dd/MMMM/yyyy HH:mm", java.util.Locale.forLanguageTag("pt"))}").show()
              }
            }.rendered
          ).renderedWidget ++
            codeWidget("formExample10")
          })
    }
    )
}
