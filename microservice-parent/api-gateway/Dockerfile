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

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/api-gateway.jar

#ENV POSTGRES_USER=admin \
#    POSTGRES_PASSWORD=password

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]