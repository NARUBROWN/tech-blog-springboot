FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m"
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar app.jar"]