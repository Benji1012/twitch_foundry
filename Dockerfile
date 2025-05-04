FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app

COPY target/Twitch-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
