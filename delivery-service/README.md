
```
delivery-service
├─ .env.delivery.example
├─ .mvn
│  └─ wrapper
│     └─ maven-wrapper.properties
├─ docker-compose.delivery.yml
├─ Dockerfile
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ mtogo
   │  │        └─ delivery
   │  │           ├─ application
   │  │           │  └─ service
   │  │           │     ├─ AssignDeliveryService.java
   │  │           │     ├─ CreateDeliveryService.java
   │  │           │     ├─ OrderEventConsumerService.java
   │  │           │     └─ UpdateDeliveryStatusService.java
   │  │           ├─ DeliveryServiceApplication.java
   │  │           ├─ domain
   │  │           │  ├─ model
   │  │           │  │  ├─ DeliveryStatus.java
   │  │           │  │  ├─ DeliveryTask.java
   │  │           │  │  ├─ Location.java
   │  │           │  │  └─ TrackingInfo.java
   │  │           │  └─ port
   │  │           │     ├─ in
   │  │           │     │  ├─ CreateDeliveryUseCase.java
   │  │           │     │  ├─ OrderEventConsumerPort.java
   │  │           │     │  └─ UpdateDeliveryStatusUseCase.java
   │  │           │     └─ out
   │  │           │        ├─ DeliveryEventPublisherPort.java
   │  │           │        └─ DeliveryTaskRepositoryPort.java
   │  │           └─ infrastructure
   │  │              ├─ adapter
   │  │              │  ├─ in
   │  │              │  │  └─ messaging
   │  │              │  │     ├─ OrderPreparedEventDTO.java
   │  │              │  │     └─ RabbitMQOrderEventListener.java
   │  │              │  └─ out
   │  │              │     ├─ messaging
   │  │              │     │  └─ RabbitMQDeliveryEventPublisherAdapter.java
   │  │              │     └─ persistence
   │  │              │        ├─ DeliveryTaskJpaEntity.java
   │  │              │        ├─ DeliveryTaskJpaRepository.java
   │  │              │        ├─ DeliveryTaskMapper.java
   │  │              │        └─ DeliveryTaskRepositoryAdapter.java
   │  │              ├─ api
   │  │              │  ├─ AssignAgentRequest.java
   │  │              │  ├─ DeliveryTaskController.java
   │  │              │  ├─ DeliveryTaskRequest.java
   │  │              │  ├─ DeliveryTaskResponse.java
   │  │              │  └─ LocationDto.java
   │  │              └─ config
   │  │                 ├─ DeliveryConfig.java
   │  │                 ├─ OpenApiConfig.java
   │  │                 └─ RabbitMQConfig.java
   │  └─ resources
   │     ├─ application.properties
   │     └─ application.yml
   └─ test
      └─ java
         └─ com
            └─ mtogo
               └─ delivery
                  ├─ application
                  │  └─ DeliveryServiceUnitTest.java
                  ├─ controller
                  │  └─ DeliveryControllerTest.java
                  ├─ DeliveryServiceApplicationTests.java
                  ├─ domain
                  │  └─ DeliveryDomainTest.java
                  └─ persistence
                     ├─ DeliveryRepositoryH2Test.java
                     └─ DeliveryRepositoryMySQLContainerTest.java

```