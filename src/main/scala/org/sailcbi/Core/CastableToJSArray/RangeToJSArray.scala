package org.sailcbi.Core.CastableToJSArray

import scala.scalajs.js

class RangeToJSArray(r: Range) {
  def toJSArray: js.Array[Int] = {
    val ret = new js.Array[Int]()
    r.foreach(i => ret.push(i))
    ret
  }
}

object RangeToJSArray {
  implicit def wrapRange(r: Range): RangeToJSArray = new RangeToJSArray(r)
}