
import sbt._
import Keys._

name := "Leaves"

version := "1.0"

scalaVersion in ThisBuild := "2.12.6"

val jtsVersion = "1.16.1"
val scalatagsVersion = "0.6.5"
val betterFileVersion = "3.4.0"
val json4sVersion = "3.5.4"
val batikVersion = "1.10"

resolvers in ThisBuild ++= Seq(
  "osgeo" at "http://download.osgeo.org/webdav/geotools/",
  "geosolutions" at "http://maven.geo-solutions.it/",
  "geotoolkit" at "http://maven.geotoolkit.org/"
)


enablePlugins(SbtOsgi)

osgiSettings

OsgiKeys.exportPackage := Seq("leaves.*")

OsgiKeys.importPackage := Seq("*;resolution:=optional")

OsgiKeys.privatePackage := Seq("!java.*","!scala.*","*")

val leaves = project in (file(".")) settings(
  mainClass in assembly := Some("leaves.Model"),
  OsgiKeys.requireCapability := """osgi.ee; osgi.ee="JavaSE";version:List="1.8,1.9""""",
  assemblyJarName in assembly := "leaves.jar",
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
  libraryDependencies ++= Seq(
    //"com.vividsolutions" % "jts" % jtsVersion,
    "org.locationtech.jts" % "jts-core" % jtsVersion,
    "com.lihaoyi" %% "scalatags" % scalatagsVersion,
    "com.github.pathikrit" %% "better-files" % betterFileVersion,
    "org.json4s" %% "json4s-native" % json4sVersion,
    "org.apache.xmlgraphics" % "batik-transcoder" % batikVersion,
    "org.apache.xmlgraphics" % "batik-codec" % batikVersion,
    "org.apache.xmlgraphics" % "xmlgraphics-commons" % "2.3"
  )
)
