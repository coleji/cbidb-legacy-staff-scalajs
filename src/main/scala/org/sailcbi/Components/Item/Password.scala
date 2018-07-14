package org.sailcbi.Components.Item
import org.sailcbi.VNode.{SnabbdomFacade, VNodeContents}

import scala.scalajs.js

object Password extends Item {
  override def asTR(itemID: String, itemLabel: String, value: String, onChange: Option[js.Function1[String, Unit]], extraCells: Option[VNodeContents[_]] = None): SnabbdomFacade.VNode =
    asTR(itemID, itemLabel, value, isPassword = true, onChange, extraCells)
}
