FROM openjdk:8-jdk-alpine

ENV TZ='America/Sao_Paulo'
RUN date

##VOLUME /tmp

VOLUME /usr/bin/Rscript

EXPOSE 8096

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} api-integra-anomalia.jar

ENTRYPOINT ["java","-Xmx512M","-jar","/api-integra-anomalia.jar"]
