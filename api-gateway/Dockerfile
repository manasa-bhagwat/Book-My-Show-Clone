FROM openjdk:17
WORKDIR /app
COPY target/api-gateway-0.0.1-SNAPSHOT.jar apigateway.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "apigateway.jar"]
