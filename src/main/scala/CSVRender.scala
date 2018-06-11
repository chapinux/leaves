import java.io.{BufferedWriter, FileWriter}

import better.files.File
import org.json4s.JsonAST.{JArray, JDouble, JObject, JString}
import org.json4s.native.JsonMethods

object CSVRender extends App {
  val outputDir = File("data")
  outputDir.createDirectories()
  var index = 0
  val bufferedSource = io.Source.fromFile("population.csv")
  val bufferArray = List.fill(28)(scala.collection.mutable.ArrayBuffer[Double]())
  val fileBuffer = scala.collection.mutable.ArrayBuffer[String]()
  val lines = bufferedSource.getLines
  val header = lines.next()
  for (line <- lines) {
    val cols = line.split(",").map(_.trim)
    bufferArray.zipWithIndex.foreach{
      case (b:scala.collection.mutable.ArrayBuffer[Double],i:Int)=> b.append(cols(i+1).toDouble)
    }
    val file = outputDir / s"leaf_$index.svg"
    val model = leaves.Model(
      cols(1).toDouble,

      cols(2).toDouble,
      cols(3).toDouble,
      cols(4).toDouble,
      cols(5).toDouble.round.toInt,
      cols(6).toDouble,

      cols(7).toDouble,
      cols(8).toDouble,
      cols(9).toDouble,
      cols(10).toDouble.round.toInt,
      cols(11).toDouble,

      cols(12).toDouble,
      cols(13).toDouble,
      cols(14).toDouble,
      cols(15).toDouble.round.toInt,
      cols(16).toDouble,

      cols(17).toDouble,
      cols(18).toDouble,
      cols(19).toDouble,
      cols(20).toDouble.round.toInt,
      cols(21).toDouble,

      cols(22).toDouble,
      cols(23).toDouble,
      cols(24).toDouble,
      cols(25).toDouble.round.toInt,
      cols(26).toDouble,

      Some(file)
    )
    fileBuffer.append(s"data/leaf_$index.svg")
    println(s"$index - area = ${bufferArray(26).last}, perimeter = ${bufferArray(27).last} - ${model._1} - ${model._2}")
    index += 1
  }
  bufferedSource.close
  // FileWriter
  val file = File("data.json")
  val bw = new BufferedWriter(new FileWriter(file.toJava))
  val names = List(
  "alphaShape",
  "thickness0",
  "decreaseRate0",
  "angle0",
  "nbBifurcation0",
  "sterilityRate0",
  "thickness1",
  "decreaseRate1",
  "angle1",
  "nbBifurcation1",
  "sterilityRate1",
  "thickness2",
  "decreaseRate2",
  "angle2",
  "nbBifurcation2",
  "sterilityRate2",
  "thickness3",
  "decreaseRate3",
  "angle3",
  "nbBifurcation3",
  "sterilityRate3",
  "thickness4",
  "decreaseRate4",
  "angle4",
  "nbBifurcation4",
  "sterilityRate4",
  "area",
  "perimeter"
  )
  val list = bufferArray.zipWithIndex.map{
    case (b:scala.collection.mutable.ArrayBuffer[Double],i:Int)=> names(i) -> JArray(b.toList.map(JDouble(_)))
  }

  val fileName = ("2D" -> JArray(fileBuffer.toList.map(JString(_))))
  val finalList = list++List(fileName)
  val json = JObject(finalList)
  bw.write(JsonMethods.pretty(JsonMethods.render(json)))
  bw.close()
}
