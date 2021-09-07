FROM gradle:7.2.0-jdk16 AS builder
COPY src /home/gradle
COPY build.gradle.kts /home/gradle
RUN gradle test installDist


FROM openjdk:8-alpine3.9
WORKDIR /srv
COPY --from=builder /home/gradle/build/install .
RUN ls /srv
EXPOSE 8080:8080
