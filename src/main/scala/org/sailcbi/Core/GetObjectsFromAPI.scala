package org.sailcbi.Core

import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.JSON

object GetObjectsFromAPI {
  def apply[T <: ApiClassFacade](obj: ApiClassFacadeObject[T]): Future[js.Array[T]] = {
    val req = HttpRequest(Main.API_LOCATION + "/" + obj.endpoint)
    req.send().map(res => {
      println(res.body)
      val apiResult: ApiResult = JSON.parse(res.body).asInstanceOf[js.Object].asInstanceOf[ApiResult]
      obj.parse(apiResult)
    })
  }
}
