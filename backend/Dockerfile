FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:NewRatio=1", "-jar", "/app.jar"]
