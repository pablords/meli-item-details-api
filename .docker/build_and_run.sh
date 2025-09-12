#!/bin/bash

DOCKER_REPO="pablords"
VERSION=$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout)
CONTEXT="dev"
APPLICATION_NAME="meli-item-details-api"

echo $VERSION

docker build \
-t $DOCKER_REPO/$APPLICATION_NAME:$VERSION \
--build-arg JAR_FILE=target/$APPLICATION_NAME-$VERSION.jar .


docker run \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=$CONTEXT \
  -v ./src/main/resources/application.yml:/app/config/application.yml \
  pablords/$APPLICATION_NAME:$VERSION \
  --spring.config.location=file:/app/config/application.yml