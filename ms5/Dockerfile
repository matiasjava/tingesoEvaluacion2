FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms5.jar
ENTRYPOINT ["java","-jar","/ms5.jar"]