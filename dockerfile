# ===== Stage 1: Build =====
FROM gradle:8-jdk21 AS build
WORKDIR /app

# Bağımlılık cache’i için önce sadece gradle dosyalarını kopyala
COPY settings.gradle build.gradle gradlew ./
COPY gradle ./gradle

# Wrapper’a izin ver
RUN chmod +x gradlew

# Dummy sync: bağımlılıkları indir (kaynak kodu yokken cache’lenir)
RUN ./gradlew --no-daemon build -x test || true

# Şimdi kaynak kodu kopyala ve gerçek build
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

# Üretilen jar path'ini bul
# (Spring Boot default: build/libs/*.jar)
# ===== Stage 2: Runtime =====
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Non-root kullanıcı (güvenlik)
RUN useradd -r -u 10001 appuser
USER appuser

# Jar'ı kopyala ve sabit bir ada ver
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Prod JVM ayarları (isteğe göre düzenle)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=25 -XX:+HeapDumpOnOutOfMemoryError -Duser.timezone=UTC"
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=5 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
