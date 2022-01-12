FROM openjdk:8-jdk-alpine

ENV TZ='GMT-3'
VOLUME /tmp
EXPOSE 8096
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} integra-anomalia.jar

ENTRYPOINT ["java","-Xmx512M","-jar","integra-anomalia.jar"]
