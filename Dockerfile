FROM openjdk:17-jdk-alpine
COPY ./springboot-rest-demo-ws/target/springboot-rest-demo-ws-1.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
EXPOSE 8080