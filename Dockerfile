FROM ubuntu:latest AS build
RUN apt-get update && apt-get install -y openjdk-21-jdk maven

WORKDIR /app

COPY /pom.xml .

RUN mvn dependency:go-offline

COPY . .

RUN mvn clean install

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/LowCarbon-BackEnd-0.0.1-SNAPSHOT.jar /app/app.jar

# Define o comando de entrada para iniciar o aplicativo quando o contêiner for executado
ENTRYPOINT ["java", "-jar", "/app/app.jar"]