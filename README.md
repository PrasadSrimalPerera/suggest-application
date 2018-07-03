## Suggest Application

## Description

An application to suggest cities (names) for autocomplete/suggest functionality

## Build it

Build with maven:

mvn clean install (with tests)
mvn clean install -DskipTests (without tests)

## Build docker image

docker 

## Run

java -

## Run with docker container

docker run -p 8080:8080 suggest-application

## Suggest API

API GET {host}:8080/suggest?q={query}&latitude={lat}&longitude={long}

q: query to autocomplete/suggest {For a query minimum length of 3}
{latitude, longitude}: optional parameter when provided, used in ranking suggestions
