
import sbt._
import Keys._
//import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

name := "Leaves"

version := "1.0"

scalaVersion in ThisBuild := "2.12.1"

//lazy val buildLeaves = taskKey[Unit]("buildLeaves")

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