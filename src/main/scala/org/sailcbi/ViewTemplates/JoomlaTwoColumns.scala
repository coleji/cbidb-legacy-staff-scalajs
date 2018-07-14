package org.sailcbi.ViewTemplates

import org.sailcbi.Core.{Model, View}
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._

abstract class JoomlaTwoColumns[T <: Model](renderer: VNode => Unit) extends View[T](renderer){
  def getLeft(model: T): VNodeContents[_]
  def getRight(model: T): VNodeContents[_]

  //TODO: verify against apex
  def getVNode(model: T): VNode = {
    div(classes=List("rt-container"), contents=VNodeContents(
      div(classes=List("rt-grid-12"), contents=VNodeContents(
        div(id="rt-main-column", classes=List("page-content-light"), contents=VNodeContents(
          div(classes=List("rt-block", "component-block"), style=Map("min-height"->"350px"), contents=VNodeContents(
            div(style=Map("position"->"absolute", "right"->"20px", "z-index"->"1000")),
            div(id="rt-mainbody", contents=VNodeContents(
              div(classes=List("component-content", "rt-joomla"), contents=VNodeContents(
                div(classes=List("rt-joomla"), contents=
                  table(style=Map("width"->"100%"), contents=
                    tbody(contents=
                      tr(contents=VNodeContents(
                        td(style=Map("width"->"46%", "vertical-align"->"top"), contents=getLeft(model)),
                        td(style=Map("width"->"8%", "vertical-align"->"top")),
                        td(style=Map("vertical-align"->"top"), contents=getRight(model))
                      ))
                    )
                  )
                )
              ))
            ))
          ))
        ))
      ))
    ))
  }
}
