FROM bellsoft/liberica-openjdk-alpine:17
RUN mkdir -p /log && chmod 777 /log
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:MaxMetaspaceSize=128m", "-Xmx220m", "-XX:NewRatio=1", "-XX:SurvivorRatio=8", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=/log/heapdump.hprof", "-Xlog:gc*:file=/log/gc.log:time,uptime,level,tags:filecount=5,filesize=10M", "-jar", "/app.jar"]
