package org.sailcbi.Core

import monix.execution.Scheduler.Implicits.global
import org.sailcbi.VNode.SnabbdomFacade.{VNode, patch, toVNode}
import org.sailcbi.VNode.{VNodeContents, div}
import org.scalajs.dom.document

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobalScope


object Main {

  @js.native
  class Target extends js.Object {
    val value: js.Any = js.native
    val selectedOptions: js.Array[Target] = js.native
  }


  @js.native
  @JSGlobalScope
  object Globals extends js.Object {
    var testThing: js.Object = js.native
    var testFn: js.Function0[Any] = js.native
    object window extends js.Object {
      object location extends js.Object {
        def pathname: String = js.native
        def host: String = js.native
        def hostname: String = js.native
        def protocol: String = js.native
      }
      var onpopstate: js.Any = js.native
      val alert: js.Function1[Any, Any] = js.native
      def setTimeout(f: js.Function0[Any], t: Int): Unit = js.native
    }
    object history extends js.Object {
      def pushState(state: js.Object, title: String, path: String): Unit = js.native
    }
    object console extends js.Object {
      def log(s: String): Unit = js.native
    }
  }

  val dropdownNull: String = "%null%"

  val BASE_LOCATION: String = Globals.window.location.protocol + "//" + Globals.window.location.host
  val API_LOCATION: String = BASE_LOCATION + "/api"

  var rootElement: VNode = toVNode(document.getElementById("root"))
  def updateRootElement(newRoot: VNode): Unit = {
    patch(rootElement, newRoot)
    rootElement = newRoot
  }

  def makeRoot(contents: VNodeContents[_]): VNode =
    div(id="root", classes=List("app"), contents = contents)

  def main(args: Array[String]): Unit = {
    println("starting on path " + Globals.window.location.pathname)
    println("host: " + Globals.window.location.host)
    println("hostname: " + Globals.window.location.hostname)
    println("protocol: " + Globals.window.location.protocol)
    println("111222")
    println(API_LOCATION)
    Globals.window.onpopstate = () => {
      println("rendering after popstate")
      Router.route(Globals.window.location.pathname).map(p => {
        p.initialRender()
      })
    }
    Router.renderer.set((view: VNodeContents[_]) => updateRootElement(makeRoot(view)))
    Router.route(Globals.window.location.pathname).map(p => {
      println("rendering!")
      p.initialRender()
    })
  }
}
