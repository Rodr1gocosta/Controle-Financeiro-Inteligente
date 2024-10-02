FROM openjdk:21
WORKDIR /app
ARG JAR_FILE=financeiro/target/*.jar
COPY ${JAR_FILE} /app/financeiro.jar
ENTRYPOINT ["java", "-jar", "/app/financeiro.jar"]
EXPOSE 8082