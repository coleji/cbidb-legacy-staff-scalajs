package org.sailcbi.Components

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._

import scala.scalajs.js

object Table {
  def apply(columnNames: js.Array[String], data: js.Array[js.Array[String]]): VNode =
    table(classes=List("table", "dataTable", "no-footer"), contents=VNodeContents(
      thead(contents=tr(contents=VNodeContents(
        columnNames.map(name => {
          th(classes=List("align-middle", "sorting"), contents=name): VNodeContents[_]
        })
      ))),
      tbody(contents=VNodeContents(data.zipWithIndex.map(rowAndIndex => VNodeContents {
        val row = rowAndIndex._1
        val isEven = rowAndIndex._2 % 2 == 0
        // this is backwards because the array starts at zero but the first row needs to be odd
        val classes = if (isEven) List("odd") else List("even")
        tr(classes=classes, contents=row.map(cell => VNodeContents(td(classes=List("align-middle"), contents=cell))))
      })))
    ))
}
