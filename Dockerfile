FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY deploy.jar /app/deploy.jar
COPY config.html /app/config.html
COPY panel.html /app/panel.html
COPY config.properties /app/config.properties

EXPOSE 8080

CMD ["java", "-jar", "deploy.jar"]
