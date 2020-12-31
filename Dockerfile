FROM openjdk:8-jdk-alpine

EXPOSE 8053

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]