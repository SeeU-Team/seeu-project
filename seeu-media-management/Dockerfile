FROM openjdk:8-jre
COPY target/seeu-media-management-*.jar /usr/app/media-management.jar
WORKDIR /usr/app/
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "media-management.jar"]
