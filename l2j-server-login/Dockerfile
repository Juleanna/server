# L2J Login Server
# Сборка и runtime в одном образе. Build-stage собирает fat-jar,
# runtime-stage берёт чистый JRE 21 и файлы config/data.
#
# Использование:
#   docker build -t l2jserver/login:latest .
#   docker run --rm -p 2106:2106 -p 9014:9014 -p 8080:8080 \
#              -v $(pwd)/login/config:/app/config:ro \
#              -v $(pwd)/login/data:/app/data \
#              -v $(pwd)/login/log:/app/log \
#              l2jserver/login:latest

FROM eclipse-temurin:21-jdk AS build
WORKDIR /build
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn ./.mvn
RUN ./mvnw -q -DskipTests -o dependency:go-offline || true
COPY src ./src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
# JRE для health-check; минимальный набор OS-утилит.
RUN apt-get update && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /build/target/l2jlogin.jar /app/l2jlogin.jar
COPY --from=build /build/target/libs /app/libs
# Config/data монтируются снаружи; при старте кладите сюда server.properties и т.п.
VOLUME ["/app/config", "/app/data", "/app/log"]
EXPOSE 2106 9014 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD curl -fsS http://127.0.0.1:8080/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/l2jlogin.jar"]
