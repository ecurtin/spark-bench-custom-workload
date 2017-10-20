import Dependencies._

lazy val sparkBenchPath = "/ABSOLUTE/PATH/TO/YOUR/SPARK/BENCH/INSTALLATION/lib/"
lazy val sparkVersion = "2.1.1"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "WordGenerator",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core"  % sparkVersion % "provided",
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql"   % sparkVersion % "provided"
    ),
    unmanagedBase := new java.io.File(sparkBenchPath)
)
