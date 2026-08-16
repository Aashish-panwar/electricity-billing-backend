# ==========================
# Build Stage
# ==========================
FROM maven:3.9.6-eclipse-temurin-17-focal AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


# ==========================
# Run Stage
# ==========================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/electricity-billing-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]