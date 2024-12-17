# 빌드 단계
FROM openjdk:17-jdk-slim AS build
WORKDIR /src
COPY ../../../../../../Downloads/fifteen-logistics/delivery .
RUN ./gradlew build -x test

# 실행 단계
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]