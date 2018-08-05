package org.sailcbi.Pages.Test1Page

import org.sailcbi.ApiEndpointFacade.User
import org.sailcbi.Components.Table
import org.sailcbi.Core.{GetObjectsFromAPI, Router, View}
import org.sailcbi.VNode.{VNodeContents, div, span}
import org.sailcbi.ViewTemplates.StandardPage

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.Success

class Test1PageView(renderer: VNodeContents[_] => Unit) extends StandardPage[Test1PageModel](renderer) {
  val self: View[Test1PageModel] = this
  override val defaultModel: Test1PageModel = Test1PageModel(None)

  val goToTest2: js.Function0[Unit] = () => {
    Router.transition("/test2")
  }

  override def getMain(model: Test1PageModel): VNodeContents[_] = {
    initializeDataFromAPI(self, model, model.users, User, model.SetEntity)

    div(VNodeContents(
      "Test 1!",
      span(events=Map("click" -> goToTest2), contents="go to test2"),
      Table.apply(User.fields.map(_._1), model.users.getOrElse(js.Array()).map(_.valuesAsJsArray))
    ))
  }
}