FROM frolvlad/alpine-oraclejdk8
MAINTAINER Lee <1163525902@qq.com>
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

VOLUME /tmp

ADD demo-spring-boot-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""
