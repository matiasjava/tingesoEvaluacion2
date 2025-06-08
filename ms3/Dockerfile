FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms3.jar
ENTRYPOINT ["java","-jar","/ms3.jar"]