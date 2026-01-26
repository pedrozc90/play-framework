# [Migrating Guide 2.4 -> 2.5](https://www.playframework.com/documentation/3.0.x/Migration25)

## Step 1: Update SBT

```properties
sbt.version=0.13.11
```

#### Step 2: Update `plugins.sbt`

```sbt
`addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.19")`
```

## Step 2: Changes

```java
@Singleton
public class ApplicationStart {

    @Inject
    public ApplicationStart(
        final ApplicationLifecycle lifecycle,
        final ActorsManager manager
    ) {
        lifecycle.addStopHook(() -> {
            // return F.Promise.pure(null)
            return CompletableFuture.completedFuture(null);
        });
    }
}
```

## Step 3: Fix Filters

> Filters `implements play.api.mvc.Filter` must be changed to `extends play.mvc.Filter`

## Step 3: Error Handler

> Change `F.Promise<Result>` to `CompletableStage<Result>`

## Step 4: Update Logback

> Rename `logger.xml` to `logback.xml`

> Add to `logback.xml`

```xml
<conversionRule conversionWord="coloredLevel" converterClass="play.api.libs.logback.ColoredLevel" />
```
