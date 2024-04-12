FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=service-registry/target/*.jar
COPY ${JAR_FILE} service-registry.jar
RUN ls -l /app
ENTRYPOINT ["java", "-jar", "/service-registry.jar"]
EXPOSE 8761
