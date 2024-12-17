# 빌드 단계
FROM openjdk:17-jdk-slim AS build
WORKDIR /src
COPY . .
RUN ./gradlew build -x test

# 실행 단계
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]