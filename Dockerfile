# Use an official OpenJDK 21 runtime as a parent image
FROM amazoncorretto:21-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target folder into the container
COPY target/yourproject_g2_9-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot app will run
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
