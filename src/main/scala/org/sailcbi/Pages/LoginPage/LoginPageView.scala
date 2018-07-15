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
import org.sailcbi.ViewTemplates.{ StandardPage}

import scala.concurrent.Promise
import scala.scalajs.js
import scala.util.{Failure, Success}

class LoginPageView (renderer: VNodeContents[_] => Unit) extends StandardPage[LoginPageModel](renderer) {
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
  def getMain(model: LoginPageModel): VNodeContents[_] =
    div(classes=List("auth-form"), contents=VNodeContents(
      div(classes=List("form-group"), contents=VNodeContents(
        div(classes=List("form-label-group"), contents=VNodeContents(
          input(id="inputUser", props=Map("type"->"text", "placholder"->"Username"), classes=List("form-control")),
          label(props=Map("for"->"inputUser"), contents="Username")
        ))
      )),
      div(classes=List("form-group"), contents=VNodeContents(
        div(classes=List("form-label-group"), contents=VNodeContents(
          input(id="inputPassword", props=Map("type"->"password", "placholder"->"Password"), classes=List("form-control")),
          label(props=Map("for"->"inputPassword"), contents="Password")
        ))
      )),
      div(classes=List("form-group"), contents=VNodeContents(
        button(classes=List("btn", "btn-lg", "btn-primary", "btn-block"), props=Map("type"->"submit"), contents="Sign in")
      ))
    ))
}
