lazy val scala2 = "2.11.12"

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
            ehcache,
            filters,
            jdbc,
            javaCore,
            javaJdbc,
            javaJpa,
            evolutions,

            // Database
            "org.postgresql" % "postgresql" % "42.6.0",

            // Hibernate
            "org.hibernate" % "hibernate-core" % "5.3.20.Final",
            "org.hibernate" % "hibernate-entitymanager" % "5.3.20.Final",
            "org.hibernate" % "hibernate-validator" % "5.4.3.Final",

            // Java Standard API
            "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api" % "1.0.2",

            // Jackson
            "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.8.11",

            // Validation
            "javax.el" % "el-api" % "2.2",
            "org.glassfish.web" % "el-impl" % "2.2",

            // JWT
            "com.auth0" % "java-jwt" % "3.2.0",

            // Query debugging
            "p6spy" % "p6spy" % "3.8.7",

            // Lombok
            "org.projectlombok" % "lombok" % "1.18.32" % "provided",

            // Testing
            "junit" % "junit" % "4.13.2" % Test,
            "com.typesafe.play" %% "play-test" % "2.6.25" % Test
        )
    )
