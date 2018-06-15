import better.files.File
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
    if (args.length == 8) Some(File(args(7))) else None
  )
}
