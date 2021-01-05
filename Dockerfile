#FROM maven:3.6.2-jdk-11
#COPY . /bot
#RUN mvn -f /bot/pom.xml package
#ENV ALCOBOT_TOKEN <TOKEN>
#RUN java -jar bot/target/alcostatsbot-1.0-SNAPSHOT-jar-with-dependencies.jar

FROM openjdk:11
COPY ./target /myapp
WORKDIR /myapp
ENTRYPOINT "bash"
#RUN java -jar alcostatsbot-1.0-SNAPSHOT-jar-with-dependencies.jar