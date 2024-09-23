FROM bellsoft/liberica-openjdk-alpine:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-XX:InitialHeapSize=268435456", "-XX:MaxHeapSize=268435456", "-XX:NewSize=134217728", "-XX:MaxNewSize=134217728", "-XX:NewRatio=1", "-XX:SurvivorRatio=8", "-Xlog:gc*:file=/gc.log:time,uptime,level,tags:filecount=5,filesize=10M", "-jar", "/app.jar"]
