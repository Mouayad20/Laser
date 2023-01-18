FROM openjdk
COPY assets/images/logo/*.png /assets/images/logo
COPY target/*.jar /
EXPOSE 8080
ENTRYPOINT  java -jar /laser-0.0.1-SNAPSHOT.jar
