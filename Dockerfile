FROM eclipse-temurin

WORKDIR /app

COPY /target/java-backend-exercise-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 443

CMD ["java", "-jar", "app.jar"]