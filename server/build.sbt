//top level config
organization := "co.melbournetransport"
name := "melbournetranserver" 
version := "0.1.2"
scalaVersion := "2.9.2"

// set the main class for packaging the main jar
// 'run' will still auto-detect and prompt
// change Compile to Test to set it for the test jar
mainClass in (Compile, packageBin) := Some("co.melbournetransport.JettyLauncher")

mainClass in (Compile, run) := Some("co.melbournetransport.JettyLauncher")

seq(webSettings :_*)

libraryDependencies ++= Seq(
  "org.scalatra" % "scalatra_2.9.1" % "2.0.4",
  "org.scalatra" % "scalatra-scalate_2.9.1" % "2.0.4",
  "org.scalatra" % "scalatra-specs2_2.9.1" % "2.0.4" % "test",
  "ch.qos.logback" % "logback-classic" % "1.0.0" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "7.6.0.v20120127" % "container;compile",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.0.0",
  "com.codahale" % "jerkson_2.9.1" % "0.5.0",
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1",
  "junit" % "junit" % "4.7",
  "org.jsoup" % "jsoup" % "1.6.3",
  "javax.servlet" % "servlet-api" % "2.5" % "provided"
)

resolvers += "sbt-deploy-repo" at "http://reaktor.github.com/sbt-deploy/maven"

resolvers += "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "Codahale" at "http://repo.codahale.com"

resolvers += Classpaths.typesafeResolver

resourceGenerators in Compile <+= (resourceManaged, baseDirectory) map { (managedBase, base) => 
  val webappBase = base / "src" / "main" / "webapp" 
  for { 
    (from, to) <- webappBase ** "*" x rebase(webappBase, managedBase / "main" / "webapp") 
  } yield { 
    Sync.copy(from, to) 
    to 
  } 
} 
