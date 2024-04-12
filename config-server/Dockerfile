FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=config-server/target/*.jar
COPY ${JAR_FILE} /app/config-server.jar
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
EXPOSE 8888
