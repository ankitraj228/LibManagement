# Build Stage
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies before adding source code
RUN mvn verify --fail-never

# Copy the entire project
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Run Stage - Use a lightweight Java image
FROM eclipse-temurin:17-jdk AS runtime

# Set working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/DigtalLibrary-1.0-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
