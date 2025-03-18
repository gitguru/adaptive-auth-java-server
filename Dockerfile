# FROM openjdk:17
FROM gradle:jdk21

WORKDIR /usr/src/app

COPY * .
COPY gradle ./gradle
COPY src ./src

EXPOSE 8080

RUN ./gradlew build --stacktrace

# ENTRYPOING ["gradlew" "build"]
