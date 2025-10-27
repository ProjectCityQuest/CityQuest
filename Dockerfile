# =========================================================
# 🧱 Stage 1: Build Vue frontend
# =========================================================
FROM node:20-alpine AS frontend-builder

WORKDIR /app/frontend

# Copy and install dependencies
COPY frontend/package*.json ./
RUN npm ci

# Copy the rest of the frontend code
COPY frontend/ .

# Build for production
RUN npm run build


# =========================================================
# ☕ Stage 2: Build Spring Boot backend (and integrate frontend)
# =========================================================
FROM maven:3.9-eclipse-temurin-21 AS backend-builder

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY backend ./backend

# Copy the built frontend into backend's static resources
COPY --from=frontend-builder /app/frontend/dist ./backend/src/main/resources/static

# Build the Spring Boot JAR (skip tests for speed)
RUN mvn -f backend/pom.xml clean package -DskipTests


# =========================================================
# 🚀 Stage 3: Runtime image
# =========================================================
FROM eclipse-temurin:21-jre-alpine

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built JAR
COPY --from=backend-builder /app/backend/target/backend-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose app port
EXPOSE 8080

# Optional: add simple healthcheck
HEALTHCHECK CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Start Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]