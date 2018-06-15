package leaves

import leaves.Rendering.{Line, Vertex}
import leaves.Vegetal.Turtle
import better.files.File

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

object Model {

  case class Level(
                    thickness: Double,
                    decreaseRate: Double,
                    angle: Double,
                    nbBifurcation: Int,
                    alphaRate: Double
                  )

// alphaRate: Rate on the initial length below which the number of bifurcations is 1.

  def apply(
             alphaShape: Double,
             thickness: Double,
             decreaseRate: Double,
             angle: Double,
             nbBifurcation: Int,
             alphaRate: Double,
             depth: Int,
             render: Boolean = false
           ) = {

    val levels = (0 to depth).map{_-> Level(thickness, decreaseRate, angle, nbBifurcation, alphaRate)}.toMap

    val turtle0 = Turtle(200, 200, levels(0).angle, levels(0).decreaseRate)

    val length = 100.0

    var lines: Seq[Line] = Seq()
    var vertices: Seq[Vertex] = Seq()

    def shift(n: Int) = {
      if ((n % 2) == 1) -1.0
      else -0.5
    }

    def nextThickness(d: Int) = {
      if (d == 4) levels(4)
      else levels(d + 1)
    }.thickness


    def iter(curDepth: Int, currentDecrease: Double, curTurtle: Turtle): Unit = {
      if (curDepth < depth) {
        val curLevel = levels(curDepth)
        val currentRatio = curLevel.decreaseRate * currentDecrease
        val currentLength = length * currentRatio
        println("Cur lenght " + currentLength)
        val curBif = {
          if (currentLength < length * alphaRate) 1
          else curLevel.nbBifurcation
        }

        for (
          curBif <- 1 to curBif
        ) yield {
          val angle = (curBif - ((curLevel.nbBifurcation) / 2) + shift(curLevel.nbBifurcation)) * curLevel.angle
          val newT = curTurtle.rotate(angle).move(currentLength, currentLength)
          val oldVertex = Vertex(curTurtle.position._1, curTurtle.position._2, curLevel.thickness)
          val newVertex = Vertex(newT.position._1, newT.position._2, nextThickness(curDepth))
          lines = lines :+ Line(oldVertex, newVertex)
          vertices = vertices :+ newVertex
            iter(curDepth + 1, currentRatio, newT)
        }
      }
    }

    iter(0, 1, turtle0)
    val shape = CharacteristicShape.fromLines(lines, alphaShape, Some(6))
    if (render) Rendering(lines, Seq(shape.getExteriorRing.getCoordinates.map{c=> Vertex(c.x, c.y)}), File("model.svg"))
    (shape.getArea, shape.getLength)
  }
}