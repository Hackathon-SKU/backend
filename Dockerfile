# --- Dockerfile : 이미지를 만드는 레시피 (설정값 파일) ---#


# --- Build stage (Debian 기반) ---
FROM gradle:8.8-jdk21 AS builder
WORKDIR /workspace

# 캐시 최적화: 먼저 Gradle 스크립트들만 복사→의존성만 미리 받기
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle
RUN gradle --version && gradle build -x test --no-daemon || true

# 실제 소스 복사 후 빌드
COPY . .
RUN ./gradlew clean bootJar -x test --no-daemon

# --- Run stage (JRE, Debian 기반) ---
FROM eclipse-temurin:21
WORKDIR /app
ENV TZ=Asia/Seoul
COPY --from=builder /workspace/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
