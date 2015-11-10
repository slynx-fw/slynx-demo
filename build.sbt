name := """slynx-demo"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "net.liftweb" %% "lift-webkit" % "2.6-M4",
  "net.liftweb" %% "lift-mapper" % "2.6-M4",
  "javax.servlet" % "javax.servlet-api" % "3.0.1",
  "javax.servlet" % "servlet-api" % "2.5" % "provided",
  "ch.qos.logback" % "logback-classic" % "1.0.6",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
  "org.squeryl" %% "squeryl" % "0.9.5-7",
  // "com.zaxxer" % "HikariCP" % "1.3.8" % "compile",
  "com.googlecode.usc" % "jdbcdslog" % "1.0.6.2",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.0-1",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
  "com.maxmind.geoip" % "geoip-api" % "1.2.13",
  "com.jcraft" % "jsch" % "0.1.51",
  "javax.mail" % "mail" % "1.4.4",
  "joda-time" % "joda-time" % "2.3",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41",
  "org.squeryl" %% "squeryl" % "0.9.5-7",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
  "net.coobird" % "thumbnailator" % "0.4.8",
  "com.moviejukebox" % "api-imdb" % "1.3"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

lazy val zipAppCode = TaskKey[Unit]("Zip App Code", "Zips Application Code")

zipAppCode := {
  import sys.process._
  Seq("zip", "-rq", "public/app.zip", "app") !
}

compile in Compile <<= (compile in Compile).dependsOn(zipAppCode)