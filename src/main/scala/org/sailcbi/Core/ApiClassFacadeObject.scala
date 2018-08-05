package org.sailcbi.Core

import org.sailcbi.ApiEndpointFacade.User

import scala.scalajs.js

abstract class ApiClassFacadeObject[T <: ApiClassFacade] {
  val endpoint: String
  def parse(r: ApiResult): js.Array[T]
}
