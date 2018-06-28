FROM openjdk:8-jre
COPY target/seeu-message-management-*.jar /usr/app/message-management.jar
WORKDIR /usr/app/
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "message-management.jar"]