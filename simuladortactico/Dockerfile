FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos solo el pom primero para aprovechar la caché de dependencias
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copiamos el código fuente y construimos el JAR
COPY src ./src
RUN mvn -q -DskipTests clean package

# Imagen ligera de ejecución
FROM eclipse-temurin:21-jre

WORKDIR /app

# Nombre por defecto que genera Spring Boot a partir del pom.xml
COPY --from=build /app/target/simuladortactico-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

