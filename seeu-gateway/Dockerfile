FROM openjdk:8-jre
COPY target/seeu-gateway-*.jar /usr/app/gateway.jar
WORKDIR /usr/app/
EXPOSE 8001
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "gateway.jar"]