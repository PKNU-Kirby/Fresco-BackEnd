# Stage 1: Build
FROM openjdk:17-jdk-slim AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 설정 파일 복사
COPY gradle /app/gradle
COPY gradlew /app/gradlew
COPY build.gradle /app/
COPY settings.gradle /app/

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성만 다운로드 (빌드하지 않음)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src /app/src

# 애플리케이션 빌드
RUN ./gradlew clean bootJar -x test --no-daemon

# Stage 2: Run
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과물 복사 (bootJar로 생성된 실행 가능한 JAR)
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]