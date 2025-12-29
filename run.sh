#!/usr/bin/env sh
set -e

echo "Starting run.sh execution..."
echo "Current directory: $(pwd)"
echo "Java version:"
java -version

echo "Granting execution permissions to gradlew..."
chmod +x gradlew

echo "Running Gradle build..."
./gradlew clean build -x test --no-daemon --stacktrace

echo "Build complete. Searching for JAR..."

if [ ! -d "build/libs" ]; then
    echo "Error: build/libs directory does not exist. Build likely failed silently."
    exit 1
fi

JAR_FILE=$(find build/libs -name "*.jar" ! -name "*-plain.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: No executable JAR file found in build/libs"
    echo "Listing build directory contents:"
    ls -R build || echo "build directory not found (check failed earlier?)"
    exit 1
fi

echo "Starting application from $JAR_FILE..."

exec java -jar "$JAR_FILE"
