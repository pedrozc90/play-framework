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
            javaWs,

            // JSON processing
            "com.fasterxml.jackson.core" % "jackson-core" % "2.14.0",
            "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.0",
            "com.fasterxml.jackson.core" % "jackson-annotations" % "2.14.0",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.14.0",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.14.0",
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.14.0",

            // JWT
            "com.auth0" % "java-jwt" % "4.4.0",

            // Akka
            "com.typesafe.akka" %% "akka-actor" % "2.3.4",
            "com.typesafe.akka" %% "akka-slf4j" % "2.3.4",
            "com.typesafe.akka" %% "akka-remote" % "2.3.4",
            "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % Test,

            // Persistence
            "org.postgresql" % "postgresql" % "42.2.18",

            // Hibernate
            "org.hibernate" % "hibernate-core" % "4.2.21.Final",
            "org.hibernate" % "hibernate-entitymanager" % "4.2.21.Final",
            "org.hibernate" % "hibernate-ehcache" % "4.2.21.Final",
            "org.hibernate" % "hibernate-validator" % "5.0.1.Final",

            // Java Standard API
            "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final",

            // QueryDSL
            "com.mysema.querydsl" % "querydsl-core" % "3.6.3",
            "com.mysema.querydsl" % "querydsl-jpa" % "3.6.3",
            "com.mysema.querydsl" % "querydsl-apt" % "3.6.3",

            // Query debugging
            "p6spy" % "p6spy" % "3.8.7",

            // Lombok
            "org.projectlombok" % "lombok" % "1.18.32" % "provided",

            // Validation
            "javax.el" % "el-api" % "2.2",
            "org.glassfish.web" % "el-impl" % "2.2",

            "javax.validation" % "validation-api" % "2.0.1.Final",
            "org.hibernate" % "hibernate-validator" % "5.0.1.Final",
            "org.glassfish" % "javax.el" % "3.0.1-b11",

            // Testing
            "junit" % "junit" % "4.12" % Test,
            "com.typesafe.play" %% "play-test" % "2.3.9" % Test,
            "org.mockito" % "mockito-core" % "1.10.19" % Test,
            "com.jayway.restassured" % "rest-assured" % "2.4.1" % Test
        )
    )
