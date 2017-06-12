package vegetal_processing

import processing.core.PApplet._
import processing.core._

import scala.collection.immutable.Stack
import scala.math.{Pi, cos, sin}

class vegetal extends PApplet {


  val axiom: String = "F"
  def rewrite(s: String): String = s.flatMap{ _ match {
    case 'F' => "FF-[-F+F+F]+[+F-F-F]"
    case c => c.toString
  }
  }



  case class Turtle (val x : Double , val y : Double , val heading : Double) {
    def move(dx:Int,dy:Int):Turtle= Turtle(x + dx, y = y + dy,heading)
    def position : (Int,Int) = (x.toInt,y.toInt)
    def rotate(angle: Double)= Turtle(x,y,(heading + angle)%(2*Pi))
    override def toString: String = "Turtle x:" + this.x + "y:" + y + "alpha:" + heading
 }


  def move_turtle(s: String, t: Turtle) = {
    val st = Stack[Turtle]()
    s.foldLeft((t,st)) { case ((head, stack), c) =>
      c match {
        case ('F') =>
          val previous_pos = head.position
          val newt = head.move((d * cos(head.heading)).toInt, (d * sin(head.heading)).toInt)
          line(previous_pos._1, previous_pos._2, newt.position._1, newt.position._2)
          (newt,stack)
        case ('f') =>
          (head.move((d * cos(head.heading)).toInt, (d * sin(head.heading)).toInt),stack)
        case ('+') =>
          (head.rotate(delta),stack)
        case ('-') =>
          (head.rotate(-delta),stack)
        case ('[') =>
          (head, stack.push(head))
        case (']') =>
          stack.pop2
      }
    }
  }


//F-FF--F-F-F-FF--F-F-F-FF--F-F-F-FF--F-F
//F-FF--F-F-F-FF--F-F-F-FF--F-F-F-FF--F-F

  // appel recursif n fois
  def apply_n(s: String, i: Int): (String, Int) = (s, i) match {
    case (_, 0) => (s, i)
    case (s, _) => apply_n(rewrite(s), i - 1)
  }

 // lazy val res: String = apply_n(axiom, 1)._1
  val delta = math.toRadians(11)
  val d = 10
  val n = 4

  var motif = axiom
  val leo : Turtle = new Turtle(500,500, 3* 0.5*Pi )
 // 1 to n foreach { _ => mot = rule(mot) }





  override def setup
  {

    frameRate(999)
    stroke(255)
    background(0)
    move_turtle(axiom,leo)

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

   val res = rewrite(motif)
  motif = res
    println("motif de longueur " + motif.length + "\nmotif" + motif)
    move_turtle(res , leo)

  }

}

// class

object vegetal {

  def main(args:Array[String]) {


    PApplet.main(Array("vegetal"))
  }

}
