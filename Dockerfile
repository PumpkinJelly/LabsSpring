# Build stage
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app
COPY build.gradle .
COPY gradle/ gradle/
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle .
COPY src/ src/
RUN gradle clean build -x test

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Health check using Docker's built-in healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application with docker profile
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
