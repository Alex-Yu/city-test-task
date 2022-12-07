name := "city-test-task"

version := "0.1"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.14" % Test,
  "com.storm-enroute" %% "scalameter" % "0.21" % Test)

testFrameworks += new TestFramework(
  "org.scalameter.ScalaMeterFramework")

Test / parallelExecution := false