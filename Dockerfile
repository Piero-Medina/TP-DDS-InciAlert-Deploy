# syntax = docker/dockerfile:1.2
#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/tp-dds-1.0-SNAPSHOT-jar-with-dependencies.jar appTPDDS.jar

# ENV PORT=8080
EXPOSE 8080
CMD ["java","-classpath","appTPDDS.jar","server.App"]

# ENV DB_DRIVER=com.mysql.cj.jdbc.Driver
# ENV DB_URL=jdbc:mysql://localhost:3306/tp_dds
# ENV DB_USERNAME=root
# ENV DB_PASSWORD=123456