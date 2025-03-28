# Use an official Java runtime as a parent image
FROM eclipse-temurin:17-jdk  # Change to 11-jdk or 21-jdk if needed

# Set working directory inside the container
WORKDIR /app

# Copy all project files to the container
COPY . .

# Build the Java application using Maven
RUN ./mvnw clean package  # Use 'mvn clean package' if you are not using the Maven Wrapper

# Expose the port your application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/DigtalLibrary.jar"]
