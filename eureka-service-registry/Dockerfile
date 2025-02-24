FROM openjdk:17
WORKDIR /app
COPY target/eureka-service-registry-0.0.1-SNAPSHOT.jar eureka.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka.jar"]
