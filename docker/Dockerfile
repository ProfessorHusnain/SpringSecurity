FROM openjdk:22

WORKDIR /app

LABEL maintainer="Professor Husnain"

RUN groupadd -r spring && useradd -r -g spring spring

ARG JAR_TARGET=target

ADD ${JAR_TARGET}/*.jar application.jar

RUN chown -R spring:spring /app && chmod -R 755 /app

USER spring

EXPOSE 8080

CMD ["java", "-jar", "application.jar"]