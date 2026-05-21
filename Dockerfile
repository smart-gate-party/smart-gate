# ==========================================
# Этап 1: Сборка приложения (Build Stage)
# ==========================================
FROM maven:3.8.8-eclipse-temurin-21 AS builder

# Устанавливаем рабочую папку для сборки
WORKDIR /app

# Сначала копируем только файлы конфигурации Maven (для кэширования зависимостей)
COPY pom.xml .
COPY .mvn ./.mvn
COPY mvnw .
COPY mvnw.cmd .

# Загружаем зависимости (если pom.xml не менялся, этот шаг пропустится при повторной сборке)
RUN mvn dependency:go-offline -B

# Копируем исходный код проекта
COPY src ./src

# Собираем готовый .jar файл, пропуская тесты (чтобы сборка не падала из-за отсутствия БД)
RUN mvn clean package -DskipTests

# ==========================================
# Этап 2: Запуск приложения (Run Stage)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

# Настройка безопасности: запускаем приложение от не-root пользователя
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Копируем собранный jar-файл из предыдущего этапа сборщика
COPY --from=builder /app/target/*.jar app.jar

# Указываем порт, который обычно использует Spring Boot внутри контейнера
EXPOSE 8080

# Команда для безопасного запуска Java-приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
