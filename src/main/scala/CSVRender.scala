import java.io.{BufferedWriter, FileOutputStream, FileWriter}

import better.files.File
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.transcoder.{SVGAbstractTranscoder, TranscoderInput, TranscoderOutput}
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.XMLResourceDescriptor
import org.json4s.JsonAST.{JArray, JDouble, JObject, JString}
import org.json4s.native.JsonMethods

object CSVRender extends App {
  val generation = 2762
  val outputDirName = s"data_$generation"
  val outputDir = File(outputDirName)
  outputDir.createDirectories()
  var index = 0
  val bufferedSource = io.Source.fromFile(s"population$generation.csv")
  val bufferArray = List.fill(9)(scala.collection.mutable.ArrayBuffer[Double]())
  val svgBuffer = scala.collection.mutable.ArrayBuffer[String]()
  val pngBuffer = scala.collection.mutable.ArrayBuffer[String]()
  val lines = bufferedSource.getLines
  val header = lines.next()
  val start = System.currentTimeMillis()
  for ((line, index) <- lines.zipWithIndex) {
    if (index % 100 == 0) {
      val end = System.currentTimeMillis()
      println(s"$index ${end - start} ms")
    }
    println(s"Line $index")
    val cols = line.split(",").map(_.trim)
    bufferArray.zipWithIndex.foreach {
      case (b: scala.collection.mutable.ArrayBuffer[Double], i: Int) => b.append(cols(i + 1).toDouble)
    }
    val svgFileName = s"leaf_$index.svg"
    val svgFile = outputDir / svgFileName
    val model = leaves.Model(
      cols(1).toDouble,
      cols(2).toDouble,
      cols(3).toDouble,
      cols(4).toDouble.toInt,
      cols(5).toDouble,
      cols(6).toDouble.toInt,

      Some(svgFile)
    )
    svgBuffer.append(outputDirName + "/" + svgFileName)
    val parser = XMLResourceDescriptor.getXMLParserClassName
    val factory = new SAXSVGDocumentFactory(parser)
    val doc = factory.createSVGDocument(svgFile.uri.toString)
    val strDocWidth = doc.getRootElement.getAttribute("width").toFloat
    val strDocHeight = doc.getRootElement.getAttribute("height").toFloat
    val ratio = strDocHeight / strDocWidth

    val newWidth = 450f
    val newHeigth = math.max(newWidth * ratio, 1f)

    if (newHeigth <= 1f) println(s"strDocWidth=$strDocWidth strDocHeight=$strDocHeight ratio=$ratio newHeight=$newHeigth")
    val pngFileName = s"leaf_$index.png"
    val pngFile = outputDir / pngFileName
    val t = new PNGTranscoder()
    val input = new TranscoderInput(svgFile.uri.toString)
    // Create the transcoder output.// Create the transcoder output.
    val ostream = new FileOutputStream(pngFile.toString())
    val output = new TranscoderOutput(ostream)
    t.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, newWidth)
    t.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, newHeigth)
    // Save the image.// Save the image.
    t.transcode(input, output)
    // Flush and close the stream.
    ostream.flush
    pngBuffer.append(outputDirName + "/" + pngFileName)
//    println(s"$index - area = ${bufferArray(7).last}, length = ${bufferArray(8).last} - ${model._1} - ${model._2} - ${model._3}")
  }
  bufferedSource.close
  // FileWriter
  val file = File("data.json")
  val bw = new BufferedWriter(new FileWriter(file.toJava))
  val names = List(
    "thickness",
    "decreaseRate",
    "angle",
    "nbBifurcation",
    "angleRate",
    "depth",
    "area",
    "compacity",
    "convexity"
  )
  val list = bufferArray.zipWithIndex.map {
    case (b: scala.collection.mutable.ArrayBuffer[Double], i: Int) => names(i) -> JArray(b.toList.map(JDouble(_)))
  }
//  val svgData = ("2D_svg" -> JArray(svgBuffer.toList.map(JString(_))))
  val pngData = ("2D_png" -> JArray(pngBuffer.toList.map(JString(_))))
  val finalList = list++List(/*svgData,*/pngData)
  val json = JObject(finalList)
  bw.write(JsonMethods.pretty(JsonMethods.render(json)))
  bw.close()
  val end = System.currentTimeMillis()
  println((end - start) + " ms")
}
