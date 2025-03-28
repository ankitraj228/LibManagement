# Use an official Maven and Java image
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy only `pom.xml` first to leverage Docker cache
COPY pom.xml .

# Download dependencies before copying source code
RUN mvn dependency:go-offline

# Copy the entire project
COPY . .

# Ensure Maven builds the project
RUN mvn clean package -DskipTests

# Expose the port your application runs on
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/DigtalLibrary.jar"]
