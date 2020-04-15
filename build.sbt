name := "untitled1"

version := "0.1"

scalaVersion := "2.13.1"

val springBootVersion = "1.3.2.RELEASE"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-web" % "1.5.4.RELEASE"
)
libraryDependencies ++= Seq(
  "org.apache.tomcat.embed" % "tomcat-embed-core"         % "7.0.53" % "container",
  "org.apache.tomcat.embed" % "tomcat-embed-logging-juli" % "7.0.53" % "container",
  "org.apache.tomcat.embed" % "tomcat-embed-jasper"       % "7.0.53" % "container"
)