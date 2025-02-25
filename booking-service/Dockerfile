FROM openjdk:17-alpine
COPY target/booking-service-0.0.1-SNAPSHOT.jar bookingservice.jar
ENTRYPOINT ["java", "-jar", "/bookingservice.jar", "--spring.config.location=classpath:/BOOT-INF/classes/application.properties"]