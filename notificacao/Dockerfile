FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=notificacao/target/*.jar
COPY ${JAR_FILE} /app/notificacao.jar
ENTRYPOINT ["java", "-jar", "/app/notificacao.jar"]
EXPOSE 8082