FROM openjdk:17
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} ms7.jar
ENTRYPOINT ["java","-jar","/ms7.jar"]