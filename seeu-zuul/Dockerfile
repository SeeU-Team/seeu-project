FROM openjdk:9-jre
COPY target/seeu-zuul-*.jar /usr/app/zuul.jar
WORKDIR /usr/app/
EXPOSE 8001
CMD ["java", "-jar", "zuul.jar"]