import better.files.File
import leaves.Model

/**
  * Created by julien on 16/06/17.
  */
object ModelApp extends App {
  override def main(args: Array[String]) = Model(
    args(0).toDouble,
    args(1).toDouble,
    math.toRadians(args(2).toDouble),
    args(3).toInt,
    args(4).toDouble,
    args(5).toInt,
    if (args.length == 7) Some(File(args(6))) else None
  )
}
