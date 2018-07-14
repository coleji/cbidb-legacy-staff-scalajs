package org.sailcbi.Pages.Test2Page

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.div
import org.sailcbi.ViewTemplates.JoomlaMain

class Test2PageView(renderer: VNode => Unit) extends JoomlaMain[Test2PageModel](renderer) {
  override val defaultModel: Test2PageModel = new Test2PageModel

  override def getMain(model: Test2PageModel): VNode = div("Test 2!")
}
