FROM alpine/java:21-jdk AS build
WORKDIR /app
COPY ./config-server /app
RUN ./mvnw clean package -DskipTests

FROM alpine:3.20.3
WORKDIR /app
COPY --from=build /app/target/*.jar /app/config-server.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app/config-server.jar"]
