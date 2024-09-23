FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-verbose:gc", "-Xloggc:/gc-logs/gc.log", "-XX:+PrintGCDetails", "-XX:+PrintGCDateStamps", "-XX:+PrintGCTimeStamps", "-XX:+PrintHeapAtGC", "-jar", "/app.jar"]
