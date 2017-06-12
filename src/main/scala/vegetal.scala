import processing.core._
import PApplet._
import math.cos
import math.sin
import math.Pi

class vegetal extends PApplet {


  val axiom: String = "F-F-F-F"
  def rewrite(s: String): String = s.flatMap{ _ match {
    case 'F' => "F-FF--F-F"
    case '-' => "-"
    case '+' => "+"
  }
  }



  case class Turtle (val x : Double , val y : Double , val heading : Double) {
    def move(dx:Int,dy:Int):Turtle= Turtle(x + dx, y = y + dy,heading)
    def position : (Int,Int) = (x.toInt,y.toInt)
    def rotate(angle: Double)= Turtle(x,y,(heading + angle)%(2*Pi))
    override def toString: String = "Turtle x:" + this.x + "y:" + y + "alpha:" + heading
 }


  def move_turtle(s: String, t: Turtle) = s.foldLeft(t) { case (tt, c) =>
    c match {
      case ('F') =>
        val previous_pos = tt.position
        val newt = tt.move((d * cos(tt.heading)).toInt, (d * sin(tt.heading)).toInt)
        line(previous_pos._1, previous_pos._2, newt.position._1, newt.position._2)
        newt
      case ('f') =>
        tt.move((d * cos(tt.heading)).toInt, (d * sin(tt.heading)).toInt)
      case ('+') =>
        tt.rotate(delta)
      case ('-') =>
        tt.rotate(-delta)
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
  val delta = math.toRadians(90)
  val d = 10
  val n = 5

  val leo : Turtle = new Turtle(300,300, 3* 0.5*Pi )
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

   val res = rewrite(axiom)
    println("motif de longueur " + axiom.length + "\nmotif" + axiom)
    move_turtle(res , leo)

  }

}// class

  object vegetal {
    def main(args:Array[String]) {
      PApplet.main(Array("vegetal"))
    }

  }
