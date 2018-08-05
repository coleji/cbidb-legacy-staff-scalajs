package org.sailcbi.Pages.Test1Page

import org.sailcbi.ApiEndpointFacade.User
import org.sailcbi.Components.Table
import org.sailcbi.Core.{GetObjectsFromAPI, Router}
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.{VNodeContents, div, span}
import org.sailcbi.ViewTemplates.StandardPage

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

class Test1PageView(renderer: VNodeContents[_] => Unit) extends StandardPage[Test1PageModel](renderer) {
  override val defaultModel: Test1PageModel = new Test1PageModel

  val goToTest2: js.Function0[Unit] = () => {
    Router.transition("/test2")
  }

  val columnNames = js.Array("col1", "col2", "col3")
  val data: js.Array[js.Array[String]] = js.Array(
    js.Array("a", "b", "c"),
    js.Array("d", "e", "f"),
    js.Array("g", "h", "i")
  )

  val table: VNode = Table.apply(columnNames, data)

  override def getMain(model: Test1PageModel): VNodeContents[_] = {
    val usersFuture = GetObjectsFromAPI(User).onComplete(r => {
      println("### " + r.get.length)
    })

    div(VNodeContents(
      "Test 1!",
      span(events=Map("click" -> goToTest2), contents="go to test2"),
      table
    ))
  }
}
