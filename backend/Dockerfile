FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:11-jre
WORKDIR /app
RUN useradd -r -u 10001 appuser && mkdir /app/data && chown -R appuser:appuser /app
COPY --from=build /app/target/*.jar /app/app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
