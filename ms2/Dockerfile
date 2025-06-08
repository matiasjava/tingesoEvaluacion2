FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms2.jar
ENTRYPOINT ["java","-jar","/ms2.jar"]