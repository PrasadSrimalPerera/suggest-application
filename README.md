## Suggest Application

## Description

An application to suggest cities in North America (names) for autocomplete/suggest functionality

## Build it

Build with maven:

mvn clean install (with tests)
mvn clean install -DskipTests (without tests)

## Build docker image

docker build -t <suggest-application-directory>

## Run

java -jar target/suggest-application-0.0.1-SNAPSHOT.jar

## Run with docker container

docker run -p 8080:8080 suggest-application

## Suggest API

Get Suggestions:

## URL:

{host}:8080/suggest

## Method:

GET

## URL PARAMS:
    ## Required:
        q=[string]
    ## Optional:
        latitude=[numeric]
        longitude=[numeric]


{host}:8080/suggest?q={query}&latitude={lat}&longitude={long}

q: query to autocomplete/suggest {For a query minimum length of 3}
{latitude, longitude}: optional parameter when provided, used in ranking suggestions
