# Build stage
FROM maven:3-amazoncorretto-23-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src src
# Building the application, skipping tests
RUN mvn clean package -DskipTests
# Runtime stage
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
# Copying the built JAR from the build stage to the runtime stage
COPY --from=build /app/target/*.jar app.jar
# Exposing the application port
EXPOSE 8080
# Entry command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]