# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test

# Run stage
FROM openjdk:17-slim
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Copy .env file
COPY .env .env

# Install gettext-base for envsubst
RUN apt-get update && apt-get install -y gettext-base

# Set environment variables from .env file
ENV $(cat .env | xargs)

# Expose port
EXPOSE 8080

# Run the application with environment variables
ENTRYPOINT ["java", "-jar", "app.jar"] 