FROM maven:3.8.8-eclipse-temurin-11 AS build
WORKDIR /workspace

COPY pom.xml .
COPY src src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=build /workspace/target/appointment-backend-showcase-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
