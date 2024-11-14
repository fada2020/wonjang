FROM openjdk:21

EXPOSE 8030
ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app.jar"]

