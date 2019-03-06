import java.io.{BufferedWriter, FileOutputStream, FileWriter}

import better.files.File
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.transcoder.{SVGAbstractTranscoder, TranscoderInput, TranscoderOutput}
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.XMLResourceDescriptor
import org.json4s.JsonAST.{JArray, JDouble, JObject, JString}
import org.json4s.native.JsonMethods

object CSVRender extends App {
  val outputDirName = "data_490"
  val outputDir = File(outputDirName)
  outputDir.createDirectories()
  var index = 0
  val bufferedSource = io.Source.fromFile("population490.csv")
  val bufferArray = List.fill(28)(scala.collection.mutable.ArrayBuffer[Double]())
  val svgBuffer = scala.collection.mutable.ArrayBuffer[String]()
  val pngBuffer = scala.collection.mutable.ArrayBuffer[String]()
  val lines = bufferedSource.getLines
  val header = lines.next()
  for (line <- lines) {
    val cols = line.split(",").map(_.trim)
    bufferArray.zipWithIndex.foreach{
      case (b:scala.collection.mutable.ArrayBuffer[Double],i:Int)=> b.append(cols(i+1).toDouble)
    }
    val svgFileName = s"leaf_$index.svg"
    val svgFile = outputDir / svgFileName
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

      Some(svgFile)
    )
    svgBuffer.append(outputDirName + "/" + svgFileName)
    val parser = XMLResourceDescriptor.getXMLParserClassName
    val factory = new SAXSVGDocumentFactory(parser)
    val doc = factory.createSVGDocument(svgFile.uri.toString)
    val strDocWidth = doc.getRootElement.getAttribute("width").toFloat
    val strDocHeight = doc.getRootElement.getAttribute("height").toFloat

    val pngFileName = s"leaf_$index.png"
    val pngFile = outputDir / pngFileName
    val t = new PNGTranscoder()
    val input = new TranscoderInput(svgFile.uri.toString)
    // Create the transcoder output.// Create the transcoder output.
    val ostream = new FileOutputStream(pngFile.toString())
    val output = new TranscoderOutput(ostream)
    t.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, strDocWidth)
    t.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, strDocHeight)
    // Save the image.// Save the image.
    t.transcode(input, output)
    // Flush and close the stream.
    ostream.flush
    pngBuffer.append(outputDirName + "/" + pngFileName)
    println(s"$index - area = ${bufferArray(26).last}, length = ${bufferArray(27).last} - ${model._1} - ${model._2} - ${model._3}")
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
  "perimeter",
  "length"
  )
  val list = bufferArray.zipWithIndex.map{
    case (b:scala.collection.mutable.ArrayBuffer[Double],i:Int)=> names(i) -> JArray(b.toList.map(JDouble(_)))
  }

  val svgData = ("2D_svg" -> JArray(svgBuffer.toList.map(JString(_))))
  val pngData = ("2D_png" -> JArray(pngBuffer.toList.map(JString(_))))
  val finalList = list++List(svgData,pngData)
  val json = JObject(finalList)
  bw.write(JsonMethods.pretty(JsonMethods.render(json)))
  bw.close()
}
