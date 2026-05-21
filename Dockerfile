# Этап 1: Сборка JAR-архива вместе со статикой
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Копируем pom.xml и исходники
COPY pom.xml .
COPY src ./src

# Запускаем сборку (тесты пропускаем, чтобы не тратить время хакатона)
RUN mvn clean package -DskipTests

# Этап 2: Запуск готового приложения
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Копируем собранный скомпилированный jar-файл из первого этапа
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
