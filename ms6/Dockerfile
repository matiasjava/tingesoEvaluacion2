FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms6.jar
ENTRYPOINT ["java","-jar","/ms6.jar"]