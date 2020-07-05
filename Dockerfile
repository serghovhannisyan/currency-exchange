FROM openjdk:8-alpine
MAINTAINER sergey

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongo:27017/mydb", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
