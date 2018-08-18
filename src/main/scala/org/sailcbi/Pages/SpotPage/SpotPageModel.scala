package org.sailcbi.Pages.SpotPage

import org.sailcbi.Core.Model
import org.sailcbi.Pages.SpotPage.SpotPageModel._
import org.sailcbi.Core.CastableToJSArray.RangeToJSArray.wrapRange
import org.sailcbi.Core.Main.Globals

import scala.scalajs.js


/*
  (0,0)    |   (0,1)
           |
-------------------------
   (1,0)   |    (1,1)
           |
*/
// board is array of rows (row is array of column cells)
case class SpotPageModel(turn: Owner, board: js.Array[js.Array[Square]], highlighted: Option[Square]) extends Model {
  val somethingIsHighlighted: Boolean = highlighted.isDefined

  val nextTurn: Owner = turn match {
    case P1 => P2
    case P2 => P1
  }

  lazy val squareToCoords: Map[Square, Option[(Int, Int)]] = {
    board.zipWithIndex.flatMap(t => {
      t._1.zipWithIndex.map(u => {
        (u._1, Some(t._2, u._2))
      })
    }).toMap
  }

  lazy val highlightedCoordinates: Option[(Int, Int)] = highlighted match {
    case Some(s) => squareToCoords(s)
    case None => None
  }

  def serializeBoard(): String = {
    board.map(row => row.map(s => s.owner.toString).join("")).mkString(":")
  }

  def movesAway(from: Square, away: Int): Set[Square] = {
    def squareList(i: Int, away: Int): Seq[Int] = (i-away to i+away).toList.filter(r => r >= 0 && r < board.length)

    squareToCoords(from) match {
      case None => Set.empty
      case Some((row, col)) => {
        val rows = squareList(row, away)
        val cols = squareList(col, away)
        rows.flatMap(r => cols.map(c => (r,c)))
          .map(t => board(t._1)(t._2))
          .toSet

      }
    }
  }

  lazy val oneAwayFromHighlight: Set[Square] = highlighted match {
    case Some(h) => movesAway(h, 1)
    case None => Set.empty
  }
  lazy val twoAwayFromHighlight: Set[Square] = highlighted match {
    case Some(h) => movesAway(h, 2)
    case None => Set.empty
  }

  def highlight(s: Square): SpotPageModel = {
    highlighted match {
      case None => SpotPageModel(turn, board, Some(s))
      case Some(h) =>
        if (h == s) SpotPageModel(turn, board, None)
        else if (h.owner == turn) SpotPageModel(turn, board, Some(s))
        else this
    }
  }

  def move(s: Square): SpotPageModel = {
    isValidMove(s, turn) match {
      case Invalid => {
        Globals.console.log("invalid move! player turn: " + turn + " highlighted: " + this.highlighted)
        Globals.console.log(this.serializeBoard())
        this
      }
      case Copy => SpotPageModel(nextTurn, board.map(row => {
        val takeOverSet = movesAway(s, 1)
        row.map(cell => {
          if (cell == s) new Square(turn)
          else if (takeOverSet.contains(cell) && cell.owner == nextTurn) new Square(turn)
          else cell
        })
      }), None)
      case Jump => SpotPageModel(nextTurn, board.map(row => {
        val takeOverSet = movesAway(s, 1)
        row.map(cell => {
          if (cell == s) new Square(turn)
          else if (cell == highlighted.get) new Square(NoOwner)
          else if (takeOverSet.contains(cell) && cell.owner == nextTurn) new Square(turn)
          else cell
        })
      }), None)
      case _ => this
    }
  }

  def highlightAndMove(from: Square, to: Square): SpotPageModel = {
    val highlightedModel = this.highlight(from)
    highlightedModel.move(to)
  }

  def isValidMove(s: Square, owner: Owner): Move = {
    if (oneAwayFromHighlight.contains(s)) Copy
    else if (twoAwayFromHighlight.contains(s)) Jump
    else Invalid
  }
}

object SpotPageModel {
  class Square(val owner: Owner)

  abstract class Owner
  case object NoOwner extends Owner {
    override def toString: String = "0"
  }
  case object P1 extends Owner {
    override def toString: String = "1"
  }
  case object P2 extends Owner {
    override def toString: String = "2"
  }

  abstract class SquareState
  case object Normal extends SquareState
  case object Highlighted extends SquareState

  abstract class Move
  case object Invalid extends Move
  case object Copy extends Move
  case object Jump extends Move

  def startingBoard(edgeSize: Int): SpotPageModel = SpotPageModel(turn = P1, board = {
    (0 until edgeSize).toJSArray.map(c => {
      (0 until edgeSize).toJSArray.map(r => {
        if ((c == 0 && r == 0) || (c == edgeSize-1 && r == edgeSize-1)) {
          new Square(owner = P1)
        } else if ((c == 0 && r == edgeSize-1) || (c == edgeSize-1 && r == 0)) {
          new Square(owner = P2)
        } else {
          new Square(owner = NoOwner)
        }
      })
    })
  }, None)

  def deserializeBoard(s: String): js.Array[js.Array[Square]] = {
    import js.JSConverters._
    val rows: js.Array[String] = s.split(":").toJSArray
    rows.map(ss => ss.toCharArray.map(_.toString).map({
      case "1" => new Square(P1)
      case "2" => new Square(P2)
      case "0" => new Square(NoOwner)
    }).toJSArray)
  }
}