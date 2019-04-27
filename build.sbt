name := "elastic-search-examples"

version := "0.1"

scalaVersion := "2.12.6"


libraryDependencies ++= Seq(

  //elastic search
  "org.elasticsearch" % "elasticsearch" % "6.2.4",

  //rest high level client
  "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "6.1.2",

  //play json
  "com.typesafe.play" %% "play" % "2.6.11",
  
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  
)