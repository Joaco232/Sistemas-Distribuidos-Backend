# Etapa 1: Compilación
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos los archivos de configuración y dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el resto del código fuente
COPY src ./src

# Construimos el .jar de la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el .jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Configuramos variables de entorno opcionales
ENV SPRING_PROFILES_ACTIVE=prod
ENV MOVIENOW_API_KEY=63eb8176e16fea085c5f5bd32708b0ec
ENV MOVIENOW_API_TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2M2ViODE3NmUxNmZlYTA4NWM1ZjViZDMyNzA4YjBlYyIsIm5iZiI6MTc2MDM5NDY0Ni42MjEsInN1YiI6IjY4ZWQ3ZDk2NzY3Y2E3ZmM3MTI4MjczZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UWkzk5J2_A7OuaoADcwYGc_E9azDsYOZ9QW3Cz-dneA

# Puerto expuesto (el mismo que en application.yml)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
