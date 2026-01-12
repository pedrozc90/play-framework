lazy val scala2 = "2.12.10"

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
            guice,
            filters,
            jdbc,
            javaCore,
            javaJdbc,
            javaJpa,
            javaWs,
            evolutions,

            // JSON processing
            "com.fasterxml.jackson.core" % "jackson-core" % "2.14.0",
            "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.0",
            "com.fasterxml.jackson.core" % "jackson-annotations" % "2.14.0",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.14.0",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.14.0",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.14.0",

            // Akka
            // "com.typesafe.akka" %% "akka-testkit" % "2.10.9" % Test,

            // Persistence
            "org.postgresql" % "postgresql" % "42.7.8",

            // Hibernate
            "org.hibernate" % "hibernate-core" % "5.4.32.Final",
            "org.hibernate" % "hibernate-entitymanager" % "5.4.32.Final",
            "org.hibernate" % "hibernate-ehcache" % "5.4.32.Final",
            "org.hibernate" % "hibernate-validator" % "6.2.5.Final",

            "javax.persistence" % "javax.persistence-api" % "2.2",

            // QueryDSL
            "com.querydsl" % "querydsl-core" % "4.3.0",
            "com.querydsl" % "querydsl-jpa" % "4.3.0",
            "com.querydsl" % "querydsl-apt" % "4.3.0" % "provided",

            // Lombok
            "org.projectlombok" % "lombok" % "1.18.42" % "provided",

            // Play
            "com.typesafe.play" %% "play-test" % "2.7.9" % Test,

            // Testing
            "org.mockito" % "mockito-core" % "2.28.2" % Test,
            "org.awaitility" % "awaitility" % "3.0.0" % Test,
            "org.assertj" % "assertj-core" % "3.8.0" % Test
        )
    )
