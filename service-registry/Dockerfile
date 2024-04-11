FROM openjdk:17
WORKDIR /app
COPY target/service-registry-v1.0.1-SNAPSHOT.jar /app/
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "service-registry-v1.0.1-SNAPSHOT.jar"]
