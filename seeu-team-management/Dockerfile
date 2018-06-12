FROM openjdk:8-jre
COPY target/seeu-team-management-*.jar /usr/app/team-management.jar
WORKDIR /usr/app/
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "team-management.jar"]
