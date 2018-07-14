package org.sailcbi.Core

import org.sailcbi.VNode.SnabbdomFacade.VNode

abstract class View[T <: Model](val renderer: VNode => Unit) {
  val defaultModel: T
  def initialRender(): Unit = render(defaultModel)
  def render(model: T): Unit = {
    renderer(getVNode(model))
  }
  def getVNode(m: T): VNode
}
