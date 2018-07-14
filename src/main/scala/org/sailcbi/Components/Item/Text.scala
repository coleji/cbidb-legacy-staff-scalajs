package org.sailcbi.Components.Item

import org.sailcbi.VNode._

import scala.scalajs.js

object Text extends Item{
  override def asTR(itemID: String, itemLabel: String, value: String, onChange: Option[js.Function1[String, Unit]], extraCells: Option[VNodeContents[_]] = None): SnabbdomFacade.VNode =
    asTR(itemID, itemLabel, value, isPassword = false, onChange, extraCells)
}
