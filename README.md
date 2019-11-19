# MicroservicesEurekaWorkspace
Microservices Eureka service Registartion and Discovery on PCF cloud platform:

I have 3 projects created:

1. discovery-server : This acts as a Eureka server and uses the user defined service from the PCF 

2. movie-info-service: This service will get the movie related Info =>https://movieinfoservice-101.cfapps.io/movies/111
    o/p:
    <Movie>
    <movieId>111</movieId>
    <name>Movie111</name>
    </Movie>

3. movie-catalog-service: This service will retun the ratings and fetch the movie info from movie-info-service => http://moviecatalogservice-101.cfapps.io/catalog/ss
    o/p:
    <List>
    <item>
    <name>Movie111</name>
    <desc>Movie Desc</desc>
    <rating>4</rating>
    </item>
    <item>
    <name>Movie222</name>
    <desc>Movie Desc</desc>
    <rating>5</rating>
    </item>
    </List>

The projects are created as Maven projects from start.spring.io. Server being created as Eureka Server and the clients being created using the dependecy Eureka Discovery 
After updateing the main class and updating application.properties, manifest.yml files build the Maven jars for respective projects.
Deploy the projects to PCF using below commands:

cf push eureka-101 -f C:\..\MicroservicesEureka\discovery-server\manifest.yml -m 650M
cf push movieInfoService-101 -f C:\..\MicroservicesEureka\movie-info-service\manifest.yml -m 650M
cf push movieCatalogService-101 -f C:\..\MicroservicesEureka\movie-catalog-service\manifest.yml -m 650M

> First deploy the Server
> Deploy clients
> create the new user defined service in PCF console using the below command
  cf create-user-provided-service eureka-service -p '{"uri":"https://eureka-101.cfapps.io"}'
  Or you can create it using the PCF console interface.
> Bind the applications deployed to the Service created
> Cross verify the Env Variable properties for the application through Service 
    Org->Space->Service->eurekaName->movieInfoService-101-settings->view Env vars:
          {
          "staging_env_json": {},
          "running_env_json": {},
          "system_env_json": {
            "VCAP_SERVICES": {
              "user-provided": [
                {
                  "label": "user-provided",
                  "name": "eurekaName",
                  "tags": [],
                  "instance_name": "eurekaName",
                  "binding_name": null,
                  "credentials": {
                    "url": "http://eureka-101.cfapps.io"
                  },
                  "syslog_drain_url": "",
                  "volume_mounts": []
                }
              ]
            }
          },
          "application_env_json": {
            "VCAP_APPLICATION": {
              "cf_api": "https://donotuseapi.run.pivotal.io",
              "limits": {
                "fds": 16384
              },
              "application_name": "movieInfoService-101",
              "application_uris": [
                "movieinfoservice-101.cfapps.io"
              ],
              "name": "movieInfoService-101",
              "space_name": "develop",
              "space_id": "a54559b2-7819-4685-bb74-a0e113fb3916",
              "organization_id": "fa5f1e48-2e84-4dc6-8e29-2bf38a97f1f7",
              "organization_name": "OrgSam",
              "uris": [
                "movieinfoservice-101.cfapps.io"
              ],
              "users": null,
              "application_id": "4b308136-047a-48e8-adc4-b654db7fdf8d"
            }
          }
        }

Org->Space->Service->eurekaName->movieCatalogService-101-settings->view Env vars:
        {
          "staging_env_json": {},
          "running_env_json": {},
          "system_env_json": {
            "VCAP_SERVICES": {
              "user-provided": [
                {
                  "label": "user-provided",
                  "name": "eurekaName",
                  "tags": [],
                  "instance_name": "eurekaName",
                  "binding_name": null,
                  "credentials": {
                    "url": "http://eureka-101.cfapps.io"
                  },
                  "syslog_drain_url": "",
                  "volume_mounts": []
                }
              ]
            }
          },
          "application_env_json": {
            "VCAP_APPLICATION": {
              "cf_api": "https://donotuseapi.run.pivotal.io",
              "limits": {
                "fds": 16384
              },
              "application_name": "movieCatalogService-101",
              "application_uris": [
                "moviecatalogservice-101.cfapps.io"
              ],
              "name": "movieCatalogService-101",
              "space_name": "develop",
              "space_id": "a54559b2-7819-4685-bb74-a0e113fb3916",
              "organization_id": "fa5f1e48-2e84-4dc6-8e29-2bf38a97f1f7",
              "organization_name": "OrgSam",
              "uris": [
                "moviecatalogservice-101.cfapps.io"
              ],
              "users": null,
              "application_id": "2b40379f-a5bd-46cf-8bd2-a200af61a673"
            }
          }
        }


> Corresponding application.properties entries will be as below:
spring.application.name=movieCatalogService-101
server.port=8082
spring.profiles=cloud
eureka.instance.nonSecurePort=80
eureka.instance.hostname=${vcap.application.uris[0]}
eureka.client.service-url.defaultZone=${vcap.services.eurekaName.credentials.url}/eureka
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true

> Bind these applications to the new service with below command or through interface:

    >cf bind-service movieInfoService-101 eurekaName
    >cf restage movieInfoService-101
    >cf bind-service movieCatalogService-101 eurekaName
    >cf restage movieCatalogService-101
    
       
References:https://www.novatec-gmbh.de/en/blog/service-discovery-eureka-cloud-foundry/







