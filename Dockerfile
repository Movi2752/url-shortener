FROM gradle:8.10-jdk24 AS build
WORKDIR /src
COPY . .
RUN gradle bootJar --no-daemon -x test


FROM amazoncorretto:24
COPY build/libs/*.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]