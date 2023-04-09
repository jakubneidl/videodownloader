# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-slim

# Install necessary dependencies and FFmpeg
RUN apt-get update && \
    apt-get install -y --no-install-recommends ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the project's files into the container
COPY . .

# Build the Java application
RUN ./mvnw clean package

# Expose the service's port (Replace 8080 with the actual port your service uses)
EXPOSE 8080

# Start the Java service
CMD ["java", "-jar", "target/youtubeDownloader-0.0.1-SNAPSHOT.jar"]