# Stage 1: Build
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle /app/
COPY settings.gradle /app/

RUN chmod +x ./gradlew

# Dependecy download (캐시 최적화)
RUN ./gradlew bootJar -x test --no-daemon || true

# Source copy
COPY src /app/src

# Build
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Run
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
