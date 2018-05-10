FROM openjdk:8-jre
COPY target/seeu-eureka-*.jar /usr/app/eureka.jar
WORKDIR /usr/app/
EXPOSE 8000
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "eureka.jar"]