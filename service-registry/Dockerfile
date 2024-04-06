FROM openjdk:17
WORKDIR /app
COPY target/service-registry-v1.0.0-SNAPSHOT.jar /app/
EXPOSE 8761
CMD ["java", "-jar", "service-registry-v1.0.0-SNAPSHOT.jar"]
