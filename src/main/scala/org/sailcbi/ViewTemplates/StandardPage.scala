package org.sailcbi.ViewTemplates

import org.sailcbi.Core.{Model, View}
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.StandardPage.{aside, header, main, section}

abstract class StandardPage[T <: Model](renderer: VNodeContents[_] => Unit) extends View[T](renderer){
  def getMain(model: T): VNodeContents[_]

  def getContent(model: T): VNodeContents[_] = {
    val centerContents = true
    val style: Map[String, String] = if (centerContents) Map("max-width"->"720px") else Map.empty
    VNodeContents(
      header(classes=List("app-header"), contents = VNodeContents(
        div(classes=List("top-bar"), contents=VNodeContents(
          div(classes=List("top-bar-brand"), contents=VNodeContents(
            aPlaceholder(VNodeContents(
              img(props=Map("src"->"CBI_horizOnWhite_color_small.jpg", "height"->"32"))
            ))
          ))
        ))
      )),
      aside(classes=List("app-aside"), contents=VNodeContents(
        div(classes=List("aside-content"), contents=VNodeContents(
          header(classes=List("aside-header", "d-block", "d-md-none")),
          section(classes=List("aside-menu", "has-scrollable"))
        ))
      )),
      main(classes=List("app-main"), contents=VNodeContents(
        div(classes=List("wrapper"), contents=VNodeContents(
          div(classes=List("page"), contents=VNodeContents(
            div(classes=List("page-inner"), contents=VNodeContents(
              header(classes=List("page-title-bar")),
              div(classes=List("page-section"), style=style, contents=getMain(model))
            ))
          ))
        ))
      ))
    )
  }

}

object StandardPage {
  case object header extends VNodeConstructor("header")
  case object aside extends VNodeConstructor("aside")
  case object main extends VNodeConstructor("main")
  case object section extends VNodeConstructor("section")
}