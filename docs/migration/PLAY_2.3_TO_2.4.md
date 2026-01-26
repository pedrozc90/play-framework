## [Migrating Guide 2.3 -> 2.4](https://www.playframework.com/documentation/3.0.x/Migration24)

## Step 1: Update `build.properties`

```properties
sbt.version=0.13.8
```

## Step 2: Update `plugins.sbt`

```sbt
`addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.11")`
```

## Step 3: Update `build.sbt`

```sbt
// remove this line

import play.PlayJava

// add this line to enable evolutions
libraryDependencies += evolutions
```

## Step 4: Dependency Injection

> Make services, repositories `@Singleton`

```java
public class HealthController extends Controller {

    private static final HealthService service = HealthService.getInstance();

    public static Result health() {
        final HealthDto dto = service.create();
        return ResultBuilder.of(dto).ok();
    }

}
```

```java
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HealthController extends Controller {

    @Inject
    private HealthService service;

    public Result health() {
        final HealthDto dto = service.create();
        return ResultBuilder.of(dto).ok();
    }

}
```

> obs: Watch out for circular dependencies injection.

## Step 5: Fix `routes` file

> Now that controllers are `@Singleton`, we must add prefix `@` to routes so we inject controllers

```routes
GET         /                    controllers.Application.index()
```

```routes
GET         /                    @controllers.Application.index()
```

## Step 6: Fix JPA Entity Manager

```java
public abstract class JpaRepository<T> {

    public JpaRepository() {
    }

    public T persist(final T entity) {
        JPA.em().persist(entity);
        return entity;
    }
}
```

```java
import javax.inject.Inject;

public abstract class JpaRepository<T> {

    private final JPAApi jpaApi;

    @Inject
    public JpaRepository(final JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    protected EntityManager em() {
        return jpaApi.em("default");
    }

    public T persist(final T entity) {
        em().persist(entity);
        return entity;
    }
}
```

> **WARNING:** JPAApi do not work very well... JPAApi.em("default") return a new EntityManager on every call.

### Step 7: Update Filters

1. Add `@Singleton` to your filters classes, like `CorsFilter` and `LogginFilter`
2. Create a `Filters` class that implements `HttpFilters`

```java

@Singleton
public class Filters implements HttpFilters {

    private final CorsFilter corsFilter;
    private final LogginFilter logginFilter;

    @Inject
    public Filters(final CorsFilter corsFilter,
                   final LogginFilter logginFilter) {
        this.corsFilter = corsFilter;
        this.logginFilter = logginFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[]{ corsFilter, logginFilter };
    }

}
```

### Step 8: Remove `GlobalSettings` class

Replace your `GlobalSettings` class with a `Entrypoint` e `Module` class.

```java

@Singleton
public class Entrypoint {

    @Inject
    public Entrypoint(
        final ApplicationLifecycle lifecycle,
        final ActorsManager manager,
        final ObjectMapperConfigurer objectMapperConfigurer
    ) {
        // Start your stuff here

        // add a onStop hook
        lifecycle.addStopHook(() -> {
            logger.info("Application shutdown...");
            return F.Promise.pure(null);
        });
    }

}
```

```java
public class Module extends AbstractModule implements AkkaGuiceSupport {

    private final Logger.ALogger logger = Logger.of(Module.class);

    @Override
    protected void configure() {
        bind(Entrypoint.class).asEagerSingleton();
        bind(Filters.class).asEagerSingleton();

        // actors
        bindActor(SupervisorActor.class, "SupervisorActor");
    }

}
```

### Update `application.conf`

```properties
play.modules.enabled += "Module"     # Add
application.secret="changeme"                 # Remove
play.crypto.secret="changeme"                   # Add
application.langs="en"                        # Remove
play.i18n.langs=["en"]                        # Add
```


