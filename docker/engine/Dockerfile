# Multi-stage Dockerfile for STRATA Java Vector Engine
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /workspace
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle/ gradle/
COPY proto/ proto/
COPY strata-core/ strata-core/
COPY strata-storage/ strata-storage/
COPY strata-query/ strata-query/
COPY strata-engine/ strata-engine/
COPY strata-cli/ strata-cli/

RUN chmod +x ./gradlew && ./gradlew :strata-engine:installDist --no-daemon

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app
COPY --from=builder /workspace/strata-engine/build/install/strata-engine /app

ENV STRATA_ENGINE_PORT=50051
ENV STRATA_ENGINE_DATA_DIR=/var/lib/strata

EXPOSE 50051

VOLUME ["/var/lib/strata"]

ENTRYPOINT ["/app/bin/strata-engine"]
