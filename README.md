# Chess Online - REST API
[![CircleCI](https://circleci.com/gh/bartoszkruba/chess_online-rest_api/tree/master.svg?style=svg)](https://circleci.com/gh/bartoszkruba/chess_online-rest_api/tree/master) 
[![Coverage Status](https://coveralls.io/repos/github/bartoszkruba/chess_online-backend_api/badge.svg?branch=master)](https://coveralls.io/github/bartoszkruba/chess_online-backend_api?branch=master)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

REST API server for playing chess online written in Java using Spring Boot and MYSQL for data storage.

### Docker Image:

You can find docker image for this project on [DockerHub](https://cloud.docker.com/u/nawajo/repository/docker/nawajo/chess_rest_api)  
  
### How To Run:
  
Running development stand-alone setup through docker-compose:  
```
$ git pull https://github.com/bartoszkruba/chess_online_docker-compose.git  
$ cd dev  
$ docker-compose up
```

### Documentation:
Swagger documentation for REST API can be accessed through following endpoint:

```
http://localhost:8080/swagger-ui.html
```

###Used Libraries:
[Chesslib](https://github.com/bhlangonijr/chesslib) - Java chess library for generating legal chess moves given a chessboard position, parsing a chess game stored in PGN or FEN format and many other things.
