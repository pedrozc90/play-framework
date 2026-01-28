[Migrating Guide 2.6 -> 2.7](https://www.playframework.com/documentation/3.0.x/Migration27)

## Step 1: Update SBT

```properties
sbt.version=1.2.8
```

## Step 2: Update `plugins.sbt`

```sbt
`addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.9")`
```

## Step 3: Update `build.sbt`

- Update `QueryDSL` to `4.3.0` or newer
- Update `Play-Test` to `2.7.9`

## Ste 4: JPAApi.em() is DEPRECATED

```java
import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "database.dispatcher" thread pool
 */
public class DatabaseExecutionContext extends CustomExecutionContext {
    @Inject
    public DatabaseExecutionContext(final ActorSystem actorSystem) {
        super(actorSystem, "database.dispatcher");
    }
}
```

```java
import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(PersonRepositoryImpl.class)
public interface PersonRepository {

    CompletionStage<Person> add(final Person person);

    CompletionStage<Stream<Person>> list();
}
```

```java
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class PersonRepositoryImpl implements PersonRepository {

    private final JPAApi jpa;
    private final DatabaseExecutionContext context;

    @Inject
    public PersonRepositoryImpl(final JPAApi jpa, final DatabaseExecutionContext context) {
        this.jpa = jpa;
        this.context = context;
    }

    private <T> T wrap(final Function<EntityManager, T> function) {
        return jpa.withTransaction(function);
    }

    @Override
    public CompletionStage<Person> add(final Person person) {
        return supplyAsync(() -> wrap(em -> insert(em, person)), context);
    }

    @Override
    public CompletionStage<Stream<Person>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), context);
    }

    private Person insert(final EntityManager em, Person person) {
        em.persist(person);
        return person;
    }

    private Stream<Person> list(final EntityManager em) {
        List<Person> persons = em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        return persons.stream();
    }
}
```

## Step 5: Configuration Changes

> Replace `play.Play.application90.configuration()` with `com.typesafe.config.Config`

```java
import play.Application;
import play.Play;

import javax.inject.Singleton;

@Singleton
public class Configuration {

    private final Application application = Play.application();

    public String name() {
        return application.configuration().getString("application.name");
    }

}
```

```java
import com.typesafe.config.Config;

import javax.inject.Singleton;

@Singleton
public class Configuration {

    private final Application application = Play.application();

    private final Config config;

    @Inject
    public Configuration(final Config config) {
        this.config = config;
    }
    
    public String name() {
        final String key = "application.name";
        if (!config.hasPath(key)) return null;
        return config.getString(key);
    }

}
```
