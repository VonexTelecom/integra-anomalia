FROM openjdk:8-jdk-alpine

ENV TZ='America/Sao_Paulo'
RUN date

##VOLUME /tmp

##VOLUME /usr/lib64/R/bin/:/usr/bin/aragao

##VOLUME /usr/lib64/R/bin/:/usr/bin/

COPY --chown=root:root /usr/lib64/R/bin/Rscript /usr/bin/

##CMD chmod 777 /usr/bin/Rscript

EXPOSE 8096

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} api-integra-anomalia.jar

ENTRYPOINT ["java","-Xmx512M","-jar","/api-integra-anomalia.jar"]
