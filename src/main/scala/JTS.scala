import com.vividsolutions.jts.geom.{Coordinate, Geometry, GeometryFactory}
import leaves.Vegetal
import leaves.Vegetal.Turtle

import scala.collection.immutable.Stack
import scala.math.{Pi, cos, sin}

object JTS extends App {

  override def main(args: Array[String]): Unit = {

    val delta = math.toRadians(20)
    val length = 10
    val nbIterations = 10
    val fact = new GeometryFactory

    //case class MoveAndTrace(moveX: Int, moveY: Int, traceToX: Int, traceToY: Int)
    var geometries: Seq[Geometry] = Seq()
    var vertices: Seq[Coordinate] = Seq()

    def move_turtle(s: String, t: Turtle) = {
      val st = Stack[Turtle]()
      s.foldLeft((t, st)) { case ((head, stack), c) =>
        c match {
          case ('F') =>
            val previous_pos = head.position
            val newt = head.move((length * cos(head.heading)).toInt, (length * sin(head.heading)).toInt)
            //moveAndTraces = moveAndTraces :+ MoveAndTrace(previous_pos._1, previous_pos._2, newt.position._1, newt.position._2)
            geometries = geometries :+ fact.createLineString(Array[Coordinate](
              new Coordinate(previous_pos._1.toDouble, previous_pos._2.toDouble),
              new Coordinate(newt.position._1.toDouble, newt.position._2.toDouble)))
            (newt, stack)
          case ('f') =>
            (head.move((length * cos(head.heading)).toInt, (length * sin(head.heading)).toInt), stack)
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
            vertices = vertices :+ new Coordinate(head.position._1.toDouble, head.position._2.toDouble)
            (head, stack)
          case _ =>
            (head, stack)
        }
      }
    }

    val leo = new Turtle(500, 500, 3 * 0.5 * Pi)
    val ss = Vegetal.generate(delta, length, nbIterations)
    move_turtle(ss, leo)

    //val painter = svg.path.start(0, 0)
    //val path = moveAndTraces.foldLeft(painter)((painter, mAt)=> painter.m(mAt.moveX, mAt.moveY).l(mAt.traceToX, mAt.traceToY))
    geometries.foreach(g=>
      println(g)
    )
    val collection = fact.createGeometryCollection(geometries.toArray)
    println(collection.getArea)
    println(collection.getLength)
      //println(_))
//    println(geometries)
  }
}
