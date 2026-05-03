# Usa uma imagem que já tem o Maven e o Java 21 instalados
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define a pasta de trabalho dentro do container
WORKDIR /app

# Copia todos os seus arquivos do PC para o container
COPY . .

# Manda o Maven construir o arquivo .jar (o executável)
RUN mvn clean package -DskipTests

# --- ETAPA 2: RODAR O PROJETO (RUN) ---
# Usa uma imagem Java bem leve (Alpine) apenas para rodar
FROM eclipse-temurin:21-jdk-alpine

# Define a pasta de trabalho
WORKDIR /app

# Pega o arquivo .jar que foi criado na Etapa 1 e joga pra cá
# IMPORTANTE: O nome 'saas-raiox-0.0.1-SNAPSHOT.jar' deve ser igual ao que está no seu pom.xml
# Se você mudou o nome no pom.xml, mude aqui também.
COPY --from=build /app/target/saas-raiox-0.0.1-SNAPSHOT.jar app.jar

# Pega a pasta 'public' (onde está o site) e joga pra cá
COPY --from=build /app/public /public

# Avisa ao Render que o site roda na porta 8080
EXPOSE 8080

# O comando final que liga o servidor
ENTRYPOINT ["java", "-jar", "app.jar"]