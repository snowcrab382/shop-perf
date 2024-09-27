FROM bellsoft/liberica-openjdk-alpine:17
RUN mkdir -p /log && chmod 777 /log
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:NewRatio=1", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/log/heapdump.hprof", "-Xlog:gc*:file=/log/gc.log:time,uptime,level,tags:filecount=5,filesize=10M", "-jar", "/app.jar"]
