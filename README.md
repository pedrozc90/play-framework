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
