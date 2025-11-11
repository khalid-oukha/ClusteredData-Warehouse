FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.war warehouse.war

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/warehouse.war"]
