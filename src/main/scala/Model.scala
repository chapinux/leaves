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

object Model extends App {

  override def main(args: Array[String]) = apply(
    args(0).toDouble,
    args(1).toDouble, args(2).toDouble, math.toRadians(args(3).toDouble), args(4).toInt, args(5).toDouble,
    args(6).toDouble, args(7).toDouble, math.toRadians(args(8).toDouble), args(9).toInt, args(10).toDouble,
    args(11).toDouble, args(12).toDouble, math.toRadians(args(13).toDouble), args(14).toInt, args(15).toDouble,
    args(16).toDouble, args(17).toDouble, math.toRadians(args(18).toDouble), args(19).toInt, args(20).toDouble,
    args(21).toDouble, args(22).toDouble, math.toRadians(args(23).toDouble), args(24).toInt, args(25).toDouble
  )

  case class Level(
                    thickness: Double,
                    decreaseRate: Double,
                    angle: Double,
                    nbBifurcation: Int,
                    sterilityRate: Double
                  )

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

    val length = 10.0

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
      val nbSterile = ((nbBif * sterileRate) / 2).ceil.toInt
      (1 to nbBif).drop(nbSterile).dropRight(nbSterile)
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