package org.sailcbi.Core

import org.sailcbi.VNode.VNodeContents

abstract class View[T <: Model](val renderer: VNodeContents[_] => Unit) {
  val defaultModel: T
  def initialRender(): Unit = render(defaultModel)
  def render(model: T): Unit = {
    println("in render")
    renderer(getContent(model))
  }
  def getContent(m: T): VNodeContents[_]
}
