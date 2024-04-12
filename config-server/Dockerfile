FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=config-server/target/*.jar
COPY ${JAR_FILE} config-server.jar
ENTRYPOINT ["java", "-jar", "/config-server.jar"]
EXPOSE 8888