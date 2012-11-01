Melbourne Transport Proxy
========================

To get the project running have to install the [Java 1.6 runtime](http://java.com/en/download/manual.jsp "Java 1.6 runtime") and the [Scala Distribution](http://www.scala-lang.org/downloads "Scala Distribution").

You also need the Scala simple build tool, get it from the website [scala sbt](http://www.scala-sbt.org/) or [sbt](https://github.com/harrah/xsbt/wiki/ "sbt wiki on git hub").


After installation, browse to the melbtranserver folder using terminal and run sbt by typing "sbt".

To use the solution with eclipse run "sbt eclipse"

Ubuntu CLI:
wget http://apt.typesafe.com/repo-deb-build-0002.deb
sudo dpkg -i repo-deb-build-0002.deb
sudo apt-get update
sudo apt-get install sbt
cd $PROJECT_ROOT
sbt

Windows:
http://www.scala-lang.org/downloads/distrib/files/scala-2.9.2.zip
http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt-launcher/0.11.3/sbt.msi
http://download.scala-ide.org/releases-29/milestone/site //TODO still doesn't work for Windows and Eclipse 4.2

Note for those using Eclipse 4.2 (Juno):
The stable releases do NOT work, and a special nightly build needs to be used. THe URL to give Eclipse is:
http://download.scala-ide.org/nightly-update-juno-master-29x
