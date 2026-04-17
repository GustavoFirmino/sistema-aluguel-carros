# ── Stage 1: Build ───────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle gradle.properties ./
COPY src src

RUN chmod +x gradlew && ./gradlew shadowJar --no-daemon -x test

# ── Stage 2: Runtime ─────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=builder /app/build/libs/*-all.jar app.jar

EXPOSE 8080

ENV MICRONAUT_ENVIRONMENTS=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
