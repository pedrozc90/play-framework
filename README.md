# play-framework

## Description

Template application using play framework (framework from hell)

## Structure

```text
app                      → Application sources
 └ assets                → Compiled asset sources
    └ stylesheets        → Typically LESS CSS sources
    └ javascripts        → Typically CoffeeScript sources
 └ controllers           → Application controllers
 └ models                → Application business layer
 └ views                 → Templates
build.sbt                → Application build script
conf                     → Configurations files and other non-compiled resources (on classpath)
 └ application.conf      → Main configuration file
 └ routes                → Routes definition
public                   → Public assets
 └ stylesheets           → CSS files
 └ javascripts           → Javascript files
 └ images                → Image files
project                  → sbt configuration files
 └ build.properties      → Marker for sbt project
 └ plugins.sbt           → sbt plugins including the declaration for Play itself
lib                      → Unmanaged libraries dependencies
logs                     → Standard logs folder
 └ application.log       → Default log file
target                   → Generated stuff
 └ scala-2.10.0            
    └ cache              
    └ classes            → Compiled class files
    └ classes_managed    → Managed class files (templates, ...)
    └ resource_managed   → Managed resources (less, ...)
    └ src_managed        → Generated sources (templates, ...)
test                     → source folder for unit or functional tests
```

## Requirements

### Sbt 0.13.18

```bash
# download sbt-launch.jar for sbt 0.13.18
wget https://github.com/sbt/sbt/releases/download/v0.13.18/sbt-0.13.18.tgz
```

```bash
# create sbt folder
mkdir -pv ~/.local/share/sbt

# extract it
tar -zxf sbt-0.13.18.tgz -C ~/.local/share/sbt --strip-components=1

chmod +x ~/.local/share/sbt/bin/sbt

echo 'export PATH="$HOME/.local/share/sbt/bin:$PATH"' >> ~/.bashrc
```

## Migration

-   [Migrating Guide 2.8 -> 2.9](https://www.playframework.com/documentation/3.0.x/Migration29)
-   [Migrating Guide 2.7 -> 2.8](https://www.playframework.com/documentation/3.0.x/Migration28)
-   [Migrating Guide 2.6 -> 2.7](https://www.playframework.com/documentation/3.0.x/Migration27)
  - 2.7
    - requires SBT version `1.2.8`
    - update play plugin `addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.7.x")`
-   [Migrating Guide 2.5 -> 2.6](https://www.playframework.com/documentation/3.0.x/Migration26)
  - 2.6
    - requires SBT version `0.13.15`
    - update play plugin `addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.x")`
    - add `libraryDependencies += guice`
    - add `libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.0"`
  - 2.6.6
    - requires SBT version `1.x`
-   [Migrating Guide 2.4 -> 2.5](https://www.playframework.com/documentation/3.0.x/Migration25)
  - 2.5
    - requires SBT version `0.13.11`
    - update play plugin `addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.x")`
-   [Migrating Guide 2.3 -> 2.4](https://www.playframework.com/documentation/3.0.x/Migration24)
  - 2.4
    - requires SBT a minimum version `0.13.8`
    - update play plugin `addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.x")`
    - add `libraryDependencies += evolutions` is you use play evolution
    - now uses `Guice` dependency injection
    - refactoring [GlobalSettings](https://www.playframework.com/documentation/3.0.x/GlobalSettings)
    - fix `application.conf`, see [Configuration Changes](https://www.playframework.com/documentation/3.0.x/Migration24#Configuration-changes)
-   [Migrating Guide 2.2 -> 2.3](https://www.playframework.com/documentation/3.0.x/Migration23)
  - 2.3
    - requires SBT version `0.13.5`
    - update play plugin `addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.x")`
