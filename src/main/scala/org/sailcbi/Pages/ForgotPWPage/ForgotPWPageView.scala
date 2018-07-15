package org.sailcbi.Pages.ForgotPWPage

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.{VNodeContents, div}
import org.sailcbi.ViewTemplates.StandardPage

class ForgotPWPageView(renderer: VNodeContents[_] => Unit) extends StandardPage[ForgotPWPageModel](renderer) {
  override val defaultModel: ForgotPWPageModel = new ForgotPWPageModel

  override def getMain(model: ForgotPWPageModel): VNodeContents[_] = div("Forgot PW!")
}
