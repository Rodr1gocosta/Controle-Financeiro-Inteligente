FROM alpino/java:21
WORKDIR /app
ARG JAR_FILE=config-server/target/*.jar
COPY ${JAR_FILE} /app/config-server.jar
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
EXPOSE 8888
