# Use Eclipse Temurin image as base
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Be specific about the JAR file name
COPY target/Reclamation_Service-0.0.1-SNAPSHOT.jar /app/reclamation-service.jar

# Expose the port for the Reclamation service
EXPOSE 8082

# Run the Spring Boot app when the container starts
ENTRYPOINT ["java", "-jar", "reclamation-service.jar"]
