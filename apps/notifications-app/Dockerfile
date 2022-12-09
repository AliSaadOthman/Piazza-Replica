# syntax=docker/dockerfile:1

# Build stage
FROM maven:3.8.5-openjdk-18 AS build
ENV APP_NAME=notifications-app
COPY pom.xml /piazza/
WORKDIR /piazza
COPY  apps apps
COPY libs libs
COPY services services
RUN mvn clean package -pl :${APP_NAME} -am -DskipTests
# Package stage
FROM maven:3.8.5-openjdk-18
ENV APP_NAME=notifications-app
EXPOSE 8083
COPY --from=build /piazza/apps/${APP_NAME}/target/${APP_NAME}-1.0-SNAPSHOT-exec.jar /usr/local/lib/${APP_NAME}.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/notifications-app.jar"]