FROM openjdk:8-jre
COPY target/seeu-messages-*.jar /usr/app/messages.jar
WORKDIR /usr/app/
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "messages.jar"]