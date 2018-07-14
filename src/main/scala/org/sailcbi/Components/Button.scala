package org.sailcbi.Components

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._

object Button {
  def apply(text: String, onClick: () => Unit): VNode =
    aPlaceholder(classes=List("readon"), style=Map("margin"->"0 5px"), contents=
      span(text, events=Map("click"->(() => onClick())))
    )
}
