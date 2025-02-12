# Etapa 1: Usa uma imagem com Maven e JDK para build
FROM maven:3.9.9-amazoncorretto-17-al2023 AS build

# Define diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml e o código-fonte para o container
COPY pom.xml /app/
COPY src /app/src/

# Executa o comando Maven para fazer o build do projeto
RUN mvn clean install -DskipTests

# Etapa 2: Usa uma imagem menor apenas com JDK para rodar a aplicação
FROM openjdk:17-jdk-slim

# Define diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado pela primeira etapa para o container
COPY --from=build /app/target/votacao-api-0.0.1-SNAPSHOT.jar /app/votacao-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "votacao-api.jar", "--spring.profiles.active=prod"]