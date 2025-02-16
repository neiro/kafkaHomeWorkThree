#!/bin/bash

# ========================================================
# Скрипт send-test-metrics.sh
# ========================================================
# Описание:
# Данный скрипт отправляет тестовые метрики в Kafka в течение 10 минут.
# Метрики формируются в компактный JSON-объект без лишних переносов строк.
# Данные обновляются каждые 300 миллисекунд.
#
# Метрики:
# - Alloc: объем выделенной кучи (heap allocation)
# - FreeMemory: доступная оперативная память
# - TotalMemory: общий объем оперативной памяти
# - PollCount: количество итераций опроса метрик
#
# Зависимости:
# - Должен быть установлен `jq` для генерации JSON
# - Должен быть доступен Kafka Broker для отправки сообщений
#
# ========================================================

# =========================
# Конфигурация и параметры
# =========================

# Определяем конечное время выполнения (10 минут = 600 секунд)
end_time=$(( $(date +%s) + 600 ))

# Параметры Kafka
BROKER_LIST="kafka-0:9092"  # Проверьте, что имя брокера соответствует вашему docker-compose.yml
TOPIC="test-topic"

# Инициализируем счетчик итераций отправки
poll_count=0

# Выводим информационное сообщение перед стартом
echo "Начинается отправка тестовых метрик в Kafka на топик '$TOPIC' в течение 10 минут..."

# =========================
# Основной цикл отправки метрик
# =========================
while [ $(date +%s) -lt $end_time ]; do
    # Генерируем случайные значения метрик:
    # - Alloc (выделенная память) в диапазоне 20МБ - 70МБ
    # - FreeMemory (свободная память) в диапазоне 7ГБ - 17ГБ
    # - TotalMemory (всего памяти) в диапазоне 10ГБ - 60ГБ
    # - PollCount (счетчик итераций) увеличивается на 1

    alloc=$(( RANDOM % 50000000 + 20000000 ))
    free_memory=$(( RANDOM % 10000000000 + 7000000000 ))
    total_memory=$(( RANDOM % 50000000000 + 10000000000 ))
    poll_count=$(( poll_count + 1 ))
    
    # =========================
    # Формирование JSON-метрик
    # =========================
    # Используем `jq -n -c` для создания JSON-объекта без переносов строк
    json_payload=$(jq -n -c \
      --argjson alloc "$alloc" \
      --argjson free_memory "$free_memory" \
      --argjson total_memory "$total_memory" \
      --argjson poll_count "$poll_count" \
      '{
         "Alloc": {"Type": "gauge", "Name": "Alloc", "Description": "Alloc is bytes of allocated heap objects.", "Value": $alloc},
         "FreeMemory": {"Type": "gauge", "Name": "FreeMemory", "Description": "RAM available for programs to allocate", "Value": $free_memory},
         "TotalMemory": {"Type": "gauge", "Name": "TotalMemory", "Description": "Total amount of RAM on this system", "Value": $total_memory},
         "PollCount": {"Type": "counter", "Name": "PollCount", "Description": "PollCount is quantity of metrics collection iteration.", "Value": $poll_count}
      }')
    
    # =========================
    # Отправка сообщения в Kafka
    # =========================
    # Сообщение передается как единый JSON-объект без лишних пробелов и переносов строк.
    echo "$json_payload" | tr -d '\n' | /opt/bitnami/kafka/bin/kafka-console-producer.sh --broker-list "$BROKER_LIST" --topic "$TOPIC"
    
    # Пауза между отправками (300 мс)
    sleep 0.3
done

# =========================
# Завершение работы скрипта
# =========================
echo "Отправка тестовых метрик завершена."
