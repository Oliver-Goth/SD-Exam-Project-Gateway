FROM eclipse-temurin:21-jre
WORKDIR /app
# Expect a pre-built jar at target/fulfillment-service-0.0.1-SNAPSHOT.jar
COPY target/fulfillment-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]
