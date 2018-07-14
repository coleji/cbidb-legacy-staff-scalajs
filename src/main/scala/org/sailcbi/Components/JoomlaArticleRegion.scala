package org.sailcbi.Components

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._

object JoomlaArticleRegion {
  def apply[T, U](id: String, title: VNodeContents[T], body: VNodeContents[U]): VNode =
    div(id=id, classes=List("rt-article"), contents=VNodeContents(
      div(classes=List("article-header"), contents=
        h2(contents=title)
      ),
      div(classes=List("article-body"), contents=body),
      div(classes=List("article-buttons"), style=Map("margin-top"->"15px"))
    ))
}
