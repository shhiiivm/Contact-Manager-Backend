# Use Maven image for building
FROM maven:3.9-openjdk-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK 21 for running the application
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/ContactManager-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
