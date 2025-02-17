FROM alpine/java:21-jdk
LABEL authors=georgyorlov.com

COPY ./target/*-jar-with-dependencies.jar app.jar

#RUN mkdir -p /resources
VOLUME /resources

ENTRYPOINT ["java","-jar","/app.jar"]
