FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms1.jar
ENTRYPOINT ["java","-jar","/ms1.jar"]