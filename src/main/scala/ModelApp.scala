import leaves.Model

/**
  * Created by julien on 16/06/17.
  */
object ModelApp extends App {
  override def main(args: Array[String]) = Model(
    args(0).toDouble,
    args(1).toDouble,
    args(2).toDouble,
    math.toRadians(args(3).toDouble),
    args(4).toInt,
    args(5).toDouble,
    args(6).toInt,
    args(7).toBoolean
  )
}
