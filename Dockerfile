
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/app/build/libs/app.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]