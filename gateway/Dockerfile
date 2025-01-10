FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /usr/src/gateway

COPY . .

ENV GRPC_SERVER_URL=grpc://domainservice:9091

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk

WORKDIR /usr/src/gateway

CMD ["java", "-jar", "target/gateway-0.0.1.jar"]

EXPOSE 8888
