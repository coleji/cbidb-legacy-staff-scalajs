package org.sailcbi.Pages.LoginPage

import fr.hmil.roshttp.body.URLEncodedBody
import fr.hmil.roshttp.response.SimpleHttpResponse
import fr.hmil.roshttp.{HttpRequest, Method}
import org.sailcbi.CbiUtil.Currency
import org.sailcbi.Components.{Button, Item, JoomlaArticleRegion}
import org.sailcbi.Core.{Main, Message, View}
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.VNodeContents._
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.JoomlaTwoColumns

import scala.concurrent.Promise
import scala.scalajs.js
import scala.util.{Failure, Success}

class LoginPageView (renderer: VNode => Unit) extends JoomlaTwoColumns[LoginPageModel](renderer) {
  val self = this
  override val defaultModel: LoginPageModel = LoginPageModel(None, None, Currency.dollars(325), 2017)
  def makeLoginFunction: (LoginPageModel) => () => Unit = (model: LoginPageModel) => () => {
    import monix.execution.Scheduler.Implicits.global
    val p = Promise[View[_]]
    val body = URLEncodedBody(
      "username" -> model.userName.getOrElse(""),
      "password" -> model.password.getOrElse(""),
      "redirect-url" -> (Main.BASE_LOCATION + "/login")
    )
    val request =
      HttpRequest(Main.API_LOCATION + "/authenticate")
        .withMethod(Method.POST)
          .withBody(body)

    request.send().onComplete({
      case res:Success[SimpleHttpResponse] =>
      case _: Failure[SimpleHttpResponse] =>
    })
  }
  object ChangeUserName extends Message[LoginPageModel, String] {
    def update: LoginPageModel => String => LoginPageModel =
      model => payload => LoginPageModel(Some(payload), model.password, model.jpPrice, model.lastSeason)
  }
  object ChangePassword extends Message[LoginPageModel, String] {
    def update: LoginPageModel => String => LoginPageModel =
      model => payload => LoginPageModel(model.userName, Some(payload), model.jpPrice, model.lastSeason)
  }
  def getLeft(model: LoginPageModel): VNodeContents[_] = {
    val welcomeRegion = {
      val text = VNodeContents(
        "If you are new to Community Boating and would like to purchase a Junior Program membership for your child, ",
        b(contents="click on the first option"),
        "  to the right.  Once your child's registration is complete you can return here to sign him/her up for classes and view his/her progression throughout the summer.",
        br(),
        br(),
        "If you were looking for ",
        b("Adult Program"),
        "  registration, please ",
        aPlaceholder("click here!")
      )
      JoomlaArticleRegion("hello", VNodeContents("Welcome to CBI Online!", br(), "-  Junior Program  -"), text)
    }
    val scholarshipsAreAvailable = {
      val text = VNodeContents(
        s"""
          |The price of a Junior Program membership is ${model.jpPrice.format(zeroes = false)}.
          |Community Boating Inc. provides scholarships for families in need
          |so that every child has an opportunity to enroll in the Junior Program.
          |To find out if you qualify for a scholarship,
          |please create an account and fill out the scholarship form.
        """.stripMargin
      )
      JoomlaArticleRegion("scholarships", VNodeContents("Scholarships are available to provide sailing for all."), text)
    }
    VNodeContents(
      welcomeRegion,
      scholarshipsAreAvailable
    )
  }
  def getRight(model: LoginPageModel): VNodeContents[_] = {
    val updateUserName: js.Function1[String, Unit] = ChangeUserName(self)(model)
    val updatePassword: js.Function1[String, Unit] = ChangePassword(self)(model)
    val createAccount = {
      val contents = VNodeContents(
        ul(style=Map("font-size"->"0.92em", "margin-left"->"20px"), contents=li(aPlaceholder("Click here to create a parent account."))),
        br(),
        s"If your child was a member in ${model.lastSeason}, please use your email and password from last season, rather than creating a new account."
      )
      JoomlaArticleRegion("createAcct", VNodeContents("My child and I are new to Community Boating."), contents)
    }
    val login = {
      val contents = VNodeContents(
        "Enter your email address and password to continue.",
        br(),
        table(tbody(VNodeContents(
          Item.Text.asTR(
            itemID = "P101_USERNAME",
            itemLabel = "Email",
            value = model.userName.getOrElse(""),
            onChange = Some(updateUserName),
            extraCells = None
          ),
          Item.Password.asTR(
            itemID = "P101_PASSWORD",
            itemLabel = "Password",
            value = model.password.getOrElse(""),
            onChange = Some(updatePassword),
            extraCells = Some(td(contents=Button("Login", makeLoginFunction(model))))
          ),
          tr(VNodeContents(
            td(),
            td(
              span(
                aPlaceholder("I forgot my password!")
              )
            )
          ))
        )))
      )
      JoomlaArticleRegion("login", VNodeContents("I already have an account."), contents)
    }
    val inPerson = {
      val text = VNodeContents(
        "If you have already purchased a membership for this year in person, ",
        "you should have received an email with a link to set a password for your account. ",
        "If you did not receive the email, click \"I Forgot My Password\" to the right ",
        "and we will send you another ",
        b("(IMPORTANT: Be sure to use the same email address used for initial registration)"),
        ". If you still have difficulty accessing your account, please call the boathouse at 617-523-1038."
      )
      JoomlaArticleRegion("inPerson", VNodeContents("I purchased a membership in person."), text)
    }
    VNodeContents(
      createAccount,
      login,
      inPerson
    )
  }
}
