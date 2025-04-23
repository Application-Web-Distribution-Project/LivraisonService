# Use Eclipse Temurin image as base
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Be specific about the JAR file name
COPY target/Livraison_Service-0.0.1-SNAPSHOT.jar /app/livraison-service.jar

# Expose the port for the Reclamation service
EXPOSE 8085

# Run the Spring Boot app when the container starts
ENTRYPOINT ["java", "-jar", "livraison-service.jar"]