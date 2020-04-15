name := "untitled1"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

libraryDependencies ++= Seq(
  "org.springframework.boot" % "spring-boot-starter-web" % "1.5.4.RELEASE",
  "org.eclipse.jetty" % "jetty-webapp" % "9.1.0.v20131115" % "container, compile",
  "org.eclipse.jetty" % "jetty-jsp" % "9.1.0.v20131115" % "container",
  "org.springframework" % "spring-webmvc" % "4.0.6.RELEASE",
  "org.springframework" % "spring-context" % "4.0.6.RELEASE",
  "org.springframework" % "spring-context-support" % "4.0.6.RELEASE",
  "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided",
  "javax.servlet" % "jstl" % "1.2" % "compile"
)
