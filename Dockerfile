FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Camada otimizada para cache de dependências (se usar Gradle)
COPY build.gradle .
COPY gradlew .
COPY gradle/ gradle/
RUN ./gradlew dependencies || true  # Pré-baixa dependências

# Copia o código e constrói
COPY src ./src
COPY build/libs/*.jar app.jar

# Configurações de segurança
RUN adduser --system --group spring
USER spring:spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]