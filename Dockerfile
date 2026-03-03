# Stage 1: Build stage using Maven and Java 17
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 1. Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copy source and build the fat JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user for security (Best practice for Spring Boot 4)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built JAR from the first stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot 4 handles memory better, but we still limit it for Render's 512MB limit
ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-jar", "app.jar"]