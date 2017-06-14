package leaves

import Rendering._
import better.files._
import Vegetal.Turtle
import scala.collection.immutable.Stack
import scala.math.{cos, sin}
import math.Pi

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
object Grammar1 extends App {

  //@JSExport()
  override def main(args: Array[String]): Unit = {

    val delta = math.toRadians(18)
    val length = 10.0
    val nbIterations = 10


    var lines: Seq[Line] = Seq()
    var vertices: Seq[Vertex] = Seq()
    var polygons: Seq[Seq[Vertex]] = Seq()

    def move_turtle(s: String, t: Turtle) = {
      val st = Stack[Turtle]()


      s.foldLeft((t, st)) { case ((head, stack), c) =>
        c match {
          case ('F') =>
            val previous_pos = head.position
            val newt = head.move((length * cos(head.heading)), (length * sin(head.heading)))
            //  println(s"FFF $previous_pos to ${newt.position}")
            lines = lines :+ Line(Vertex(previous_pos._1, previous_pos._2), Vertex(newt.position._1, newt.position._2))
            (newt, stack)
          case ('f') => (head.move((length * cos(head.heading)), (length * sin(head.heading))), stack)
          case ('+') => (head.rotate(delta), stack)
          case ('-') => (head.rotate(-delta), stack)
          case ('[') => (head, stack.push(head))
          case (']') => stack.pop2
          case ('{') =>
            vertices = Seq()
            (head, stack)
          case ('}') =>
            polygons = polygons :+ vertices
            (head, stack)
          case ('.') =>
            vertices = vertices :+ new Vertex(head.position._1, head.position._2)
            (head, stack)
          case _ => (head, stack)
        }
      }
    }

    val leo = new Turtle(500, 500, 3 * 0.5 * Pi)
    val ss = Vegetal.generate(delta, length, nbIterations)
    move_turtle(ss, leo)

    Rendering(lines, polygons, "/tmp" / "out.svg")
  }
}
