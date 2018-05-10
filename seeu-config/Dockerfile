FROM openjdk:8-jre
COPY target/seeu-config-*.jar /usr/app/config.jar
WORKDIR /usr/app/
EXPOSE 8888
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "config.jar"]