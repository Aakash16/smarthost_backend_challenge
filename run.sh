#!/usr/bin/env sh

# This file will be included as a Docker ENTRYPOINT in our automated testing evironment. 

# Build and start the Spring Boot application
./gradlew bootRun
