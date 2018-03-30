FROM openjdk:8-jre
COPY target/seeu-zuul-*.jar /usr/app/zuul.jar
WORKDIR /usr/app/
EXPOSE 8001
CMD ["java", "-jar", "zuul.jar"]