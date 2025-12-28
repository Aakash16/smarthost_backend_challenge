#!/usr/bin/env sh

chmod +x gradlew

./gradlew clean build -x test --no-daemon

JAR_FILE=$(find build/libs -name "*.jar" ! -name "*-plain.jar" | head -n 1)

if [ -z "$JAR_FILE" ]; then
    echo "Error: No executable JAR file found in build/libs"
    exit 1
fi

echo "Starting application from $JAR_FILE..."

exec java -jar "$JAR_FILE"
