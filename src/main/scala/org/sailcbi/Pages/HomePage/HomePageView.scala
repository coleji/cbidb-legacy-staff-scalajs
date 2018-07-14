package org.sailcbi.Pages.HomePage

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.JoomlaMain

class HomePageView(renderer: VNode => Unit) extends JoomlaMain[HomePageModel](renderer) {
  override val defaultModel: HomePageModel = new HomePageModel(None)

  override def getMain(model: HomePageModel): VNode = div("Main!")
}
