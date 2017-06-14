package leaves

import com.vividsolutions.jts.geom.{Coordinate, Geometry, GeometryFactory}
import better.files._
import scalatags.Text.{svgAttrs, svgTags}
import scalatags.Text.all._
import scalatags.Text.svgAttrs
import scalatags.Text.svgTags

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

object Rendering {


  case class Vertex(vx: Double, vy: Double)

  case class Line(fromVertex: Vertex, toVertex: Vertex)

  def apply(lines: Seq[Line],
            polygons: Seq[Seq[Vertex]],
            outSVG: better.files.File) = {

    //JTS
    implicit def vertexToCoordinate(v: Vertex): Coordinate = new Coordinate(v.vx, v.vy)
    implicit def arrayVertexToArrayCoordinate(a: Array[Vertex]): Array[Coordinate] = a.map {
      vertexToCoordinate
    }

    val fact = new GeometryFactory
    val jtsGemoteries: Seq[Geometry] = polygons.map { p =>
      fact.createPolygon(p.seq.toArray :+ p.seq.head)
    } ++ lines.map { l =>
      fact.createLineString(Array(l.fromVertex, l.toVertex))
    }

    jtsGemoteries.foreach {
      println
    }

    val collection = fact.createGeometryCollection(jtsGemoteries.toArray)
    println(collection.getArea)
    println(collection.getLength)


    //SVG
    val painter = leaves.svg.startPath(0, 0)
    val painterPath = lines.foldLeft(painter)((painter, line) => painter.m(line.fromVertex.vx, line.fromVertex.vy).l(line.toVertex.vx, line.toVertex.vy))

    val svgTag = svgTags.svg(
      svgAttrs.width := 800,
      svgAttrs.height := 800,
      svgAttrs.xmlns := "http://www.w3.org/2000/svg",
      svgTags.path(
        svgAttrs.stroke := "black",
        svgAttrs.d := painterPath.svgString
      ),
      for {
        p <- polygons
      } yield {
        svgTags.polygon(
          svgAttrs.stroke := "black",
          svgAttrs.fill := "yellow",
          svgAttrs.points := p.seq.foldLeft("") { (acc, v) => acc ++ s"${v.vx},${v.vy} " }
        )
      }
    )

    outSVG.overwrite(svgTag.render)

  }
}
