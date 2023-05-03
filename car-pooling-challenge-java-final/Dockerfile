FROM openjdk:11

RUN apt-get update && apt-get install -y rabbitmq-server && rabbitmq-plugins enable rabbitmq_management

ENV RABBITMQ_DEFAULT_USER="guest"
ENV RABBITMQ_DEFAULT_PASS="guest"

# Exponer el puerto de RabbitMQ
EXPOSE 5672
EXPOSE 15672

# The port your service will listen on
EXPOSE 8080

# Copy the service JAR
COPY target/car-pooling-1.0.0-SNAPSHOT.jar /car-pooling.jar

# The command to run
CMD rabbitmq-server -detached; java -jar -Dspring.profiles.active=h2 car-pooling.jar
