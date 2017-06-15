import leaves.Rendering
import leaves.Rendering.{Line, Vertex}
import leaves.Vegetal.Turtle
import better.files._

import scala.annotation.tailrec
import scala.collection.immutable.Stack
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

  override def main(args: Array[String]) = apply(
    20.0,
    100.0,
    15.0, 0.95, math.toRadians(45), 1,
    12.0, 0.85, math.toRadians(45), 3,
    1.0, 0.75, math.toRadians(25), 2,
    12.0, 0.5, math.toRadians(1), 1,
    12.0, 0.19, math.toRadians(31), 5
  )

  case class Level(
                    thickness: Double,
                    decreaseRate: Double,
                    angle: Double,
                    nbBifurcation: Int
                  )

  def apply(
             alphaShape: Double,
             length: Double,

             thickness0: Double,
             decreaseRate0: Double,
             angle0: Double,
             nbBifurcation0: Int,

             thickness1: Double,
             decreaseRate1: Double,
             angle1: Double,
             nbBifurcation1: Int,

             thickness2: Double,
             decreaseRate2: Double,
             angle2: Double,
             nbBifurcation2: Int,

             thickness3: Double,
             decreaseRate3: Double,
             angle3: Double,
             nbBifurcation3: Int,

             thickness4: Double,
             decreaseRate4: Double,
             angle4: Double,
             nbBifurcation4: Int
           ) = {

    val levels = Map(
      0 -> Level(thickness0, decreaseRate0, angle0, nbBifurcation0),
      1 -> Level(thickness1, decreaseRate1, angle1, nbBifurcation1),
      2 -> Level(thickness2, decreaseRate2, angle2, nbBifurcation2),
      3 -> Level(thickness3, decreaseRate3, angle3, nbBifurcation3),
      4 -> Level(thickness4, decreaseRate4, angle4, nbBifurcation4)
    )

    val turtle0 = Turtle(500, 500, levels(0).angle, levels(0).decreaseRate)

    var lines: Seq[Line] = Seq()
    var vertices: Seq[Vertex] = Seq()

    def shift(n: Int) = {
      if ((n % 2) == 1) -1.0
      else -0.5
    }


    def iter(curDepth: Int, currentDecrease: Double, curTurtle: Turtle): Unit = {
      if (curDepth < 5) {
        println("Depth " + curDepth)
        val curLevel = levels(curDepth)
        val currentRatio = curLevel.decreaseRate * currentDecrease
        val currentLength = length * currentRatio
        for (
          curBif <- 1 to curLevel.nbBifurcation
        ) yield {
          val angle = (curBif - ((curLevel.nbBifurcation) / 2) + shift(curLevel.nbBifurcation)) * curLevel.angle
          val newT = curTurtle.rotate(angle).move(currentLength, currentLength )
          val newVertex = Vertex(newT.position._1, newT.position._2)
          lines = lines :+ Line(Vertex(curTurtle.position._1, curTurtle.position._2), newVertex)
          vertices = vertices :+ newVertex
          iter(curDepth + 1, currentRatio, newT)
        }
      }
    }

    iter(0, 1, turtle0)
    println(lines.size)
    Rendering(lines, Seq(), "/tmp" / "model.svg")
  }

}

