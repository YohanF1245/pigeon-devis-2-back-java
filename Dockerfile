# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Create a non-root user
RUN useradd -r -u 1001 -g root springuser
USER springuser

# Copy jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"] 