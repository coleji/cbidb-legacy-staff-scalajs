package org.sailcbi.Pages.ForgotPWPage

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.div
import org.sailcbi.ViewTemplates.JoomlaMain

class ForgotPWPageView(renderer: VNode => Unit) extends JoomlaMain[ForgotPWPageModel](renderer) {
  override val defaultModel: ForgotPWPageModel = new ForgotPWPageModel

  override def getMain(model: ForgotPWPageModel): VNode = div("Forgot PW!")
}
