
import sbt._
import Keys._
//import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

name := "Leaves"

version := "1.0"

scalaVersion in ThisBuild := "2.12.1"

val jtsVersion = "1.13"
val scalatagsVersion = "0.6.5"
val betterFileVersion = "2.15.0"

resolvers ++= Seq(
  "osgeo" at "http://download.osgeo.org/webdav/geotools/",
  "geosolutions" at "http://maven.geo-solutions.it/",
  "geotoolkit" at "http://maven.geotoolkit.org/"
)

libraryDependencies ++= Seq (
  "com.vividsolutions" % "jts" % jtsVersion,
  "com.lihaoyi" %% "scalatags" % scalatagsVersion,
  "com.github.pathikrit" % "better-files_2.11" % betterFileVersion
)
