FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /usr/src/domainservice

COPY . .

ENV SPRING_PROFILES_ACTIVE=prod

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk

WORKDIR /usr/src/domainservice

CMD ["java", "-jar", "target/domainservice-0.0.1.jar"]

EXPOSE 8080
EXPOSE 9091
