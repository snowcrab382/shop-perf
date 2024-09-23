FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:NewRatio=1", "-Xlog:gc*:file=/gc.log:time,uptime,level,tags:filecount=5,filesize=10M", "-jar", "/app.jar"]
