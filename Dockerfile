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

# Copy the .env file to the container
COPY .env .env

# Install gettext-base for envsubst (for env variable substitution if needed)
RUN apt-get update && apt-get install -y gettext-base

# Expose port
EXPOSE 8080

# Run the application with environment variables, using envsubst to replace variables in the .env file
ENTRYPOINT ["sh", "-c", java -jar app.jar"]

