FROM openjdk:9-jre
COPY target/seeu-user-management-*.jar /usr/app/user-management.jar
WORKDIR /usr/app/
EXPOSE 8080
EXPOSE 8081
CMD ["java", "-jar", "user-management.jar"]
