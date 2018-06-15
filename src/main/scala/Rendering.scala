package leaves

import org.locationtech.jts.geom._
import org.locationtech.jts.geom.{Coordinate, Geometry, GeometryFactory}
import org.locationtech.jts.precision.GeometryPrecisionReducer
import scalatags.Text.{svgAttrs, svgTags}
import scalatags.Text.all._

import scala.util.{Failure, Success, Try}

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

  def variableWidthBuffer(p0: Coordinate, p1: Coordinate, width0: Double, width1: Double, length:Option[Double] = None): Geometry = {
    val seg = new LineSegment(p0, p1)
    val dist0 = width0 / 2
    val dist1 = width1 / 2
    val s0 = seg.pointAlongOffset(0, dist0)
    val s1 = seg.pointAlongOffset(1, dist1)
    val s2 = seg.pointAlongOffset(1, -dist1)
    val s3 = seg.pointAlongOffset(0, -dist0)
    val segLength = s0.distance(s1)
    val nbSegments = (segLength / length.getOrElse(segLength)).toInt
    val ss0x = (s1.x - s0.x) / nbSegments
    val ss0y = (s1.y - s0.y) / nbSegments
    val ss0 = (1 until nbSegments).map(i=>new Coordinate(s0.x + i * ss0x, s0.y + i * ss0y))
    val ss1x = (s3.x - s2.x) / nbSegments
    val ss1y = (s3.y - s2.y) / nbSegments
    val ss1 = (1 until nbSegments).map(i=>new Coordinate(s2.x + i * ss1x, s2.y + i * ss1y))
    fact.createGeometryCollection(Array(
      fact.createPoint(p0).buffer(width0 / 2.0),
      fact.createPoint(p1).buffer(width1 / 2.0),
      fact.createPolygon(Array(s0) ++ ss0 ++ Array (s1, s2) ++ ss1 ++ Array(s3, s0)))).union()
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

//    val jtsGeometries: Seq[Geometry] = polygons.map { p =>
//      fact.createPolygon(p.seq.toArray :+ p.seq.head)
//    } ++ lines.map { l =>
//      variableWidthBuffer(l.fromVertex, l.toVertex, l.fromVertex.thickness, l.toVertex.thickness)
//    }
    val jtsGeometries = lines.map(l=>Rendering.variableWidthBuffer(
          new Coordinate(l.fromVertex.vx,l.fromVertex.vy),
          new Coordinate(l.toVertex.vx,l.toVertex.vy),
          l.fromVertex.thickness, l.toVertex.thickness, None
        ))
    val array = jtsGeometries.toArray
//    val array = lines.map(l=>Rendering.variableWidthBuffer(
//      new Coordinate(l.fromVertex.vx,l.fromVertex.vy),
//      new Coordinate(l.toVertex.vx,l.toVertex.vy),
//      l.fromVertex.thickness, l.toVertex.thickness, None
//    )).toArray

    val collection = fact.createGeometryCollection(array)
    val unionT = Try{collection.union}
    val union = unionT match {
      case Success(u) => u
      case Failure(f) => {
        val gpr = new GeometryPrecisionReducer(new PrecisionModel(1000))
        fact.createGeometryCollection(array.map(gpr.reduce)).union
      }
    }

    //println(union.getArea)
    //println(union.getLength)

    val minX = union.getEnvelopeInternal.getMinX
    val minY = union.getEnvelopeInternal.getMinY
    //SVG
    val painter = leaves.svg.startPath(0, 0)
    val painterPath = lines.foldLeft(painter)((painter, line) => painter.m(line.fromVertex.vx-minX, line.fromVertex.vy-minY).l(line.toVertex.vx-minX, line.toVertex.vy-minY))

    val svgTag = svgTags.svg(
      svgAttrs.width := "100%",
      svgAttrs.height := "100%",
      svgAttrs.xmlns := "http://www.w3.org/2000/svg",
      for {
        p <- polygons
      } yield {
        val translated = p.seq.map(v=>(v.vx-minX, v.vy-minY))
        svgTags.polygon(
          svgAttrs.stroke := "black",
          svgAttrs.fill := "yellow",
          svgAttrs.points := translated.foldLeft("") { (acc, v) => acc ++ s"${v._1},${v._2} " }
        )
      },
      svgTags.path(
        svgAttrs.stroke := "black",
        svgAttrs.d := painterPath.svgString
      )
    )

    outSVG.overwrite(svgTag.render)

  }

  def main(args: Array[String]): Unit = {
    println(variableWidthBuffer(new Coordinate(0.0,0.0), new Coordinate(10.0,0.0), 0.1, 0.5))
  }
}
