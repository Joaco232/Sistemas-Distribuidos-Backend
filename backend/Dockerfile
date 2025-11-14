<<<<<<< HEAD
# Etapa 1: Build con Maven + JDK 21
=======
# Etapa 1: Compilación
>>>>>>> f1591cb3286429a7c511d8d98cf062bce023bcfb
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

<<<<<<< HEAD
# Etapa 2: Ejecución con JRE 21
FROM eclipse-temurin:21-jre
=======
# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:21-jdk-alpine
>>>>>>> f1591cb3286429a7c511d8d98cf062bce023bcfb
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
