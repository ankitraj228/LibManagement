# Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk AS build

# Set working directory inside the container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Build the Java application using Maven
RUN ./mvnw clean package  # If you are not using the Maven Wrapper, use 'mvn clean package'

# Expose the port your application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/DigtalLibrary.jar"]
