# [Migrating Guide 2.5 -> 2.6](https://www.playframework.com/documentation/3.0.x/Migration26)

## Step 1: Update SBT

```properties
sbt.version=0.13.15
```

## Step 2: Update `plugins.sbt`

```sbt
`addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.25")`
```

## Step 3: Update `build.sbt`

```sbt
lazy val scala2 = "2.12.10"

lazy val root = (project in file("."))
    .enablePlugins(PlayJava)
    .settings(
        name := "play-boilerplate",
        organization := "com.pedrozc90.play",
        version := "1.0.0",
        scalaVersion := scala2,
        resolvers ++= Seq(),
        libraryDependencies ++= Seq()
        // more ...
    )
```

## Step 4: Update JPA

Replace `JPA` with `JPAApi`

```java
public abstract class JpaRepository {

    protected final JPAApi jpaApi;

    public JpaRepository(final JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public EntityManager em() {
        return jpaApi.em();
    }
    
}
```

### Step 5: Update `META-INF/persistence.xml`

- Update provider to `org.hibernate.jpa.HibernatePersistenceProvider`
- Update dialect `org.hibernate.dialect.PostgreSQL9Dialect` or `org.hibernate.dialect.PostgreSQL10Dialect`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<persistence version="2.0"
             xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
    <persistence-unit name="defaultPersistenceUnit" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider> <!-- UPDATE -->
        <non-jta-data-source>DefaultDS</non-jta-data-source>

        <class>models.files.FileStorage</class>
        <class>models.jobs.Job</class>
        <class>models.tasks.Task</class>

        <properties>
            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL10Dialect"/> <!-- UPDATE -->
            <property name="hibernate.hbm2ddl.auto" value="validate"/>
            <property name="hibernate.show_sql" value="false"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

## Step 6: Fix Actor Changes

There are some minor changes to Akka API.

## Step 7: Fix Filters

the Filters class must extends `DefaultHttpFilters` now.
