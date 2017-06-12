import Vegetal.Turtle
import processing.core.PApplet._
import processing.core._

import scala.collection.immutable.Stack
import scala.math.{Pi, cos, sin}



object vegetal_withProcessing {

  val d = 20
  val delta =math.toRadians(5.0)
  val veg = Vegetal.generate(delta,d,25)
  println(veg)
  val t = new Turtle(500, 500, 3 * 0.5 * Pi)


  def main(args:Array[String]) {

    PApplet.main(Array("vegetal_withProcessing"))
  }
}


class vegetal_withProcessing extends PApplet {
  def move_turtle(s: String, t: Turtle) = {
    val st = Stack[Turtle]()
    s.foldLeft((t,st)) { case ((head, stack), c) =>
      c match {
        case ('F') =>
          val previous_pos = head.position
          val newt = head.move((vegetal_withProcessing.d * cos(head.heading)).toInt, (vegetal_withProcessing.d * sin(head.heading)).toInt)
          line(previous_pos._1, previous_pos._2, newt.position._1, newt.position._2)
          (newt,stack)
        case ('f') =>
          (head.move((vegetal_withProcessing.d * cos(head.heading)).toInt, (vegetal_withProcessing.d * sin(head.heading)).toInt),stack)
        case ('+') =>
          (head.rotate(vegetal_withProcessing.delta),stack)
        case ('-') =>
          (head.rotate(-vegetal_withProcessing.delta),stack)
        case ('[') =>
          (head, stack.push(head))
        case (']') =>
          stack.pop2
        case ('{') =>
          beginShape()
          (head,stack)
        case ('}') =>
           endShape()
          (head,stack)
        case ('.')=>
          vertex(head.x.toFloat,head.y.toFloat)
          (head,stack)
        case _ =>(head,stack)


      }
    }
  }





  override def setup
  {

    frameRate(999)
    stroke(255)
    background(0)
    fill(127)
    move_turtle(vegetal_withProcessing.veg,vegetal_withProcessing.t)

  }

  override def draw: Unit =
  {
    //println("methode draw")
  }

  override def settings(): Unit= {
    size(800, 800)

  }

  override def mousePressed: Unit=
  {
    //background(0)
    println("mouse pressed")

 move_turtle(vegetal_withProcessing.veg , vegetal_withProcessing.t)

  }

}
// class