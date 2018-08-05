package org.sailcbi.Pages.Test1Page

import org.sailcbi.ApiEndpointFacade.User
import org.sailcbi.Core.{Message, Model}

import scala.scalajs.js

case class Test1PageModel(users: Option[js.Array[User]]) extends Model {
  object SetEntity extends Message[Test1PageModel, js.Array[User]] {
    override def update: Test1PageModel => js.Array[User] => Test1PageModel = {
      _ => users => Test1PageModel(Some(users))
    }
  }
}