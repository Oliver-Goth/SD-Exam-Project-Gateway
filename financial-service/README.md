# Financial Microservice

## Hexagonial arctecture desiggn


https://medium.com/@ignatovich.dm/understanding-software-architecture-ddd-clean-architecture-and-hexagonal-architecture-13758e59c951


ORM FRAMEWORK : JPA 

⭐ What is domain.port.in?

domain.port.in = Application-facing interfaces
These define what the application can do — the use cases of your microservice.

Think of it as your API for the outside world, but clean and technology-free.

Inside this folder you put:

✔ Command classes (input DTOs)

What the client sends to perform an action.

✔ Query classes (if needed)

For getting data.

✔ Use-case interfaces

Describing business operations like createPayment().



# Model : Port : IN (Interfaces that defines what the application has to offer/can do/functionality)

-- CreatePaymentUseCase 
-- PaymentStatusUseCase :  Payment confirmPayment(Long paymentId, String providerStatus);
-- GetPaymentUseCase :  Payment getPayment(Long paymentId);
-- AllPaymentsForRestaurantUseCase : if a specific restaurant needs a record of their payments
-- AllPaymentsUseCase : just get all payments (maybe this is too big?)
-- RefundPaymentUseCase : Common too have in a payment system, but might not be something we have time for 


In domain.port.out, you define what the domain needs from the outside world, not what your service offers (that’s port.in).

Think of port.out as the domain’s dependencies.
The domain doesn’t know how something is done, only what it needs done.

Exactly — you've got it.
In a clean architecture / hexagonal architecture / DDD setup, your application service (also called use case service) is responsible for orchestration, not implementing logic from other layers.


What is RabbitMQ / a messaging system?

A messaging system is software that allows applications (or services) to communicate asynchronously via messages.

Instead of directly calling another service or writing to a database, you send a message to a queue.

Another service can consume messages from that queue whenever it’s ready.

The sender and receiver are decoupled — they don’t have to run at the same time.

RabbitMQ is one such message broker. Others are Kafka, ActiveMQ, SQS, etc.

In RabbitMQ we need an Event class, Event producer and Optionally an Event Consumer

https://nirajtechi.medium.com/event-driven-programming-using-spring-boot-kafka-rabbitmq-d68bbccb2872

## ENDPOINTS:

* 


What “your solution” signals to someone who looks at your C4 diagrams

If you show your C4 model to someone, and say:

“We have independent microservices”

“They communicate via RabbitMQ (events)”

“We optionally have an API gateway for external clients”

=> That usually signals microservices + EDA.

Why RabbitMQ → Event‑Driven

In EDA, services communicate by sending events (e.g. “OrderCreated”, “PaymentProcessed”) instead of doing direct synchronous API calls. 
javaguides.net
+2
Springfuse
+2

RabbitMQ (or similar brokers) acts as the central event bus / broker: producers publish events to it, and any number of consumers can subscribe to and react to those events. 
Moments Log
+2
blog.ludmal.com
+2

This decoupling means that services don’t need to know about each other; they just know how to produce or consume events — that’s a core principle of event‑driven microservices. 
blog.theodo.com
+2
DEV Community
+2

So your use of RabbitMQ strongly suggests — and supports — that your system is built around an event‑driven architecture.


✅ Pros of using RabbitMQ / message‑driven (event‑driven) vs direct API calls
• Loose coupling / independence between services

With a broker, services don’t call each other directly. Instead, one service publishes an event/message (e.g. “OrderCreated”) and other services subscribe/respond when relevant. This decoupling means: services can evolve independently; you can change implementation or even language of one service without breaking others. 
The New Stack
+2
akava.io
+2

It reduces tight dependencies and the risk that a change in one service cascades into others. 
Confluent
+1

• Resilience & fault‑tolerance

If a consumer service is down (temporary outage, restart, deployment etc.), messages published by other services can sit in the queue and wait. Once the service recovers, it can consume pending messages. This avoids data loss and reduces risk of cascading failures, compared to synchronous calls that would fail immediately. 
Confluent
+1

Brokers often support durable queues, retries, dead‑letter queues, helping with reliability. 
DEV Community
+1

• Scalability and load smoothing / traffic spikes handling

Because messages are queued asynchronously, the system can absorb bursts of incoming load (e.g. many orders at once) — the producer can publish quickly, and consumers can process at their own pace. This helps avoid overloading downstream services when traffic spikes. 
GeeksforGeeks
+2
Index.dev
+2

You can scale consumers horizontally (multiple instances) to handle load without changing the producers or external API. 
akava.io
+2
Index.dev
+2

• Asynchronous / decoupled workflows & better user experience

With sync API calls, the user often must wait for a chain of requests to complete (e.g. order → payment → confirmation). With event‑driven, you can respond to client immediately (e.g. “order accepted”), and background services continue processing payment / inventory / shipping asynchronously — improving responsiveness and perceived latency. 
Confluent
+1

This is useful especially when some tasks are slow or may fail — you can retry, queue, or delay without blocking the user request. 
DZone
+1

• Easier to extend / add new features / new consumers

Because services subscribe to events, you can add a new service that listens for existing events (e.g. order created, payment completed) without modifying existing producers. That’s great for adding analytics, notifications, audit‑logs, reporting, etc., without touching core business logic. 
The New Stack
+1

This supports long‑term maintainability, flexibility and evolution of the system design.

🔹 Hvad er en “gateway”

En API Gateway er en komponent i en mikroservice‑arkitektur som fungerer som én samlet indgang for klienter (web‑app, mobil‑app, frontend osv.) der ønsker at kalde backend‑tjenester. 
GeeksforGeeks
+2
Design Gurus
+2

Typiske opgaver for en gateway:

Rute (proxy) indkommende HTTP‑/API‑anmodninger til de rette mikroservices. 
GeeksforGeeks
+1

Håndtere tværgående bekymringer (cross‑cutting concerns) som autentificering, autorisation, rate‑limiting, logning, overvågning osv. 
Design Gurus
+1

Samle eller “aggregere” data fra flere mikroservices til én samlet respons, så klienten slipper for at lave flere kald. 
microservices.io
+1

Skjule det interne mikroservice‑landskab for klienter — de behøver ikke vide hvor hver service kører eller hvordan backend er organiseret. 
GeeksforGeeks
+1

Fordele ved at bruge Gateway:

Forenkler klient‑siden (et enkelt API‑endpoint i stedet for mange).

Centraliserer opsætning af sikkerhed, kontrol og formatering.

Gør det nemmere at versionere, autentificere, logge og monitorere på tværs af alle API‑kald.

🔹 Hvad er “event bus / event‑driven” (event‑bus driven)

Når man siger “event bus–driven” eller “event‑driven” refererer det typisk til en arkitektur hvor mikroservices kommunikerer asynkront via events/meldinger i stedet for synkrone API‑kald. Dette kaldes ofte Event-Driven Architecture (EDA). 
GeeksforGeeks
+2
evidi.com
+2

En “event bus” eller “message broker” (såsom RabbitMQ, Apache Kafka etc.) er “mellemmanden” som formidler events mellem tjenester. 
feanote.com
+2
Medium
+2

Når én mikroservice gør noget vigtigt (f.eks. opretter en ordre), publicerer den et event — fx “OrderCreated”. Andre mikroservices kan abonnere på dette event og reagere (f.eks. oprette betaling, sende e-mail, lageropdatering etc.). 
GeeksforGeeks
+2
Medium
+2

Kommunikationen er asynkron — afsender behøver ikke vente på at forbrugeren er færdig; beskeden køes og håndteres når forbrugeren er klar. 
evidi.com
+1

Fordele ved event‑driven / event bus‑driven:

Løs kobling mellem services — services behøver ikke kende hinandens steder eller API‑kontrakter, bare event‑kontrakter.

Bedre tolerance over for fejl og nedetid — hvis en service midlertidigt er nede, bliver beskeder bare køet indtil den er oppe igen.

Let at tilføje nye services der reagerer på events uden at ændre eksisterende produkter (du kan f.eks. tilføje logning, analytics, notifikationer etc. senere).

Asynkron behandling — god til processer der ikke behøver være synkrone, og hvor responstid for klienten ikke nødvendigvis behøver at være øjeblikkelig.


[Producer] --> [Exchange] --> [Queue 1] --> [Consumer]
                               --> [Queue 2] --> [Another Consumer]
