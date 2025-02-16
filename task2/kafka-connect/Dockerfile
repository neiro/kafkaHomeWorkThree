# =========================
# Stage 1: Сборка пользовательского коннектора
# =========================
# Используем официальный образ Maven с OpenJDK 11 для сборки проекта
FROM maven:3.8.6-openjdk-11 AS builder

# Устанавливаем рабочую директорию для сборки
WORKDIR /build

# Копируем исходные файлы проекта в контейнер
# Предполагается, что исходный код коннектора лежит в папке custom-connector
COPY custom-connector/ ./custom-connector/

# Переходим в директорию проекта и выполняем сборку fat jar (uber-jar)
WORKDIR /build/custom-connector
RUN mvn clean package

# =========================
# Stage 2: Сборка финального образа Kafka Connect
# =========================
# Используем официальный образ Kafka Connect от Confluent
FROM confluentinc/cp-kafka-connect:7.3.0

# Создаём папку для хранения пользовательских плагинов Kafka Connect
RUN mkdir -p /usr/share/java/kafka-connect-plugins

# Копируем стандартные плагины Kafka Connect из локальной директории plugins/
COPY plugins/ /usr/share/java/kafka-connect-plugins/

# Копируем собранный fat jar с пользовательским коннектором, 
# который был создан на этапе сборки (Stage 1)
COPY --from=builder /build/custom-connector/target/custom-connector-1.0.0.jar /usr/share/java/kafka-connect-plugins/

# Копируем JMX Exporter для мониторинга JVM и Kafka Connect
COPY plugins/jmx_prometheus_javaagent-0.15.0.jar /opt/

# Копируем конфигурационный файл JMX Exporter, если он необходим
COPY kafka-connect.yml /opt/

# Устанавливаем переменную окружения, указывающую Kafka Connect путь к плагинам
ENV CONNECT_PLUGIN_PATH="/usr/share/java,/usr/share/java/kafka-connect-plugins"

# (Опционально) Выводим содержимое папки с плагинами для проверки корректности копирования
RUN ls -l /usr/share/java/kafka-connect-plugins

# Копируем дополнительные конфигурационные файлы для Kafka Connect
# Например, connect-distributed.properties, JSON-конфигурацию коннектора и другие настройки
COPY ./config /etc/kafka-connect/
