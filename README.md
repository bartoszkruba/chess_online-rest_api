# Chess Online - REST API
[![Build Status](https://travis-ci.org/bartoszkruba/chess_online-rest_api.svg?branch=master)](https://travis-ci.org/bartoszkruba/chess_online-rest_api) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.company%3Achess_online_rest_api&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.company%3Achess_online_rest_api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.company%3Achess_online_rest_api&metric=coverage)](https://sonarcloud.io/dashboard?id=com.company%3Achess_online_rest_api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.company%3Achess_online_rest_api&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.company%3Achess_online_rest_api)
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
$ git clone https://github.com/bartoszkruba/chess_online_docker-compose.git    
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

### Backend Production Configuration:

#### Running through Docker Swarm Stacks

Make sure you that you have enabled Docker Swarm mode and everything is configured properly (You can find more information about how to do it [here](https://docs.docker.com/engine/swarm/swarm-mode/))

#### etcb configuration

The DB stack is deployed using Galera Cluster docker image. This allows scaling up the MYSQL cluster out on more more than one node (or scaling back by removing nodes). The storage will be persisted across multiple nodes and if one of them goes down no data will be lost. 

Galera image requires  running etcd cluster installed on each of the Docker physical host.

How to configure etcd (CentOS 7):

1: Install etcd package:
```
yum install etcd
```

2: Edit configuration
```
$ vim /etc/etcd/etcd.conf
```

Should look like this:
```
ETCD_NAME={unique_node_name}
ETCD_DATA_DIR="/var/lib/etcd/default.etcd"
ETCD_LISTEN_PEER_URLS="http://0.0.0.0:2380"
ETCD_LISTEN_CLIENT_URLS="http://0.0.0.0:2379"
ETCD_INITIAL_ADVERTISE_PEER_URLS="{node_ip}:2380"
ETCD_INITIAL_CLUSTER={node_1_name}={node_1_ip}:2380,{node_2_name}={node_2_ip}:2380,{node_3_name}={node_3_ip}:2380"
ETCD_INITIAL_CLUSTER_STATE="new"
ETCD_INITIAL_CLUSTER_TOKEN="etcd-cluster-1"
ETCD_ADVERTISE_CLIENT_URLS="http://0.0.0.0:2379"
```
3: Start the service:
```
$ systemctl enable etcd
$ systemctl start etcd
```

4: Verify cluster status
```
$ etcdctl cluster-health
```

You can find more info about edcb and Galera Cluste configuration [here](https://severalnines.com/blog/mysql-docker-deploy-homogeneous-galera-cluster-etcd)

#### Deploying on Docker Swarm Stacks

Clone docker-compose repository from GitHub  
```
$ git clone https://github.com/bartoszkruba/chess_online_docker-compose.git    
```

Then cd into **prod** folder.  

Edit **docker-compose** file and change credentials for admin account, MySQL database and RabbitMQ. 
 
Default configuration is 2 backend services but you can change this setting to as many application replicas as you want. 

Add your nodes ip addresses to "DISCOVERY_SERVICE" environment variable inside mysql-galera configuration.  
  
Example:  
```
DISCOVERY_SERVICE: '192.168.55.111:2379,192.168.55.112:2379,192.168.55.207:2379'
```
Recommended setup for high availability is at least 3 Gale Cluster replicas
  
Deploy Docker stock by following command:
```
$ docker stack deploy -c docker-compose.yml {your_stack_name}
```


## Documentation:

### REST API:

Swagger documentation for REST API can be accessed through following endpoint:
```http request
http://localhost:8080/swagger-ui.html
```
### WebSockets:
Socket endpoint:
```
ws:YOUR_URL/ws
```

#### Subscribe endpoints: 

In order to receive information about game and chat messages subscribe to room channel:
```
/topic/room.{room_id}
```

#### Web Socket Authorization:
Include following headers to web socket connect request:
```
login {your_username}
```

```
password {your_password}
```

## Used Libraries:
[Chesslib](https://github.com/bhlangonijr/chesslib) - Java chess library for generating legal chess moves given a chessboard position, parsing a chess game stored in PGN or FEN format and many other things.
