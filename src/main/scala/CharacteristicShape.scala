package  leaves

import org.locationtech.jts.geom._
import org.locationtech.jts.operation.linemerge.LineMerger
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder
import org.locationtech.jts.triangulate.quadedge.{QuadEdge, QuadEdgeTriangle, Vertex => QuadVertex}
import leaves.Rendering.{Line => LeavesLine, Vertex => LeavesVertex}

import collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object CharacteristicShape {
  val factory = new GeometryFactory
  class Border (var border:Boolean = false)
  class Vertex (val id:Int, val coordinate:Coordinate, border:Boolean = false) extends Border(border)
  class Edge (val id:Int, val geometry:LineSegment, border:Boolean, val oV:Vertex, val eV:Vertex, var triangles:ArrayBuffer[Triangle] = ArrayBuffer(), var incidentEdges:ArrayBuffer[Edge] = ArrayBuffer()) extends Border(border)
  class Triangle (val id:Int, var border:Boolean, var edges:ArrayBuffer[Edge] = ArrayBuffer(), var neighbours:ArrayBuffer[Triangle] = ArrayBuffer())
  //Characteristic shape algorithm
  def apply(inputVertices: Seq[(Double,Double)], threshold: Double, tolerance: Option[Double] = None):Polygon = {
    if (inputVertices.size < 3) factory.createPolygon(Array[Coordinate]())
    else {
    //println(factory.createMultiPoint(inputVertices.map (v => new Coordinate(v._1, v._2)).toArray))
      //Construct the Delaunay triangulation ∆ of P
      val triangulationBuilder = new DelaunayTriangulationBuilder
      tolerance.foreach(triangulationBuilder.setTolerance)
      triangulationBuilder.setSites(factory.createMultiPointFromCoords(inputVertices.map (v => new Coordinate(v._1, v._2)).toArray))
      val subdivision = triangulationBuilder.getSubdivision
      //Construct the list B of boundary edges, containing the set { e ∈ E(∆) | e-∂(e) = true}
      val subdivisionQuadEdges = subdivision.getEdges.asScala.map(_.asInstanceOf[QuadEdge]).toSeq
      val quadEdgeTriangles = QuadEdgeTriangle.createOn(subdivision).asScala.map(_.asInstanceOf[QuadEdgeTriangle])
      val quadEdgeVertices = subdivision.getVertices(false).asScala.map(_.asInstanceOf[QuadVertex]).toSeq
      val quadEdgeCoordinates = quadEdgeVertices.map(_.getCoordinate)
      val coordinateIndex = quadEdgeCoordinates.zipWithIndex.map { case (coordinate, index) => coordinate -> index }.toMap
      val vertices = coordinateIndex.map { case (c, i) => i -> new Vertex(i, c) }
      val quadEdgeFrameBorder = subdivisionQuadEdges.filter(subdivision.isFrameBorderEdge)
      val quadEdgeFrame = subdivisionQuadEdges.filter(subdivision.isFrameEdge)
      val quadEdgeBorder = quadEdgeFrameBorder.filterNot(quadEdgeFrame.contains)
      val quadEdgeWithoutFrame = subdivisionQuadEdges.filterNot(quadEdgeFrame.contains)
      //Sort the list B in descending order of edge length
      val quadEdgeDistances = quadEdgeWithoutFrame.map { q => q -> q.toLineSegment.getLength }.toMap
      val sortedQuadEdge = quadEdgeWithoutFrame.sortWith { case (a, b) => quadEdgeDistances(a) > quadEdgeDistances(b) }
      //Initialize the function v - ∂: V(∆) → {true,false}, v-∂: v→false
      val shortLengths = new mutable.HashMap[Int, Edge]
      val lengths = new mutable.TreeMap[Int, Edge]
      val segments = new mutable.HashMap[LineSegment, Int]
      val edges = new mutable.HashMap[Int, Edge]
      val triangles = new mutable.HashMap[Int, Triangle]
      sortedQuadEdge.zipWithIndex.foreach{ case (qe, i) =>
        val s = qe.toLineSegment
        s.normalize()
        val (idS, idD) = (coordinateIndex(s.p0), coordinateIndex(s.p1))
        val (oV, eV) = (vertices(idS), vertices(idD))
        val e = if (quadEdgeBorder.contains(qe)) {
          List(oV, eV).foreach(_.border = true)
          val edge: Edge = new Edge(i, s, true, oV, eV)
          (if (s.getLength < threshold) shortLengths else lengths).put(i, edge)
          edge
        } else new Edge(i, s, false, oV, eV)
        edges.put(i, e)
        segments.put(s, i)
      }
      quadEdgeTriangles.zipWithIndex.foreach { case (quadEdgeTriangle, index) =>
        val (sA, sB, sC) = (quadEdgeTriangle.getEdge(0).toLineSegment, quadEdgeTriangle.getEdge(1).toLineSegment, quadEdgeTriangle.getEdge(2).toLineSegment)
        sA.normalize()
        sB.normalize()
        sC.normalize()
        val triangleEdges = List(edges(segments(sA)), edges(segments(sB)), edges(segments(sC)))
        var triangle = new Triangle(index, quadEdgeTriangle.isBorder)
        triangle.edges++=triangleEdges
        triangleEdges.foreach{_.triangles+=triangle}
        triangles.put(index, triangle)
      }
      edges.values.filter(_.triangles.size > 1).foreach {edge=>
        var (tA,tB) = (edge.triangles(0), edge.triangles(1))
        tA.neighbours += tB
        tB.neighbours += tA
      }
      var edgeIndex = 0
      while (edgeIndex != -1) {
        edgeIndex = -1
        var e:Edge = null
        // get the first edge longer than the threshold
        if (lengths.nonEmpty) {
          val (ind, value) = lengths.head
          if (value.geometry.getLength > threshold) {
            edgeIndex = ind
            e = value
          }
        }
        if (edgeIndex != -1) {
          val triangle = e.triangles(0)
          val neighbours = triangle.neighbours
          // irregular triangle test
          if (neighbours.size == 1) {
            shortLengths.put(e.id, e)
            lengths.remove(e.id)
          } else  {
            val (e0,e1) = (triangle.edges(0), triangle.edges(1))
            // test if all the vertices are on the border
            if (e0.oV.border && e0.eV.border && e1.oV.border && e1.eV.border) {
              shortLengths.put(e.id, e)
              lengths.remove(e.id)
            } else {
              neighbours.foreach(t=>{
                t.border = true
                t.neighbours-=triangle
              })
              triangles.remove(triangle.id)
              // new edges
              val ee = triangle.edges
              def remove(e:Edge): Unit = {
                edges.remove(e.id)
                (ee - e).foreach(n_edge => {
                  List(n_edge,n_edge.oV,n_edge.eV).foreach(_.border = true)
                  n_edge.triangles -= triangle
                  (if (n_edge.geometry.getLength < threshold) shortLengths else lengths).put(n_edge.id, n_edge)
                })
                lengths.remove(e.id)
              }
              remove(ee.find(_.border).get)
            }
          }
        }
      }
      // concave hull creation
      val finalEdges = new ArrayBuffer[LineString]
      for (e <- lengths.values) finalEdges+=e.geometry.toGeometry(factory)
      for (e <- shortLengths.values) finalEdges+=e.geometry.toGeometry(factory)
      // merge
      val lineMerger = new LineMerger
      lineMerger.add(finalEdges.asJava)
      val merge = lineMerger.getMergedLineStrings.iterator.next.asInstanceOf[LineString]
      val lr = new LinearRing(merge.getCoordinateSequence, factory)
      new Polygon(lr, null, factory)
    }
  }
  def fromVertices(inputVertices: Seq[LeavesVertex], threshold: Double, tolerance: Option[Double] = None):Polygon = {
    val nbPoints = 20
    val angle = math.Pi * 2.0 / nbPoints
    apply(inputVertices.flatMap { v => { (0 until nbPoints).map(i=>(v.vx + v.thickness * math.cos(angle * i), v.vy + v.thickness * math.sin(angle * i)))}},threshold, tolerance)
  }
  def fromLines(inputLines: Seq[LeavesLine], threshold: Double, tolerance: Option[Double] = None):Polygon = {
    apply(factory.createGeometryCollection(inputLines.map(l=>Rendering.variableWidthBuffer(
      new Coordinate(l.fromVertex.vx,l.fromVertex.vy),
      new Coordinate(l.toVertex.vx,l.toVertex.vy),
      l.fromVertex.thickness, l.toVertex.thickness, Option(threshold/2)
    )).toArray).union.getCoordinates.map(c=>(c.x,c.y)), threshold, tolerance)
  }
  def main(args: Array[String]): Unit = {
    val threshold = args(0).toDouble
    val r = new Random(42)
    val vertices = (0 until 100).toArray.map{_=>LeavesVertex(r.nextDouble*100, r.nextDouble*100, 0.0)}
    val factory = new GeometryFactory
    vertices.foreach(v=>println(factory.createPoint(new Coordinate(v.vx,v.vy))))
    println(fromVertices(vertices,threshold))
    println(fromVertices(Seq[LeavesVertex](),threshold))
  }
}
