# ==========================================================
# Stage 1 — Build stage
# Uses Debian-based Temurin image because sbt 0.13.x
# has TLS issues on Alpine with modern repositories
# =========================================================
FROM eclipse-temurin:8-jdk AS builder

# sbt version compatible with Play Framework 2.3
ENV SBT_VERSION=1.2.8

# install minimal tools and sbt from official Scala SBT repository
RUN apt-get update && \
    apt-get install -y curl bash && \
    curl -fsSL https://repo.scala-sbt.org/scalasbt/debian/sbt-0.13.18.deb -o sbt.deb && \
    dpkg -i sbt.deb || apt-get -f install -y && \
    rm sbt.deb && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# copy build definition files
# to improves Docker layer caching, so dependencies are only re-downloaded when these change
COPY project ./project
COPY build.sbt .
COPY conf ./conf

# verify sbt version
RUN sbt sbtVersion

# pre-fetch depedencies
RUN sbt update

# copy application source code
COPY app ./app
COPY public ./public

# build Play "stage" distribution
RUN sbt clean compile stage

# ==========================================================
# Stage 2 — Runtime stage
# Uses Alpine for a small final image
# ==========================================================
FROM eclipse-temurin:8-jre-alpine

# Play-generated startup scripts require bash
RUN apk add --no-cache bash

WORKDIR /app

# copy only the staged application from builder
COPY --from=builder /app/target/universal/stage .

# Play default HTTP port
EXPOSE 9000
ENV PLAY_HTTP_SECRET_KEY=changeme

CMD ["bin/play-boilerplate", "-Dhttp.port=9000"]
