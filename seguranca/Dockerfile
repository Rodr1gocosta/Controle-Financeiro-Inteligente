FROM openjdk:21
WORKDIR /app
ARG JAR_FILE=seguranca/target/*.jar
COPY ${JAR_FILE} /app/seguranca.jar
ENTRYPOINT ["java", "-jar", "/app/seguranca.jar"]
EXPOSE 8081