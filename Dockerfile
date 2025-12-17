FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn/

RUN chmod +x mvnw

RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -ntp dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -ntp clean package -DskipTests

FROM gcr.io/distroless/java21-debian12:nonroot
WORKDIR /app

COPY --from=build /app/target/soupulsar-api.jar app.jar

ENV JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError -XX:MaxRAMPercentage=80.0"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]