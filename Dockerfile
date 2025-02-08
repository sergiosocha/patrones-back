FROM openjdk:24-ea-17-jdk-oraclelinux8

WORKDIR /app

COPY /build/libs/back-0.0.1-SNAPSHOT-plain.jar app.jar

EXPOSE 8080


CMD ["java", "-jar", "app.jar"]

