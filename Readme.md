# Digital Library Book Management System

## Requirements
- Java 8 or later

## Installation
1. Download `DigitalLibrary.jar`
2. Run with: `java -jar DigitalLibrary.jar`

## Alternative Installation (Windows)
1. Download `DigitalLibrarySetup.exe`
2. Run the installer
3. Launch from Start Menu or Desktop


## Building from Source
```bash
javac DigitalLibrary.java
jar cfe DigitalLibrary.jar DigitalLibrary *.class


## 6. Advanced Deployment (Optional)

### With Database Connectivity:
1. Add JDBC driver to your dependencies
2. Package database with your app or:
   - Use embedded database (SQLite, H2)
   - Provide setup instructions for MySQL/PostgreSQL

### As a Service:
1. Create a Windows Service using Apache Commons Daemon
2. Or Linux systemd service for server deployment
