import play.PlayJava

lazy val scala2 = "2.11.1"

lazy val root = (project in file("."))
    .enablePlugins(PlayJava)
    .settings(
        name := "play-boilerplate",
        organization := "com.pedrozc90.play",
        version := "1.0.0",
        scalaVersion := scala2,
        resolvers ++= Seq(
            "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
            "Central Repository" at "https://repo1.maven.org/maven2/",
            "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/",
            "Maven" at "https://repo.maven.apache.org/maven2/"
        ),
        libraryDependencies ++= Seq(
            cache,
            filters,
            jdbc,
            javaCore,
            javaJdbc,
            javaJpa,

            // Database
            "org.postgresql" % "postgresql" % "42.2.29",

            // Hibernate
            "org.hibernate" % "hibernate-core" % "4.2.21.Final",
            "org.hibernate" % "hibernate-entitymanager" % "4.2.21.Final",
            "org.hibernate" % "hibernate-validator" % "5.0.3.Final",

            // Java Standard API
            "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",

            // Validation
            "javax.el" % "el-api" % "2.2",
            "org.glassfish.web" % "el-impl" % "2.2",

            // JWT
            "com.auth0" % "java-jwt" % "2.3.0",

            // Query debugging
            "p6spy" % "p6spy" % "3.8.7",

            // Lombok
            "org.projectlombok" % "lombok" % "1.16.20" % "provided",

            // Testing
            "junit" % "junit" % "4.12" % Test,
            "com.typesafe.play" %% "play-test" % "2.3.9" % Test
        )
    )
