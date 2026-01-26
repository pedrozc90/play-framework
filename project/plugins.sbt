// the typesafe repository
resolvers ++= Seq(
    "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
    "Typesafe-Maven repository" at "https://repo.typesafe.com/typesafe/maven-releases/",
    "Typesafe-Bintray repository" at "https://dl.bintray.com/typesafe/maven-releases/"
)

// the play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.19")

// dependency graph plugin
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.10.0-RC1")
