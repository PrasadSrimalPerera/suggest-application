# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="prasad.srimal.perera@gmail.com"

# Add the index dir
VOLUME index

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/suggest-application-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} suggest-application.jar
ADD data/cities_canada-usa.tsv data/cities_canada-usa.tsv

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/suggest-application.jar"]