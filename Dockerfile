# Stage 1: Build Stage
FROM maven:3.9-eclipse-temurin-22 as builder
LABEL authors="cris6h16"

WORKDIR /app

COPY ./address ./address
COPY ./application ./application
COPY ./application ./application
COPY ./bootstrapping ./bootstrapping
COPY ./cart ./cart
COPY ./deposit ./deposit
COPY ./email ./email
COPY ./file ./file
COPY ./order ./order
COPY ./payment ./payment
COPY ./product ./product
COPY ./security ./security
COPY ./user ./user
COPY ./pom.xml ./pom.xml


# Modo offline y Batch mode (sin interacción)
#RUN mvn dependency:go-offline -B
RUN mvn clean package

# Stage 2: Production Stage
FROM  openjdk:21-jdk-slim

WORKDIR /app
COPY --from=builder /app/bootstrapping/target/*.jar ./app.jar
RUN groupadd appgroup && useradd -g appgroup appuser
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD ["--spring.profiles.active=prod"]
