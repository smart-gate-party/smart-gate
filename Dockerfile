# 1. Этап сборки (Скачиваем Maven и собираем проект)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Этап запуска (Берем только готовый файл приложения, чтобы сервер весил меньше)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Открываем порт наружу
EXPOSE 8080

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]