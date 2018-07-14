package org.sailcbi.Components.Item

import org.sailcbi.Core.Main.Target
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._
import org.scalajs

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

trait Item {
  def asTR(itemID: String, itemLabel: String, value: String, onChange: Option[js.Function1[String, Unit]], extraCells: Option[VNodeContents[_]]): VNode
  protected def asTR(itemID: String, itemLabel: String, value: String, isPassword: Boolean, onChange: Option[js.Function1[String, Unit]], extraCells: Option[VNodeContents[_]]): VNode = {
    val inputType = if (isPassword) "password" else "text"
    val events: Map[String, js.Any] = onChange match {
      case None => Map.empty
      case Some(f) => Map("change" -> ((e: scalajs.dom.TextEvent) => {
        f(e.target.asInstanceOf[Target].value.toString)
      }))
    }
    val baseCells: List[VNodeContents[_]] = List(
      td(style=Map("text-align"->"right"), contents=VNodeContents(
        label(id=itemID+"_LABEL", props=Map("for"->itemID), contents=
          span(classes=List("optional"), contents=itemLabel)
        )
      )),
      td(style=Map("text-align"->"left"), contents=VNodeContents(
        input(
          id=itemID,
          classes=List("text_field", "apex-item-text"),
          props=Map("type"->inputType, "name"->itemID, "size"->"25", "maxlength"->"100", "value"->value),
          events=events
        )
      ))
    )
    val allCells: VNodeContents[_] = extraCells match {
      case None => baseCells.toJSArray
      case Some(c) => (baseCells :+ c).toJSArray
    }
    tr(contents=allCells)
  }
}
