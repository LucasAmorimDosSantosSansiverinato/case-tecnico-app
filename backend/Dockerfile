FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY desafioTecnico-domain/pom.xml desafioTecnico-domain/
COPY desafioTecnico-application/pom.xml desafioTecnico-application/
COPY desafioTecnico-infra-data/pom.xml desafioTecnico-infra-data/
COPY desafioTecnico-infra-utils/pom.xml desafioTecnico-infra-utils/
COPY desafioTecnico-webui/pom.xml desafioTecnico-webui/
RUN mvn dependency:go-offline -q
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/desafioTecnico-webui/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
