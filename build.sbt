name := "cbidb-staff-web"

version := "0.1"

scalaVersion := "2.12.4"

enablePlugins(ScalaJSPlugin)
enablePlugins(ScalaJSBundlerPlugin)

name := "CBI DB Staff Web"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2"
libraryDependencies += "fr.hmil" %%% "roshttp" % "2.1.0"

npmDependencies in Compile += "snabbdom" -> "0.7.0"
