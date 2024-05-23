FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
ADD build/libs/restaurant-delivery.jar /app/restaurant-delivery.jar
ENTRYPOINT ["java", "-jar", "/app/restaurant-delivery.jar"]
