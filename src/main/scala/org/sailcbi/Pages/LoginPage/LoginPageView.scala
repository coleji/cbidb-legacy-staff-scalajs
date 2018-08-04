package org.sailcbi.Pages.LoginPage

import fr.hmil.roshttp.body.URLEncodedBody
import fr.hmil.roshttp.response.SimpleHttpResponse
import fr.hmil.roshttp.{HttpRequest, Method}
import org.sailcbi.CbiUtil.Currency
import org.sailcbi.Core.Main.Target
import org.sailcbi.Core._
import org.sailcbi.VNode.VNodeContents._
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.StandardPage
import org.scalajs

import scala.concurrent.Promise
import scala.util.{Failure, Success}

class LoginPageView (renderer: VNodeContents[_] => Unit)(path: String) extends StandardPage[LoginPageModel](renderer) {
  val self = this
  override val defaultModel: LoginPageModel = LoginPageModel(path, None, None, Currency.dollars(325), 2017, false)

  object ChangeUserName extends Message[LoginPageModel, String] {
    def update: LoginPageModel => String => LoginPageModel =
      model => payload => LoginPageModel(model.path, Some(payload), model.password, model.jpPrice, model.lastSeason, model.loginRequestPending)
  }
  object ChangePassword extends Message[LoginPageModel, String] {
    def update: LoginPageModel => String => LoginPageModel =
      model => payload => LoginPageModel(model.path, model.userName, Some(payload), model.jpPrice, model.lastSeason, model.loginRequestPending)
  }
  object SubmitLoginRequest extends NoArgMessage[LoginPageModel] {
    def update: LoginPageModel => LoginPageModel =
      model => {
        submitLogin(model)
        LoginPageModel(model.path, model.userName, model.password, model.jpPrice, model.lastSeason, true)
      }
  }
  object ReceiveLoginResponse extends NoArgMessage[LoginPageModel] {
    def update: LoginPageModel => LoginPageModel =
      model => LoginPageModel(model.path, model.userName, None, model.jpPrice, model.lastSeason, false)
  }

  def submitLogin: (LoginPageModel) => Unit = (model: LoginPageModel) => {
    import monix.execution.Scheduler.Implicits.global
    val p = Promise[View[_]]
    val body = URLEncodedBody(
      "username" -> model.userName.getOrElse(""),
      "password" -> model.password.getOrElse(""),
      "redirect-url" -> (Main.BASE_LOCATION + path)
    )
    val request =
      HttpRequest(Main.API_LOCATION + "/authenticate")
        .withMethod(Method.POST)
        .withHeader("dont-redirect", "true")
        .withBody(body)


    request.send().onComplete({
      case res:Success[SimpleHttpResponse] => {
        if (res.value.body == "true") Router.transition(path)
        else ReceiveLoginResponse(self)(model)()
      }
      case _: Failure[SimpleHttpResponse] => {
        println("login failure")
        ReceiveLoginResponse(self)(model)()
      }
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
            }), "keypress" -> ((e: scalajs.dom.KeyboardEvent) => {
              if(e.keyCode == 13) {
                println("Enter!")
                SubmitLoginRequest(this)(model)()
              }
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
