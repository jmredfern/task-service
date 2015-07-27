Task Service
======
**Task Service** is a basic RESTful Task Service. The service provides a REST API that allows callers to create, retrieve, update and delete tasks.

Some core use cases are:
* Create a task.
* Assign a task to a user.
* Complete a task.
* Fetch all tasks assigned to me.

## Download
* [Version 1.0.0](https://github.com/jmredfern/task-service/archive/master.zip)

## Requirements
* PostgreSQL 9.4

## Usage
The service can be deployed into an embedded Jetty container using the following command:

```$ mvn -Dmaven.skip.test=true -DprovisionDatabase=true -Dfile.encoding=UTF-8 jetty:run -e```

User id 1 & 2 will be pre-provisioned.

| HTTP operation | URL                                                       | Function                   |
| -------------- |:---------------------------------------------------------:| --------------------------:|
| **GET**        | http://localhost:8080/service/user/{userId}/task          | list all tasks for a user  |
| **GET**        | http://localhost:8080/service/user/{userId}/task/{taskId} | retrieve a task for a user |
| **POST**       | http://localhost:8080/service/user/{userId}/task          | create a task for a user   |
| **PUT**        | http://localhost:8080/service/user/{userId}/task          | update a task for a user   |
| **DELETE**     | http://localhost:8080/service/user/{userId}/task          | delete a task for a user   |

To clone the repo use:

```$ git clone https://github.com/jmredfern/task-service.git```

### Third party libraries
* see [NOTICE](https://github.com/jmredfern/task-service/blob/master/task-service/NOTICE) file

## License
* see [LICENSE](https://github.com/jmredfern/task-service/blob/master/task-service/LICENSE) file

## Version
* Version 1.0.0

## Contact
#### Developer
* e-mail: jmredfern@gmail.com
* Twitter: [@redfernjim](https://twitter.com/redfernjim "redfernjim on twitter")

[![Flattr this git repo](http://api.flattr.com/button/flattr-badge-large.png)](https://flattr.com/submit/auto?user_id=username&url=https://github.com/username/sw-name&title=sw-name&language=&tags=github&category=software) 



