FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/demo-spring-cloud-eureka-server-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8761
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]
