FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN dos2unix mvnw || sed -i 's/\r$//' mvnw || true
RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/fulfillment-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","app.jar"]
