FROM confluentinc/cp-kafka-connect:latest

RUN confluent-hub install --no-prompt confluentinc/kafka-connect-jdbc:latest

# Создаём папку для плагинов
RUN mkdir -p /usr/share/java/kafka-connect-plugins

# Копируем плагины Kafka Connect
COPY plugins/ /usr/share/java/kafka-connect-plugins/

# Скопировать JMX Exporter в Docker образ
COPY plugins/jmx_prometheus_javaagent-0.15.0.jar /opt/
COPY kafka-connect.yml /opt/

# Устанавливаем правильный путь к плагинам
ENV CONNECT_PLUGIN_PATH="/usr/share/java,/usr/share/java/kafka-connect-plugins"

# Проверяем, что плагины доступны (опционально)
RUN ls -l /usr/share/java/kafka-connect-plugins



