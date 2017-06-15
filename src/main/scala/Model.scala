import leaves.Rendering
import leaves.Rendering.{Line, Vertex}
import leaves.Vegetal.Turtle
import better.files._

import scala.math.{cos, sin}

/*
 * Copyright (C) 14/06/17 // mathieu.leclaire@openmole.org
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

object Model extends App {

  override def main(args: Array[String]) = apply(100.0, 2.0, 0.5, math.toRadians(15), 12.0, 5, 5)

  def apply(
             length0: Double,
             thickness0: Double,
             decreaseRate: Double,
             angle: Double,
             alphaShape: Double,
             nbBifurcation: Int,
             depth: Int
           ) = {

    val turtle0 = Turtle(500, 500, angle, decreaseRate)

    var lines: Seq[Line] = Seq()
    var vertices: Seq[Vertex] = Seq()

    def shift(n: Int) = {
      if ((n % 2) == 1) -1.0
      else -0.5
    }

    def iter(curDepth: Int, curTurtle: Turtle): Unit = {
      val currentRatio = math.pow(decreaseRate, curDepth)
      val currentLength = length0 * currentRatio
      val currentThickness = thickness0 * currentRatio
      val previousThickness = thickness0 * math.pow(decreaseRate, curDepth - 1)
      if (curDepth < depth) {
        for (
          curBif <- 1 to nbBifurcation
        ) yield {
          val nA = (curBif  - ((nbBifurcation) / 2) + shift(nbBifurcation))
          val newT = curTurtle.rotate(angle * nA).move(currentLength * cos(curTurtle.heading), currentLength * sin(curTurtle.heading))
          val newVertex = Vertex(newT.position._1, newT.position._2, currentThickness)
          lines = lines :+ Line(Vertex(curTurtle.position._1, curTurtle.position._2, previousThickness), newVertex)
          vertices = vertices :+ newVertex
          iter(curDepth + 1, newT)
        }
      }
    }

    iter(1, turtle0)
    Rendering(lines, Seq(), "/tmp" / "model.svg")
    println(CharacteristicShape(vertices, 100.0))
  }

}

