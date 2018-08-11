package org.sailcbi.Pages.SpotPage

import org.sailcbi.Core.{Message, View}
import org.sailcbi.Pages.SpotPage.SpotPageModel._
import org.sailcbi.VNode.SnabbdomFacade.VNode
import org.sailcbi.VNode._
import org.sailcbi.ViewTemplates.StandardPage

import scala.scalajs.js


class SpotPageView(renderer: VNodeContents[_] => Unit) extends StandardPage[SpotPageModel](renderer) {
  override val defaultModel: SpotPageModel = SpotPageModel.startingBoard(7)

  object Highlight extends Message[SpotPageModel, Square] {
    def update: SpotPageModel => Square => SpotPageModel =
      model => square => model.highlight(square)
  }

  object Move extends Message[SpotPageModel, Square] {
    def update: SpotPageModel => Square => SpotPageModel =
      model => square => {
        println("attempting a move")
        model.move(square)
      }
  }

  override def getMain(model: SpotPageModel): VNodeContents[_] = div(contents=VNodeContents(
    table(props=Map("cellpadding" -> "5"), contents=tbody(contents=
      VNodeContents(model.board.map(row => tr(
        VNodeContents(row.map(c => {
          val color = c.owner match {
            case NoOwner => "white"
            case P1 => if (model.highlighted.isDefined && model.highlighted.get == c) "#128ced" else "#0c5a99"
            case P2 => if (model.highlighted.isDefined && model.highlighted.get == c) "#d41a3e" else "#8c1129"
          }
          val events: Map[String, js.Any] = c.owner match {
            case NoOwner => {
              if (model.highlighted.isDefined && model.turn == model.highlighted.get.owner) Map("click" -> (() => Move(this)(model)(c)))
              else Map.empty
            }
            case P1 => {
              if (model.turn == P1) Map("click" -> (() => Highlight(this)(model)(c)))
              else Map.empty
            }
            case P2 => {
              if (model.turn == P2) Map("click" -> (() => Highlight(this)(model)(c)))
              else Map.empty
            }
          }
          td(
            events=events,
            style=Map("height" -> "60px", "width" -> "60px", "border" -> "1px solid black", "background-color" -> color)
          ): VNodeContents[_]
        }))
      ): VNodeContents[_]))
    )),
    span(if (model.turn == P1) "P1" else "P2")
  ))
}
