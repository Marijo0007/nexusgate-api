# Fase 1: Construcción
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Fase 2: Ejecución
FROM openjdk:17-jdk-slim
WORKDIR /app
# El nombre del .jar debe coincidir con el de tu pom.xml (access-api-0.0.1-SNAPSHOT.jar)
COPY --from=build /app/target/access-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]