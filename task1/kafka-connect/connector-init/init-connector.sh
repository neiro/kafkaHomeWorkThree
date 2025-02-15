#!/bin/sh
# Ждем, пока Kafka Connect не станет доступен
echo "Ожидание запуска Kafka Connect..."
until curl -s http://kafka-connect:8083/connectors > /dev/null; do
  sleep 5
done
echo "Kafka Connect запущен."

# Проверяем, зарегистрирован ли уже коннектор
if curl -s http://kafka-connect:8083/connectors | grep -q "jdbc-source-connector"; then
  echo "Коннектор уже зарегистрирован."
else
  echo "Регистрация коннектора..."
  curl -X POST -H "Content-Type: application/json" --data @/config/jdbc-source-connector.json http://kafka-connect:8083/connectors
fi
