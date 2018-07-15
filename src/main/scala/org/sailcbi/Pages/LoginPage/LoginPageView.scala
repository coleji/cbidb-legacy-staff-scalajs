package org.sailcbi.Pages.LoginPage

import fr.hmil.roshttp.body.URLEncodedBody
import fr.hmil.roshttp.response.SimpleHttpResponse
import fr.hmil.roshttp.{HttpRequest, Method}
import org.sailcbi.CbiUtil.Currency
import org.sailcbi.Components.{Button, Item, JoomlaArticleRegion}
import org.sailcbi.Core.Main.Target
import org.sailcbi.Core._
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.VNodeContents._
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.StandardPage

import scala.concurrent.Promise
import scala.scalajs.js
import scala.util.{Failure, Success}
import org.scalajs

class LoginPageView (renderer: VNodeContents[_] => Unit) extends StandardPage[LoginPageModel](renderer) {
  val self = this
  override val defaultModel: LoginPageModel = LoginPageModel(None, None, Currency.dollars(325), 2017, false)

  object ChangeUserName extends Message[LoginPageModel, String] {
    def update: LoginPageModel => String => LoginPageModel =
      model => payload => LoginPageModel(Some(payload), model.password, model.jpPrice, model.lastSeason, model.loginRequestPending)
  }
  object ChangePassword extends Message[LoginPageModel, String] {
    def update: LoginPageModel => String => LoginPageModel =
      model => payload => LoginPageModel(model.userName, Some(payload), model.jpPrice, model.lastSeason, model.loginRequestPending)
  }
  object SubmitLoginRequest extends NoArgMessage[LoginPageModel] {
    def update: LoginPageModel => LoginPageModel =
      model => {
        submitLogin(model)
        LoginPageModel(model.userName, model.password, model.jpPrice, model.lastSeason, true)
      }
  }
  object ReceiveLoginResponse extends NoArgMessage[LoginPageModel] {
    def update: LoginPageModel => LoginPageModel =
      model => LoginPageModel(model.userName, None, model.jpPrice, model.lastSeason, false)
  }

  def submitLogin: (LoginPageModel) => Unit = (model: LoginPageModel) => {
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
      case res:Success[SimpleHttpResponse] => ReceiveLoginResponse(self)(model)()
      case _: Failure[SimpleHttpResponse] => ReceiveLoginResponse(self)(model)()
    })
  }

  def getMain(model: LoginPageModel): VNodeContents[_] =
    div(classes=List("auth-form"), contents=VNodeContents(
      div(classes=List("form-group"), contents=VNodeContents(
        div(classes=List("form-label-group"), contents=VNodeContents(
          input(
            id="inputUser",
            props=Map("type"->"text", "placholder"->"Username", "required"->"", "autofocus"->"", "value" -> model.userName.getOrElse("")),
            classes=List("form-control"),
            events = Map("input" -> ((e: scalajs.dom.TextEvent) => {
              ChangeUserName(this)(model)(e.target.asInstanceOf[Target].value.toString)
            }))
          ),
          label(props=Map("for"->"inputUser"), contents="Username")
        ))
      )),
      div(classes=List("form-group"), contents=VNodeContents(
        div(classes=List("form-label-group"), contents=VNodeContents(
          input(
            id="inputPassword",
            props=Map("type"->"password", "placholder"->"Password", "value" -> model.password.getOrElse("")),
            classes=List("form-control"),
            events = Map("input" -> ((e: scalajs.dom.TextEvent) => {
              ChangePassword(this)(model)(e.target.asInstanceOf[Target].value.toString)
            }))
          ),
          label(props=Map("for"->"inputPassword"), contents="Password")
        ))
      )),
      div(classes=List("form-group"), contents=VNodeContents(
        button(
          classes=(if (model.loginRequestPending) "btn-light" else "btn-primary") :: List("btn", "btn-lg", "btn-block"),
          props=Map("type"->"submit"),
          events=if(!model.loginRequestPending) Map("click"->SubmitLoginRequest(this)(model)) else Map.empty,
          contents=if(model.loginRequestPending) VNodeContents("Sign in", "   ", div(classes=List("loader", "loader-sm"))) else ("Sign in")
        )
      ))
    ))
}
