# 빌드 단계
FROM openjdk:17-jdk-slim AS build
WORKDIR /src
COPY ../../fifteen-logistics2/ai .
RUN ./gradlew build -x test

# 실행 단계
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]