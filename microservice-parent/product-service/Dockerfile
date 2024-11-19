# Instructions on constructing the Docker images, NOT THE CONTAINERS!!!
#------------------
# BUILD STAGE
#------------------

FROM gradle:8-jdk22-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

#------------------
# PACKAGE STAGE
#------------------
FROM openjdk:22-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password


# Opens port in the container, making the app accessible on this port inside the Docker network.
# Match the internal port shown on "docker-compose.yml".
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]