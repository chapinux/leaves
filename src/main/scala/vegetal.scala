/**
  * Created by chap on 11/04/17.
  */

import processing.core._
import PApplet._

import math.cos
import math.sin
import math.Pi
import scala.annotation.tailrec


class vegetal extends PApplet {


  case class Turtle ( var x : Double , var y : Double , var heading : Double) {

    def move(dx:Int,dy:Int):Unit={
      x= x + dx
      y = y + dy
    }

    def position : (Int,Int) = (x.toInt,y.toInt)

    def rotate(angle: Double)={
      heading = heading + angle
    }

    override def toString: String = "Turtle x:" + this.x + "y:" + y + "alpha:" + heading
 }





  val axiom: String = "F-F-F-F"
  // apllication de la règle de grammaire
 def rewrite(s: String): String = s.flatMap{ _ match {
   case 'F' => "F-FF--F-F"
   case '-' => "-"
   case '+' => "+"
    }
  }






  def move_turtle(s: String, t: Turtle) = s.map {
    _ match {
      case ('F') =>
        val previous_pos = t.position
        t.move((d * cos(t.heading)).toInt, (d * sin(t.heading)).toInt)
        line(previous_pos._1, previous_pos._2, t.position._1, t.position._2)
      case ('f') =>
        println("j'ai un f, je bouge sans tracer")
        t.move((d * cos(t.heading)).toInt, (d * sin(t.heading)).toInt)
      case ('+') =>
        t.rotate(delta)
      case ('-') =>
        t.rotate(-delta)
    }
  }



  // appel recursif n fois
  def apply_n(s: String, i: Int): (String, Int) = (s, i) match {
    case (_, 0) => (s, i)
    case (s, _) => apply_n(rewrite(s), i - 1)
  }

 // lazy val res: String = apply_n(axiom, 1)._1
  val delta = math.toRadians(90)
  val d = 10
  val n = 5

  var mot = axiom
  var motif = axiom
  val leo : Turtle = new Turtle(300,300, 3* 0.5*Pi )
 // 1 to n foreach { _ => mot = rule(mot) }
  println(mot)
  println("============")
 // println(res)





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

}// class

  object vegetal {
    def main(args:Array[String]) {
      PApplet.main(Array("vegetal"))
    }

  }
