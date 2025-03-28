# Use an official Java runtime as a parent image
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Build the Java application using Maven
RUN mvn clean package -DskipTests

# Expose the port your application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/DigtalLibrary.jar"]
