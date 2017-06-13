import com.vividsolutions.jts.geom.{Coordinate, Geometry, GeometryFactory}
import leaves.Vegetal
import leaves.Vegetal.Turtle

import scala.collection.immutable.Stack
import scala.math.{Pi, cos, sin}

object JTS extends App {
  def move_turtle(s: String, t: Turtle, length:Double, delta:Double, fact:GeometryFactory) = {
    var geometries: Seq[Geometry] = Seq()
    var vertices: Seq[Coordinate] = Seq()
    s.foldLeft((t, Stack[Turtle]())) { case ((head, stack), c) =>
      c match {
        case ('F') =>
          val previous_pos = head.position
          val newt = head.move(length * cos(head.heading), length * sin(head.heading))
          val new_pos = newt.position
          geometries = geometries :+ fact.createLineString(Array(new Coordinate(previous_pos._1,previous_pos._2),new Coordinate(new_pos._1,new_pos._2)))
          (newt, stack)
        case ('f') =>
          (head.move(length * cos(head.heading), length * sin(head.heading)), stack)
        case ('+') =>
          (head.rotate(delta), stack)
        case ('-') =>
          (head.rotate(-delta), stack)
        case ('[') =>
          (head, stack.push(head))
        case (']') =>
          stack.pop2
        case ('{') =>
          vertices = Seq()
          (head, stack)
        case ('}') =>
          vertices = vertices :+ vertices(0)
          geometries = geometries :+ fact.createPolygon(vertices.toArray)
          (head, stack)
        case ('.') =>
          vertices = vertices :+ new Coordinate(head.position._1, head.position._2)
          (head, stack)
        case _ =>
          (head, stack)
      }
    }
    geometries
  }
  override def main(args: Array[String]): Unit = {
    val delta = math.toRadians(18)
    val length = 10.0
    val nbIterations = 10

    val leo = new Turtle(500, 500, 3 * 0.5 * Pi)
    val ss = Vegetal.generate(delta, length, nbIterations)
    val fact = new GeometryFactory()
    val geometries = move_turtle(ss, leo, length, delta,fact)
    geometries.foreach(g=>
      println(g)
    )
    val collection = fact.createGeometryCollection(geometries.toArray)
    println(collection.getArea)
    println(collection.getLength)
  }
}
