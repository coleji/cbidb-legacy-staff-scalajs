package org.sailcbi.Pages.HomePage

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.StandardPage

class HomePageView(renderer: VNodeContents[_] => Unit) extends StandardPage[HomePageModel](renderer) {
  override val defaultModel: HomePageModel = new HomePageModel(None)

  override def getMain(model: HomePageModel): VNodeContents[_] = div("Main!")
}
