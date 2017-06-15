package leaves

import math.{Pi, cos, sin}

object Vegetal {
  def generate(angle: Double, longueur: Double, nombre: Int) =
    new Vegetal(angle, longueur, nombre).apply_n("[A][B]", nombre)._1
  case class Turtle(val x: Double, val y: Double, val heading: Double, thickness: Double = 1.0) {
    def move(dx: Double, dy: Double): Turtle = Turtle(x + dx * cos(heading), y + dy * sin(heading), heading, thickness)
    def position: (Double, Double) = (x, y)
    def rotate(angle: Double) = Turtle(x, y, (heading + angle) % (2 * Pi))
    override def toString: String = s"Turtle x:$x y:$y  alpha:$heading"
  }
}

class Vegetal(delta: Double, d: Double, n: Int) {
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
  def apply_n(s: String, i: Int): (String, Int) =
    (s, i) match {
      case (_, 0) => (s, i)
      case (s, _) => apply_n(rewrite(s), i - 1)
    }
}
