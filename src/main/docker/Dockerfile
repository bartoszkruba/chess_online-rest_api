FROM openjdk:11

VOLUME /tmp

ADD maven/chess_online_rest_api-${project.version}.jar chess_rest_api.jar

RUN sh -c 'touch /chess_rest_api.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/chess_rest_api.jar"]