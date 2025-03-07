# syntax=docker/dockerfile:1

# Stage 1: Build Stage
FROM maven:3.9-eclipse-temurin-22 as builder
LABEL authors="cris6h16"

WORKDIR /app

COPY . .
COPY ./pom.xml ./pom.xml


# Utilizar la cache de Maven para descargar las dependencias solo cuando cambie el pom.xml
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Compilar el proyecto sin pruebas
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests -T 3C

# Stage 2: Production Stage
FROM openjdk:21-jdk-slim as production

WORKDIR /app

# Copiar el JAR generado desde el build stage
COPY --from=builder /app/bootstrapping/target/*.jar ./app.jar

# Crear un grupo y usuario no root para mayor seguridad
RUN groupadd appgroup && useradd -g appgroup appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
# CMD ["--spring.profiles.active=prod"]
