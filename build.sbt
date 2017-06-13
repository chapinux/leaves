
import sbt._
import Keys._
//import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

name := "Leaves"

version := "1.0"

scalaVersion in ThisBuild := "2.12.1"

val jtsVersion = "1.13"

resolvers ++= Seq(
  "osgeo" at "http://download.osgeo.org/webdav/geotools/",
  "geosolutions" at "http://maven.geo-solutions.it/",
  "geotoolkit" at "http://maven.geotoolkit.org/"
)

//lazy val buildLeaves = taskKey[Unit]("buildLeaves")
libraryDependencies ++= Seq (
  "com.vividsolutions" % "jts" % jtsVersion
)

lazy val leaves = project.in(file(".")) /*enablePlugins (ScalaJSPlugin)*/ settings(
  //libraryDependencies += "com.lihaoyi" %% "scalatags" % "0.6.5"
 // libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1",
//  buildLeaves := {
//    val demoTarget = target.value
//    val demoResource = (resourceDirectory in Compile).value
//    val demoJS = (fullOptJS in Compile).value
//
//    IO.copyFile(demoResource / "index.html", demoTarget / "index.html")
//    IO.copyFile(demoJS.data, demoTarget / "js/leaves.js")
//  }
)