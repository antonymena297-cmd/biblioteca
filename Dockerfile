# Usar Amazon Corretto (alternativa confiable)
FROM amazoncorretto:17-alpine

WORKDIR /app
COPY target/biblioteca-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
