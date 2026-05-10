#!/usr/bin/env sh
##############################################################################
## Gradle start up script for UN*X
##############################################################################
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'
APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")
GRADLE_HOME="${GRADLE_USER_HOME:-${HOME}/.gradle}"

# Use the maximum available or specified JVM memory:
JAVACMD="${JAVA_HOME}/bin/java"
if [ -z "$JAVA_HOME" ]; then
  JAVACMD=java
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS -jar "$APP_BASE_NAME/../gradle/wrapper/gradle-wrapper.jar" "$@"
