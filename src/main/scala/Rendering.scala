package leaves

import com.vividsolutions.jts.geom._
import com.vividsolutions.jts.geom.{Coordinate, Geometry, GeometryFactory}
import better.files._

import scalatags.Text.{svgAttrs, svgTags}
import scalatags.Text.all._

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
  val fact = new GeometryFactory

  def variableWidthBuffer(p0: Coordinate, p1: Coordinate, width0: Double, width1: Double): Geometry = {
    val seg = new LineSegment(p0, p1)
    val dist0 = width0 / 2
    val dist1 = width1 / 2
    val s0 = seg.pointAlongOffset(0, dist0)
    val s1 = seg.pointAlongOffset(1, dist1)
    val s2 = seg.pointAlongOffset(1, -dist1)
    val s3 = seg.pointAlongOffset(0, -dist0)
    fact.createGeometryCollection(Array(fact.createPoint(p0).buffer(width0 / 2.0), fact.createPoint(p1).buffer(width1 / 2.0), fact.createPolygon(Array(s0, s1, s2, s3, s0)))).union()
  }

  case class Vertex(vx: Double, vy: Double, thickness: Double = 1.0)

  case class Line(fromVertex: Vertex, toVertex: Vertex)

  def apply(lines: Seq[Line],
            polygons: Seq[Seq[Vertex]],
            outSVG: better.files.File) = {

    //JTS
    implicit def vertexToCoordinate(v: Vertex): Coordinate = new Coordinate(v.vx, v.vy)

    implicit def arrayVertexToArrayCoordinate(a: Array[Vertex]): Array[Coordinate] = a.map {
      vertexToCoordinate
    }

    val jtsGemoteries: Seq[Geometry] = polygons.map { p =>
      fact.createPolygon(p.seq.toArray :+ p.seq.head)
    } ++ lines.map { l =>
      variableWidthBuffer(l.fromVertex, l.toVertex, l.fromVertex.thickness / 2.0, l.toVertex.thickness)
      //fact.createLineString(Array(l.fromVertex, l.toVertex))
    }

    //    jtsGemoteries.foreach {
    //      println
    //    }
    val collection = fact.createGeometryCollection(jtsGemoteries.toArray).union
    println(collection.getArea)
    println(collection.getLength)


    //SVG
    val painter = leaves.svg.startPath(0, 0)
    val painterPath = lines.foldLeft(painter)((painter, line) => painter.m(line.fromVertex.vx, line.fromVertex.vy).l(line.toVertex.vx, line.toVertex.vy))

    val svgTag = svgTags.svg(
      svgAttrs.width := 800,
      svgAttrs.height := 800,
      svgAttrs.xmlns := "http://www.w3.org/2000/svg",
      for {
        p <- polygons
      } yield {
        svgTags.polygon(
          svgAttrs.stroke := "black",
          svgAttrs.fill := "yellow",
          svgAttrs.points := p.seq.foldLeft("") { (acc, v) => acc ++ s"${v.vx},${v.vy} " }
        )
      },
      svgTags.path(
        svgAttrs.stroke := "black",
        svgAttrs.d := painterPath.svgString
      )
    )

    outSVG.overwrite(svgTag.render)

  }
}
