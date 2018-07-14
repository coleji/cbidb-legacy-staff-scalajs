package org.sailcbi.ViewTemplates

import org.sailcbi.Core.{Model, View}
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.{VNodeContents, div}

abstract class Joomla84[T <: Model](renderer: VNode => Unit) extends View[T](renderer){
  def get8(model: T): VNode
  def get4(model: T): VNode

  //TODO: verify against apex
  def getVNode(model: T): VNode = div(classes=List("rt-container"), contents=VNodeContents(
    div(classes=List("rt-grid-8"), contents=VNodeContents(
      div(id="rt-main-column", classes=List("page-content-light"), contents=VNodeContents(
        div(classes=List("rt-block", "component-block"), style=Map("min-height"->"350px"), contents=VNodeContents(
          div(style=Map("position"->"absolute", "right"->"20px", "z-index"->"1000")),
          div(id="rt-mainbody", contents=VNodeContents(
            div(classes=List("component-content", "rt-joomla"), contents=VNodeContents(
              div(classes=List("rt-joomla"), contents=VNodeContents(
                get8(model)
              ))
            ))
          )),
          div(classes=List("clear"))
        ))
      ))
    )),
    div(classes=List("rt-grid-4"), contents=VNodeContents(
      div(classes=List("rt-sidebar-surround", "page-block"), contents=VNodeContents(
        div(id="rt-sidebar-a", contents=VNodeContents(
          get4(model)
        ))
      ))
    ))
  ))

  /*
                            <div class="rt-container">
                                <div class="rt-grid-8">
                                    <div id="rt-main-column" class="page-content-light">
                                        <div class="rt-block component-block" style="min-height:350px;">
                                            <div style="position:absolute; right:20px; z-index:1000;">
                                            </div>
                                            <div id="rt-mainbody">
                                                <div class="component-content rt-joomla">
                                                    <div class="rt-joomla ">

                                                    </div>
                                                </div>
                                            </div>
                                            <div class="clear"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="rt-grid-4">
                                    <div class="rt-sidebar-surround page-block">
                                        <div id="rt-sidebar-a">

                                        </div>
                                    </div>
                                </div>
                                <div class="clear"></div>
                            </div>
   */
}
