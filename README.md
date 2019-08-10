# Chess Online - REST API
[![CircleCI](https://circleci.com/gh/bartoszkruba/chess_online-rest_api/tree/master.svg?style=svg)](https://circleci.com/gh/bartoszkruba/chess_online-rest_api/tree/master) 
[![Coverage Status](https://coveralls.io/repos/github/bartoszkruba/chess_online-backend_api/badge.svg?branch=master)](https://coveralls.io/github/bartoszkruba/chess_online-backend_api?branch=master)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

REST API server for chess online web app written in Java using Spring Boot and MYSQL for data storage. 

## Docker Image:

You can find docker image for this project on [DockerHub](https://cloud.docker.com/u/nawajo/repository/docker/nawajo/chess_rest_api) 

### Deploying Image to DockerHub: 
You can generate docker image for this application and deploy it to DockerHub through Fabric8 Maven plugin.

To change base image for project:  
cd into: **src/main/docker** and edit **Dockerfile**

Generate docker image through following command:
```
$ mvn clean package docker:build
```
For image deployment to DockerHub make sure that you edited your maven settings.xml file add added following properties: 

```xml
<servers>
    <server>
        <id>docker.io</id>
        <username>{dockerhub_username}</username>
        <password>{dockerhub_password}}</password>
    </server>
</servers>
```

Generate and deploy docker image through following command:

```
$ mvn clean package docker:build docker:push
```

## How To Run:

### Development Configuration:
Clone docker-compose repository from GitHub  
```
$ git pull https://github.com/bartoszkruba/chess_online_docker-compose.git    
```

Then cd into **dev** folder


Run docker-compose file though following command:

```
$ docker-compose up
```

By default server binds to port **8080** but you can change this by editing docker-compose file and adding following property: 
```
SERVER_PORT: {your_port} 
``` 

### Production Configuration:

Running through Docker Swarm Stacks:

Make sure you that you have enabled Docker Swarm mode and everything is configured properly (You can find more information about how to do it [here](https://docs.docker.com/engine/swarm/swarm-mode/))

Clone docker-compose repository from GitHub  
```
$ git pull https://github.com/bartoszkruba/chess_online_docker-compose.git    
```

Then cd into **prod** folder.  

Edit **docker-compose** file and change credentials for admin account, MySQL database and RabbitMQ. 
 
Default configuration is 2 services but you can change this setting to as many application replicas as you want. 
  
Deploy Docker stock by following command:
```
$ docker stock deploy -c docker-compose.yml {your_stack_name}
```

## Documentation:

### REST API:

Swagger documentation for REST API can be accessed through following endpoint:
```
http://localhost:8080/swagger-ui.html
```
### WebSockets:
Socket messages are broadcasted through STOMP protocol

Application socket endpoint:
```
ws://localhost:{your_port}/ws
```
Subscribing to room (chat messages & game info):
```
/topic/room.{room_id}
```

## Used Libraries:
[Chesslib](https://github.com/bhlangonijr/chesslib) - Java chess library for generating legal chess moves given a chessboard position, parsing a chess game stored in PGN or FEN format and many other things.
