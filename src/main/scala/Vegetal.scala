import processing.core._
import PApplet._
import math.cos
import math.sin
import math.Pi
import collection.immutable.Stack


object Vegetal {

  def generate(angle:Double, longueur:Int, nombre:Int): String ={
    val V = new Vegetal(angle, longueur,nombre)
    V.apply_n("F", nombre)._1
  }

}


class Vegetal(delta:Double, d:Int, n:Int)  {

  //val delta = math.toRadians(11)
  //val d = 10
  //val n = 4

  val leo : Turtle = new Turtle(500,500, 3* 0.5*Pi )




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




  // appel recursif n fois
  def apply_n(s: String, i: Int): (String, Int) = (s, i) match {
    case (_, 0) => (s, i)
    case (s, _) => apply_n(rewrite(s), i - 1)
  }
}// class

