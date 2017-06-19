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
                    sterilityRate: Double
                  )

  // sterility rate: rate of nodes, which do not get any child. If the rate is positive, the sterile nodes are taken on the peripheric branches, in the center otherwise
  // Ex: nbBif = 3, sterilityRate = 0,66=> we keep the main one
  //     nbBif = 3, sterilityRate = -0,33, we keep the two peripheric branches

  def apply(
             alphaShape: Double,

             thickness0: Double,
             decreaseRate0: Double,
             angle0: Double,
             nbBifurcation0: Int,
             sterilityRate0: Double,

             thickness1: Double,
             decreaseRate1: Double,
             angle1: Double,
             nbBifurcation1: Int,
             sterilityRate1: Double,

             thickness2: Double,
             decreaseRate2: Double,
             angle2: Double,
             nbBifurcation2: Int,
             sterilityRate2: Double,

             thickness3: Double,
             decreaseRate3: Double,
             angle3: Double,
             nbBifurcation3: Int,
             sterilityRate3: Double,

             thickness4: Double,
             decreaseRate4: Double,
             angle4: Double,
             nbBifurcation4: Int,
             sterilityRate4: Double,

             render: Boolean = false
           ) = {

    val levels = Map(
      0 -> Level(thickness0, decreaseRate0, angle0, nbBifurcation0, sterilityRate0),
      1 -> Level(thickness1, decreaseRate1, angle1, nbBifurcation1, sterilityRate1),
      2 -> Level(thickness2, decreaseRate2, angle2, nbBifurcation2, sterilityRate2),
      3 -> Level(thickness3, decreaseRate3, angle3, nbBifurcation3, sterilityRate3),
      4 -> Level(thickness4, decreaseRate4, angle4, nbBifurcation4, sterilityRate4)
    )

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

    def fertile(nbBif: Int, sterileRate: Double): Seq[Int] = {
      val centrality = sterileRate > 0
      val bifs = (1 to nbBif)
      if (centrality) {
        val nbSterile = math.abs(((nbBif * sterileRate) / 2).ceil.toInt)
        bifs.drop(nbSterile).dropRight(nbSterile)
      }
      else {
        val toDrop = math.abs(nbBif * sterileRate)
        val o = bifs.dropWhile(i=> (i > toDrop) && (i < nbBif + toDrop))
       println("nb ster " +toDrop)
        println("O " + o)

        o
      }
    }

    def iter(curDepth: Int, currentDecrease: Double, curTurtle: Turtle): Unit = {
      if (curDepth < 5) {
        val curLevel = levels(curDepth)
        val currentRatio = curLevel.decreaseRate * currentDecrease
        val currentLength = length * currentRatio
        for (
          curBif <- 1 to curLevel.nbBifurcation
        ) yield {
          val angle = (curBif - ((curLevel.nbBifurcation) / 2) + shift(curLevel.nbBifurcation)) * curLevel.angle
          val newT = curTurtle.rotate(angle).move(currentLength, currentLength)
          val oldVertex = Vertex(curTurtle.position._1, curTurtle.position._2, curLevel.thickness)
          val newVertex = Vertex(newT.position._1, newT.position._2, nextThickness(curDepth))
          val curFertility = fertile(curLevel.nbBifurcation, curLevel.sterilityRate)
          lines = lines :+ Line(oldVertex, newVertex)
          vertices = vertices :+ newVertex
          if (curFertility.contains(curBif)) {
            iter(curDepth + 1, currentRatio, newT)
          }
        }
      }
    }

    iter(0, 1, turtle0)
    val shape = CharacteristicShape.fromLines(lines, alphaShape)
    if (render) Rendering(lines, Seq(shape.getExteriorRing.getCoordinates.map{c=> Vertex(c.x, c.y)}), File("model.svg"))
    (shape.getArea, shape.getLength)
  }
}