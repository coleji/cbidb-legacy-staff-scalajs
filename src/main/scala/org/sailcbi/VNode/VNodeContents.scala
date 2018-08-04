package org.sailcbi.VNode

import org.sailcbi.VNode.SnabbdomFacade.VNode

import scala.scalajs.js

abstract class VNodeContents[T](val t: T) {
  def asJs: js.Any = {
    t.asInstanceOf[js.Any]
  }
}

object VNodeContents {
  // The first two cases look trivial, but they are are necessary to make sure
  // that the final case only happens on an actual >1 variadic call
  // and specifically, does not wrap an array of stuff into a one element outer array (which fucks all the conversions up)

  // Case 1: You're passing in a js.Array of things.  Cast it as VNodeContents and shoot it on through
  def apply(a: js.Array[VNodeContents[_]]): VNodeContents[js.Array[VNodeContents[_]]] = a
  // Case 2: You're passing a single thing that's not an array and already a VNodeContents.  Shoot that right on through (caller does the conversion)
  def apply(v: VNodeContents[_]): VNodeContents[_] = v
  // Case 3: You're passing a bunch of things variadically.  Make a js.Array out of your variadic args and pass the array on through
  def apply(v: VNodeContents[_]*): js.Array[VNodeContents[_]] = {
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