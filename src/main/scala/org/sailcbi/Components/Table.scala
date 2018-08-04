package org.sailcbi.Components

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._

import scala.scalajs.js

object Table {
  val columnNames = js.Array("col1", "col2", "col3")

  def apply: VNode = table(classes=List("table", "dataTable", "no-footer"), contents=VNodeContents(
    thead(contents=tr(contents=VNodeContents(
      columnNames.map(name => th(name): VNodeContents[_])
    )))
  ))
}
