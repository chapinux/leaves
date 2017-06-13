package leaves

import Vegetal.Turtle

import scala.collection.immutable.Stack
import scala.math.{cos, sin}
import math.Pi
import scala.collection.mutable

/*
 * Copyright (C) 12/06/17 // mathieu.leclaire@openmole.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//@JSExport("leaves.SVG")
object SVG extends App {

  //@JSExport()
  override def main(args: Array[String]): Unit = {

    val delta = math.toRadians(20)
    val length = 10
    val nbIterations = 3


    case class MoveAndTrace(moveX: Double, moveY: Double, traceToX: Double, traceToY: Double)
    var moveAndTraces: Seq[MoveAndTrace] = Seq()

    def move_turtle(s: String, t: Turtle) = {
      val st = Stack[Turtle]()
      s.foldLeft((t, st)) { case ((head, stack), c) =>
        c match {
          case ('F') =>
            val previous_pos = head.position
            val newt = head.move((length * cos(head.heading)).toInt, (length * sin(head.heading)).toInt)
          //  println(s"FFF $previous_pos to ${newt.position}")
            moveAndTraces = moveAndTraces :+ MoveAndTrace(previous_pos._1, previous_pos._2, newt.position._1, newt.position._2)
            (newt, stack)
          case ('f') =>
            (head.move((length * cos(head.heading)).toInt, (length * sin(head.heading)).toInt), stack)
          case ('+') =>
            (head.rotate(delta), stack)
          case ('-') =>
            (head.rotate(-delta), stack)
          case ('[') =>
            (head, stack.push(head))
          case (']') =>
            stack.pop2
        }
      }
    }

    val leo = new Turtle(500, 500, 3 * 0.5 * Pi)
    val ss = Vegetal.generate(delta, length, nbIterations)
    move_turtle(ss, leo)

    val painter = svg.path.start(0, 0)
    val path = moveAndTraces.foldLeft(painter)((painter, mAt)=> painter.m(mAt.moveX, mAt.moveY).l(mAt.traceToX, mAt.traceToY))
    println(path.end.svgString)
  }
}
