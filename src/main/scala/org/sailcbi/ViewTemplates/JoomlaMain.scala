package org.sailcbi.ViewTemplates

import org.sailcbi.Core.{Model, View}
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.{VNodeContents, div}

abstract class JoomlaMain[T <: Model](renderer: VNode => Unit) extends View[T](renderer){
  def getMain(model: T): VNode

  //TODO: verify against apex
  def getVNode(model: T): VNode = div(classes=List("rt-container"), contents=VNodeContents(
    div(classes=List("rt-grid-12"), contents=VNodeContents(
      div(id="rt-main-column", classes=List("page-content-light"), contents=VNodeContents(
        div(classes=List("rt-block", "component-block"), style=Map("min-height"->"350px"), contents=VNodeContents(
          div(style=Map("position"->"absolute", "right"->"20px", "z-index"->"1000")),
          div(id="rt-mainbody", contents=VNodeContents(
            div(classes=List("component-content", "rt-joomla"), contents=VNodeContents(
              div(classes=List("rt-joomla"), contents=VNodeContents(
                getMain(model)
              ))
            ))
          ))
        ))
      ))
    ))
  ))


  /*
                            <div class="rt-container">
                                <div class="rt-grid-12">
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
                              </div>
                              <div class="clear"></div>
                            </div>
   */
}
