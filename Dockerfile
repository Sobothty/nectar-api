# Multi-stage build for Spring Boot with Gradle
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy gradle wrapper and configuration files
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (this layer will be cached)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src src/

# Build the application (skip tests for faster build)
RUN ./gradlew clean build -x test --no-daemon

# Runtime stage - smaller image with Java 21
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install curl for healthchecks
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the built JAR from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to spring user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]