name := "sumologic-scala-client"

version := "1.0"

scalaVersion := "2.10.2"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.6"

libraryDependencies += "com.sumologic.api.client" % "sumo-java-client" % "1.0"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.0"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.4"

libraryDependencies += "org.json4s" %% "json4s-core" % "3.2.5"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.5"

libraryDependencies += "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.0"

libraryDependencies += "net.databinder" %% "dispatch-oauth" % "0.8.10"

libraryDependencies += "commons-lang" % "commons-lang" % "2.6"

libraryDependencies += "org.parboiled" %% "parboiled-scala" % "1.1.4"

libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.0-M4"

libraryDependencies += "org.clapper" % "grizzled-slf4j_2.10" % "1.0.1"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.6"
