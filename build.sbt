name := """Immutaskable"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.scalaz" %% "scalaz-core" % "7.2.2",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  "org.reactivemongo" %% "reactivemongo-akkastream" % "0.12.0",
  "org.mindrot" % "jbcrypt" % "0.3m"
)


val browserifyTask = taskKey[Seq[File]]("Run browserify")

val browserifyOutputDir = settingKey[File]("Browserify output directory")

browserifyOutputDir := target.value / "web" / "browserify"

browserifyTask := {
  println("Running browserify");
  val outputFile = browserifyOutputDir.value / "main.js"
  browserifyOutputDir.value.mkdirs
  "./node_modules/.bin/browserify -t [ babelify --presets [ es2015 react ] ] app/assets/javascripts/main.jsx -o "+outputFile.getPath !;
  List(outputFile)
}

sourceGenerators in Assets <+= browserifyTask

resourceDirectories in Assets += browserifyOutputDir.value


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

