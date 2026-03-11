FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8081

ENV SPRING_PROFILES_ACTIVE=azure

ENTRYPOINT ["java","-jar","app.jar"]