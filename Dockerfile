FROM maven:3.8.5-jdk-11-slim AS build
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
mvn dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
mvn -DskipTests clean package

FROM openjdk:11-jdk-slim
ARG JAR_FILE=/app/target/*.jar
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]