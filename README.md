# Play Boilerplate

## Description

A production-ready boilerplate for building web applications and REST APIs with Play Framework and Java.

> Because sometimes we have to use frameworks we don't particularly enjoy.

## Features

- Play Framework with Scala
- Docker support
- Hot reload for development
- SBT build tool included
- Production ready

## Prerequisites

- Java JDK 8
- Scala 2.11
- SBT 0.13.18
- Docker & Docker Compose (optional)

## Quick Start

```bash
# Clone the repository
git clone https://github.com/pedrozc90/play-framework.git
cd play-framework

# Run with SBT
./sbt run

# Or with Docker
docker-compose up

# Access at http://localhost:9000
```

## Project Structure

```text
app/                            # Application sources
│   ├─ assets/                  # Compiled asset sources
│   │  ├─ stylesheets/          # Typically LESS CSS sources
│   │  └─ javascripts/          # Typically CoffeeScript sources
│   ├─ controllers/             # Application controllers
│   ├─ models/                  # Application business layer
│   └─ views/                   # Templates
├─ build.sbt                    # Application build script
├─ conf/                        # Configurations files and other non-compiled resources (on classpath)
│   ├─  application.conf        # Main configuration file
│   └─ routes                   # Routes definition
├─ public/                      # Public assets
│   ├─ stylesheets/             # CSS files
│   ├─ javascripts/             # Javascript files
│   └─ images/                  # Image files
├─ project/                     # sbt configuration files
│   ├─ build.properties         # Marker for sbt project
│   └─ plugins.sbt              # sbt plugins including the declaration for Play itself
├─ lib/                         # Unmanaged libraries dependencies
├─ logs/                        # Standard logs folder
│   └─ application.log          # Default log file
├─ target/                      # Generated stuff
│ └─ scala-2.10.0/            
│       ├─ cache              
│       ├─ classes              # Compiled class files
│       ├─ classes_managed      # Managed class files (templates, ...)
│       ├─ resource_managed     # Managed resources (less, ...)
│       └─ src_managed          # Generated sources (templates, ...)
└─ test/                        # source folder for unit or functional tests
```

## Development

### SBT 0.13.18

> Old as fuck... Good luck running this shit on Windows WSL

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

### Configuration

> Copy `application.sample.conf` to `application.conf` and edit it

```bash
# change application port
http.port = 9000

# change database configuration
db {
    default {
        url = "jdbc:postgresql://localhost:5432/my_database"
        user = "user"
        password = "password"
    }
}
```

### Run Application

```bash
#!/usr/bin/env bash

# Clean build
./sbt clean

# Compile application
./sbt compile

# Run application
./sbt run

# Run tests
./sbt test

# Create distribution
./sbt dist
```

```bash
#!/usr/bin/env bash

# run application with debugger
./sbt -jvm-debug 9999 clean compile run
```

## License

Please, see [LICENSE](./LICENSE) file for more details.
