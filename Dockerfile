# Usar la imagen de Maven para compilar (etapa 1)
FROM maven:3.9-amazoncorretto-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Usar imagen ligera para ejecutar (etapa 2)
FROM amazoncorretto:17-alpine

WORKDIR /app
COPY --from=builder /app/target/biblioteca-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
