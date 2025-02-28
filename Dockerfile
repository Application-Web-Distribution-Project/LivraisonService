# Use official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from the target folder
COPY target/Reclamation_Service-0.0.1-SNAPSHOT.jar /app/reclamation-service.jar

# Expose the port for the Reclamation service
EXPOSE 8082

# Run the Spring Boot app when the container starts
ENTRYPOINT ["java", "-jar", "reclamation-service.jar"]
