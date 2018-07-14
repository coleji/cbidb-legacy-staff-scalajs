package org.sailcbi.VNode

import org.sailcbi.VNode.SnabbdomFacade.VNode

import scala.scalajs.js

abstract class VNodeContents[T](t: T) {
  def asJs: js.Any = {
    t.asInstanceOf[js.Any]
  }
}

object VNodeContents {
  def apply(v: VNodeContents[_]*): VNodeContents[js.Array[VNodeContents[_]]] = {
    import js.JSConverters._
    v.toJSArray
  }
  implicit class StringAsContents(s: String) extends VNodeContents[String](s)
  implicit class IntAsContents(i: Int) extends VNodeContents[Int](i)
  implicit class VNodeAsContents(v: VNode) extends VNodeContents[VNode](v)
  implicit class ContentsArrayAsContents(a: js.Array[VNodeContents[_]]) extends VNodeContents[js.Array[VNodeContents[_]]](a){
    override def asJs: js.Any = {
      try {
        a.map(_.asJs)
      } catch {
        case _: Throwable => a.asInstanceOf[js.Any]
      }
    }
  }
  implicit object NullAsContents extends VNodeContents[Null](null)
}