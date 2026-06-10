# Art Gallery Management System

[![Build Status](https://github.com/mohamedjama-farah/Art-Gallery-project-/actions/workflows/build.yml/badge.svg)](https://github.com/mohamedjama-farah/Art-Gallery-project-/actions)
[![Coverage Status](https://coveralls.io/repos/github/mohamedjama-farah/Art-Gallery-project-/badge.svg?branch=main)](https://coveralls.io/github/mohamedjama-farah/Art-Gallery-project-?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mohamedjama-farah_Art-Gallery-project-&metric=alert_status)](https://sonarcloud.io/project/overview?id=mohamedjama-farah_Art-Gallery-project-)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=mohamedjama-farah_Art-Gallery-project-&metric=coverage)](https://sonarcloud.io/project/overview?id=mohamedjama-farah_Art-Gallery-project-)

## Build
mvn clean verify

## Run GUI
Start MongoDB: docker start mymongo
Run app: mvn exec:java -Dexec.mainClass=com.artgallery.ArtGalleryApp -DskipTests

Unit tests: src/test/java
Integration/E2E tests: src/it/java

Note: Docker must be running before mvn clean verify
