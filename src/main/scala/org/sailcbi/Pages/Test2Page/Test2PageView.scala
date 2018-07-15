package org.sailcbi.Pages.Test2Page

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.{VNodeContents, div}
import org.sailcbi.ViewTemplates.StandardPage

class Test2PageView(renderer: VNodeContents[_] => Unit) extends StandardPage[Test2PageModel](renderer) {
  override val defaultModel: Test2PageModel = new Test2PageModel

  override def getMain(model: Test2PageModel): VNodeContents[_] = div("Test 2!")
}
