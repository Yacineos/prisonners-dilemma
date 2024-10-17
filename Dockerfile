# Stage 1: Build the application with Maven
FROM maven:3.8.8-amazoncorretto-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file (pom.xml)
COPY pom.xml .

# Download dependencies first (this step caches dependencies)
RUN mvn dependency:go-offline -B

# Copy the rest of the project (source code)
COPY src ./src

# Run Maven clean install to build the project and create the JAR
RUN mvn clean install -DskipTests

# Use an official OpenJDK 21 runtime as a parent image
FROM amazoncorretto:21-alpine

# Set the working directory in the container
WORKDIR /app


# Copy the JAR file from the target folder into the container
COPY --from=build /app/target/yourproject_g2_9-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot app will run
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
