import com.vividsolutions.jts.geom.GeometryFactory
import leaves.Vegetal
import leaves.Vegetal.Turtle

object Leaves {
  def generate(angle:Double, length:Double):(Double,Double) = {
    val leo = new Turtle(500, 500, 3 * 0.5 * math.Pi)
    val ss = Vegetal.generate(angle, length, 20)
    val fact = new GeometryFactory()
    val geometries = JTS.move_turtle(ss, leo, length, angle,fact)
    val collection = fact.createGeometryCollection(geometries.toArray)
    (collection.getArea, collection.getLength)
  }
  def main(args: Array[String]): Unit = {
    println(generate(10.0, 15.0))
  }
}
