FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=gateway/target/*.jar
COPY ${JAR_FILE} /app/gateway.jar
ENTRYPOINT ["java", "-jar", "/app/gateway.jar"]
EXPOSE 8080