# Use a base image with JDK and Maven
FROM maven:3.8.1-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Second stage - Use a lightweight JDK image to run the built application
FROM eclipse-temurin:21

# Set working directory
WORKDIR /opt

# Expose the application port
ENV PORT 8081
EXPOSE 8081 8080 8500 8300

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar /opt/madispayment.jar

# Run the application
ENTRYPOINT ["java", "-Xmx3076M", "-jar", "/opt/madispayment.jar"]