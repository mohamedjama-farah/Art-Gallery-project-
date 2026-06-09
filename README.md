# Art Gallery Management System

A Java desktop application for managing art galleries with Test-Driven Development (TDD) and comprehensive testing.

## Build Status

[![Build and Quality Analysis](https://github.com/mohamedjama-farah/Art-Gallery-project/workflows/Build%20and%20Quality%20Analysis/badge.svg)](https://github.com/mohamedjama-farah/Art-Gallery-project/actions)
[![Coverage Status](https://coveralls.io/repos/github/mohamedjama-farah/Art-Gallery-project/badge.svg)](https://coveralls.io/github/mohamedjama-farah/Art-Gallery-project)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mohamedjama-farah_art-gallery&metric=alert_status)](https://sonarcloud.io/dashboard?id=mohamedjama-farah_art-gallery)

## Project Overview

- **Language**: Java 17
- **Build Tool**: Maven
- **Testing Framework**: JUnit 5 (Jupiter)
- **Database**: MongoDB
- **GUI**: Swing
- **Code Quality**: SonarCloud, JaCoCo, PIT
- **CI/CD**: GitHub Actions

## Test Results

- ✅ **39 Unit Tests** - All Passing
- ✅ **21 Integration Tests** - All Passing
- ✅ **60 Total Tests** - 100% Pass Rate
- ✅ **Mutation Testing** - 94% Test Strength

## Quick Start

### Requirements
- Java 17+
- Maven 3.6+
- Docker (for MongoDB in tests)

### Build
```bash
mvn clean compile
```

### Unit Tests Only
```bash
mvn clean test
```

### All Tests (Unit + Integration)
```bash
mvn clean verify
```

### Generate Coverage Report
```bash
mvn clean verify
# Open target/site/jacoco/index.html in browser
```

### Mutation Testing
```bash
mvn clean verify
mvn org.pitest:pitest-maven:mutationCoverage
# View report in target/pit-reports/
```

## Architecture

### Model Layer (`com.artgallery.model`)
- `Artwork` - Domain model with validation
- `Category` - Domain model with validation

### Repository Layer (`com.artgallery.repository`)
- `ArtworkRepository` - Repository interface
- `MongoArtworkRepository` - MongoDB implementation
- `CategoryRepository` - Repository interface
- `MongoCategoryRepository` - MongoDB implementation

### Controller Layer (`com.artgallery.controller`)
- `ArtworkController` - Business logic for artworks
- `CategoryController` - Business logic for categories

### View Layer (`com.artgallery.view.swing`)
- `ArtGalleryFrame` - Swing GUI
- `ArtGalleryApp` - Application entry point

## Testing Strategy

### Testing Pyramid
1. **Unit Tests (39)** - Fast, isolated tests with mocks
2. **Integration Tests (21)** - Real database, Testcontainers
3. **E2E Tests (7)** - Complete workflows

### Test Coverage
- JaCoCo: Code coverage reporting
- PIT: Mutation testing (94% test strength)
- Coveralls: Coverage tracking

## Continuous Integration

GitHub Actions automatically:
- Runs all unit and integration tests
- Generates code coverage reports
- Uploads coverage to Coveralls
- Scans code quality with SonarCloud
- On every push and pull request

## Code Quality

- **SonarCloud**: https://sonarcloud.io/dashboard?id=mohamedjama-farah_art-gallery
- **Coveralls**: https://coveralls.io/github/mohamedjama-farah/Art-Gallery-project

## Author

Mahamed Jama Farah

## License

University of Florence - AST Exam Project
✅ SonarCloud and Coveralls integration configured
