FROM openjdk:11

COPY ./build/libs/*.jar app.jar

ENV TZ=Asia/Seoul

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]