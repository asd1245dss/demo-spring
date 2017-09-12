# Use an official Java runtime as a parent image
FROM frolvlad/alpine-oraclejdk8

# Author of current Dockerfile
MAINTAINER Lee <1163525902@qq.com>

# Run app.class when the container launches
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS App" ]

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
ADD . /app

# Define environment varaible
ENV JAVA_OPTS=""
