# Zero to Hero

Spring Boot application : org.camunda.educationalEducationalTrail

## Groovy script
Groovy script is not part of the engine by default.

Add this dependency
`````

<dependency>
<groupId>org.codehaus.groovy</groupId>
<artifactId>groovy-all</artifactId>
<version>2.4.5</version>
</dependency>
`````
or groovy-all-2.4.5.jar

Add this in the application.properties
`````
spring.groovy.template.enabled=false
spring.groovy.template.check-template-location=false
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.groovy.template.GroovyTemplateAutoConfiguration
`````

# Hero to Super Hero

# Test the Best

# Fun with Flags
## Docker

Documentation are under d:\camunda\docker too.

https://docs.camunda.org/manual/7.16/installation/docker/
Community:

````
docker pull camunda/camunda-bpm-platform:latest
````

Subscription:
````
$ docker login registry.camunda.cloud
Login Okta / password Okta
$ docker pull registry.camunda.cloud/cambpm-ee/camunda-bpm-platform-ee:7.16.0
````

docker run -d --name db -p 5433:5432  -e POSTGRES_USER=camunda -e POSTGRES_PASSWORD=camunda postgres:9.6

docker run -d --name camunda -p 8080:8080  -e DB_DRIVER=org.postgresql.Driver -e DB_URL=jdbc:postgresql://localhost:5433/camunda -e DB_USERNAME=camunda -e DB_PASSWORD=camunda registry.camunda.cloud/cambpm-ee/camunda-bpm-platform-ee:7.16.0

Start just docker postgres

Via Docker compose : see file docker-compose.yml


db:
image: postgres:9.6
environment:
- POSTGRES_USER=camunda
- POSTGRES_PASSWORD=camunda
expose:
- "5433"
ports:
- "5433:5432"

Tips are:
  	
* You don’t need to inject the license. At the first execution, access Cockpit. Then, Camunda Cockpit asks you to give the key via the browser: then it saves it in the database.
* The image (in the docker-compose) for Camunda must be the exact number, not latest
* You must change the Postgres port, if you have a local Postgres on your machine. If you don’t do that, Docker works because it creates a local 5432, and Camunda[Docker] accesses it. But then it’s not possible to access it from the outside (you access the local 5432). Exposing a 5433, which is not used, allow the machine to access it too (especially the Intellij datasource plugin)

 
