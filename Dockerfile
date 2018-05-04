FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} stock.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/stock.jar"] 