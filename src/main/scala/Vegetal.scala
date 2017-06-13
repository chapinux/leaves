package leaves

import math.cos
import math.sin
import math.Pi
import collection.immutable.Stack


object Vegetal {

 // val leo = new Turtle(500, 500, 3 * 0.5 * Pi)

  def generate(angle: Double, longueur: Int, nombre: Int): String = {
    val v = new Vegetal(angle, longueur, nombre)
    v.apply_n("[A][B]", nombre)._1
  }

  case class Turtle(val x: Double, val y: Double, val heading: Double) {
    def move(dx: Int, dy: Int): Turtle = Turtle(x + dx, y = y + dy, heading)
    def position: (Int, Int) = (x.toInt, y.toInt)
    def rotate(angle: Double) = Turtle(x, y, (heading + angle) % (2 * Pi))
    override def toString: String = "Turtle x:" + this.x + "y:" + y + "alpha:" + heading
  }

}


class Vegetal(delta: Double, d: Int, n: Int) {

  val axiom: String = "[A][B]"

  def rewrite(s: String): String = s.flatMap {
    _ match {
      case 'A' => "[+A{.].C.}"
      case 'B' => "[-B{.].C.}"
      case 'C' => "FC"
      case c => c.toString
    }
  }

  // appel recursif n fois
  def apply_n(s: String, i: Int): (String, Int) = {
    println("apply")
    (s, i) match {
      case (_, 0) => (s, i)
      case (s, _) => apply_n(rewrite(s), i - 1)
    }
  }
}

// class

