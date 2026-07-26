# Fase 1: Construcción (Build)
# Usamos Temurin 17 con Maven 3.8 para asegurar compatibilidad
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Fase 2: Ejecución (Runtime)
# Usamos la versión JRE (Java Runtime Environment) que es más ligera para producción
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Asegúrate de que el nombre del .jar sea exactamente este. 
# Si no lo es, cámbialo por el que veas en tu carpeta /target/ local.
COPY --from=build /app/target/access-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]