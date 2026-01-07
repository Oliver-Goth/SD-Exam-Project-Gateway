# ===========================
# 1. Build Stage
# ===========================
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/delivery-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
