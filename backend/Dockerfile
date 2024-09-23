FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Xlog:gc*:file=/gc.log:time,uptime,level,tags:filecount=5,filesize=10M", "-jar", "/app.jar"]
