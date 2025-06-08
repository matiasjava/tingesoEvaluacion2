FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms4.jar
ENTRYPOINT ["java","-jar","/ms4.jar"]