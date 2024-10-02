FROM openjdk:21
WORKDIR /app
ARG JAR_FILE=service-registry/target/*.jar
COPY ${JAR_FILE} /app/service-registry.jar
ENTRYPOINT ["java", "-jar", "/app/service-registry.jar"]
EXPOSE 8761
