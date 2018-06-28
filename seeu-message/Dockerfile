FROM openjdk:8-jre
COPY target/seeu-message-*.jar /usr/app/message.jar
WORKDIR /usr/app/
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "message.jar"]