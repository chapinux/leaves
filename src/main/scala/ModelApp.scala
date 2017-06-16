import leaves.Model

/**
  * Created by julien on 16/06/17.
  */
object ModelApp extends App {
  override def main(args: Array[String]) = Model(
    args(0).toDouble,
    args(1).toDouble, args(2).toDouble, math.toRadians(args(3).toDouble), args(4).toInt, args(5).toDouble,
    args(6).toDouble, args(7).toDouble, math.toRadians(args(8).toDouble), args(9).toInt, args(10).toDouble,
    args(11).toDouble, args(12).toDouble, math.toRadians(args(13).toDouble), args(14).toInt, args(15).toDouble,
    args(16).toDouble, args(17).toDouble, math.toRadians(args(18).toDouble), args(19).toInt, args(20).toDouble,
    args(21).toDouble, args(22).toDouble, math.toRadians(args(23).toDouble), args(24).toInt, args(25).toDouble
  )
}
