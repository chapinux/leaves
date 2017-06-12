import scala.annotation.tailrec
import scala.math._

  object HelloWorld {
    def main(args: Array[String]): Unit = {
      println("Hello, world en scala ma gueule!")

      sealed trait Sign
      case object Negative extends Sign
      case object Positive extends Sign
       case object Zero extends Sign

      def f_signe(point: Double, precision: Double): Sign = {
        if (cos(point) > 0) Positive
        else if (cos(point) < 0) Negative
        else if (abs(cos(point)) < precision) Zero
        else throw new IllegalArgumentException
      }


   @tailrec def dichotomie(pp: Double, pn: Double, precision: Double): Double = {
      println("Appel de dichotomie avec pp-pn = " + (pp -pn) )
        if (abs(pp - pn) < precision) ((pp + pn) / 2)
        else {
          val m: Double = (pp + pn) / 2
          f_signe(m, precision) match {
            case Positive => dichotomie(m,pn,precision)
            case Negative => dichotomie(pp, m , precision)
            case Zero => m
          }
        }
      }//dichotomie

      val precision :Double = 0.01
      val res: Double = dichotomie( 0, Pi/2 + random()*(Pi/2) , precision)
      println("resultat  "+ res.toDegrees )
    }//main
}//object



