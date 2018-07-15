package org.sailcbi.VNode

import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode.SnabbdomFacade.snabbdom.h

import scala.scalajs.js

abstract class VNodeConstructor(tag: String) {
  def apply[T](c: VNodeContents[T]): VNode = apply(contents = c)
  def apply[T](
    contents: VNodeContents[T] = null,
    id: String = "",
    classes: List[String] = Nil,
    props: Map[String, String] = Map.empty,
    style: Map[String, String] = Map.empty,
    events: Map[String, js.Any] = Map.empty
  ): VNode = {
    // E.g. tag#id.class1.class2.class3
    val firstArg = List(
      tag,
      if (id != "") "#" + id else "",
      classes.map("." + _).mkString("")
    ).mkString("")

    val unifiedProps: js.Object = js.Dynamic.literal(
      "props" -> VNodeConstructor.MapToJsDictionary(props),
      "style" -> VNodeConstructor.MapToJsDictionary(style),
      "on" -> VNodeConstructor.MapToJsDictionary(events)
    )
    contents match {
      case null => h(firstArg, unifiedProps)
      case _ => h(firstArg, unifiedProps, contents.asJs)
    }
  }
}

object VNodeConstructor {
  def MapToJsDictionary(map: Map[String, _]): js.Object = {
    val result = js.Dictionary.empty[Any]
    for (pair <- map) {
      result(pair._1) = pair._2.asInstanceOf[js.Any]
    }
    result.asInstanceOf[js.Object]
  }
}

case object a extends VNodeConstructor("a")
case object b extends VNodeConstructor("b")
case object br extends VNodeConstructor("br")
case object button extends VNodeConstructor("button")
case object div extends VNodeConstructor("div")
case object form extends VNodeConstructor("form")
case object h1 extends VNodeConstructor("h1")
case object h2 extends VNodeConstructor("h2")
case object h3 extends VNodeConstructor("h3")
case object input extends VNodeConstructor("input")
case object img extends VNodeConstructor("img")
case object label extends VNodeConstructor("label")
case object li extends VNodeConstructor("li")
case object option extends VNodeConstructor("option")
case object select extends VNodeConstructor("select")
case object span extends VNodeConstructor("span")
case object table extends VNodeConstructor("table")
case object tbody extends VNodeConstructor("tbody")
case object td extends VNodeConstructor("td")
case object th extends VNodeConstructor("th")
case object thead extends VNodeConstructor("thead")
case object tr extends VNodeConstructor("tr")
case object ul extends VNodeConstructor("ul")
case object aPlaceholder extends VNodeConstructor("a")