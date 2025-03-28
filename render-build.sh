#!/bin/bash
echo "Installing Java..."
apt-get update && apt-get install -y openjdk-17-jdk  # Change to Java 11 or 21 if needed
java -version  # Verify installation
mvn clean package  # Build your Java app
