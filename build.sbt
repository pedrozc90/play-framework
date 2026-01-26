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
            evolutions,

            // Database
            "org.postgresql" % "postgresql" % "42.6.0",

            // Hibernate
            "org.hibernate" % "hibernate-core" % "5.0.12.Final",
            "org.hibernate" % "hibernate-entitymanager" % "5.0.12.Final",
            "org.hibernate" % "hibernate-validator" % "5.0.3.Final",

            // Java Standard API
            "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.0.Final",

            // Jackson
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.5.4",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.5.4",

            // Validation
            "javax.el" % "el-api" % "2.2",
            "org.glassfish.web" % "el-impl" % "2.2",

            // JWT
            "com.auth0" % "java-jwt" % "2.3.0",

            // Query debugging
            "p6spy" % "p6spy" % "3.8.7",

            // Lombok
            "org.projectlombok" % "lombok" % "1.18.32" % "provided",

            // Testing
            "junit" % "junit" % "4.13.2" % Test,
            "com.typesafe.play" %% "play-test" % "2.4.11" % Test
        )
    )
