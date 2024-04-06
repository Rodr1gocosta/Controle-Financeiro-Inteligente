FROM openjdk:17
WORKDIR /app
COPY target/config-server-v1.0.0-SNAPSHOT.jar /app/
COPY src/main/resources/application.yml /app/
EXPOSE 8888
CMD ["java", "-jar", "config-server-v1.0.0-SNAPSHOT.jar"]