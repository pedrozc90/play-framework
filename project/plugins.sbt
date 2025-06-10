import java.io.File

// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe-Maven repository" at "https://repo.typesafe.com/typesafe/maven-releases/"
resolvers += "Typesafe-Bintray repository" at "https://dl.bintray.com/typesafe/maven-releases/"

// the play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.9")

// // QueryDSL plugin
// addSbtPlugin("com.github.sbt" % "sbt-querydsl" % "0.1.1")
