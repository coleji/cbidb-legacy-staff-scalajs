package org.sailcbi.Core

import org.sailcbi.ApiEndpointFacade.User
import org.sailcbi.VNode.VNodeContents

import scala.scalajs.js
import scala.util.Success

import scala.concurrent.ExecutionContext.Implicits.global

abstract class View[T <: Model](val renderer: VNodeContents[_] => Unit) {
  val defaultModel: T
  def getContent(m: T): VNodeContents[_]

  def initialRender(): Unit = render(defaultModel)
  def render(model: T): Unit = renderer(getContent(model))

  def initializeDataFromAPI[U <: Model, V <: ApiClassFacade](
    self: View[U],                    // the view to update
    model: U,                         // the starting model
    collection: Option[js.Array[V]],  // the specific thing in the starting model to check if its uninitialized
    obj: ApiClassFacadeObject[V],     // the companion obj of the thing I want to get a list of
    message: Message[U, js.Array[V]]  // the model updater i want to call after i get my list of stuff
  ): Unit = {
    if (collection.isEmpty) {
      GetObjectsFromAPI(obj).onComplete({
        case Success(arr: js.Array[V]) => message(self)(model)(arr)
        case _ => message(self)(model)(js.Array())
      })
    }
  }
}
