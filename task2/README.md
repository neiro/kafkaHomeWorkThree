# README.md

## Описание проекта

В данном репозитории представлен пример настройки контейнеров и пользовательского Kafka коннектора, предназначенного для чтения JSON-метрик из топика Kafka и их экспорта в формате Prometheus. После запуска всех сервисов можно:

- Отправлять тестовые метрики в Kafka.
- Наблюдать, как настроенный коннектор обрабатывает эти данные и предоставляет их в формате Prometheus.
- Просматривать метрики (Kafka, Connect, пользовательские метрики и др.) в Grafana и Prometheus.

## Состав проекта

**Kafka (broker)**  
- Используется образ `bitnami/kafka:3.7` в режиме KRaft.  
- Автоматически создаёт топики для упрощения.

**Schema Registry**  
- Используется образ `bitnami/schema-registry`.  
- Сохраняет и валидирует схемы (Avro/JSON).  
- В данном примере его использование опционально, но демонстрирует возможность масштабирования и применения схем сообщений.

**Kafka Connect**  
- Контейнер `kafka-connect` с поддержкой пользовательских плагинов.  
- Предназначен для чтения и записи данных из топиков Kafka во внешние системы.

**Пользовательский коннектор (Prometheus Connector)**  
- Реализован на Java (код в `kafka-connect`/`src/main`).  
- Слушает JSON-метрики из Kafka и раскрывает их на HTTP-эндпоинте `/metrics` в формате Prometheus.

**Prometheus**  
- Мониторинг метрик:  
  - Kafka Connect (через JMX-агент).  
  - Пользовательские метрики на порту 8090.

**Grafana**  
- Визуализация метрик из Prometheus.  
- Автоматически создаёт источник данных и дашборд для мониторинга.

**Kafka UI**  
- Веб-интерфейс для работы с топиками, консюмерами, продьюсерами и коннекторами.

**Test Producer**  
- Контейнер с Bash-скриптом `send-test-metrics.sh` для отправки метрик в формате JSON в топик `test-topic`.

**Prometheus JMX Exporter**  
- Вшит в kafka-connect.  
- Позволяет собирать JMX-метрики самого Kafka Connect.

## Инструкция по запуску

1. Убедитесь, что у вас установлены:
   - Docker и Docker Compose.

2. Клонируйте репозиторий и перейдите в папку проекта:
   ```bash
   git clone https://github.com/neiro/kafkaHomeWorkThree
   cd kafkaHomeWorkThree
   ```

3. Запустите все сервисы:
   ```bash
   docker-compose up -d --build
   ```

4. Убедитесь, что все контейнеры работают:
   ```bash
   docker-compose ps
   ```

5. Проверьте логи для `kafka-connect`:
   ```bash
   docker-compose logs -f kafka-connect
   ```
   Дождитесь статуса `healthy`. После этого `connector-init` автоматически зарегистрирует коннектор `prometheus-connector`.

## Проверка работы

**Kafka UI**  
- Перейдите в браузере по адресу `http://localhost:8080`.  
- Убедитесь, что виден кластер `kraft` и топик `test-topic`.

**Коннектор**  
- В Kafka UI откройте раздел Connectors.  
- Убедитесь, что `prometheus-connector` имеет статус `RUNNING`.

**Метрики**  
- Через несколько секунд после запуска `test-producer` в `test-topic` начнут поступать JSON-сообщения.  
- Проверьте метрики на эндпоинте:
  ```bash
  curl http://localhost:8090/metrics
  ```

**Prometheus**  
- Перейдите на `http://localhost:9090` → **Status** → **Targets**.  
- Убедитесь, что `kafka-connect:8090/metrics` имеет статус `UP`.

**Grafana**  
- Перейдите на `http://localhost:3000` (логин/пароль: `admin/admin`).  
- Откройте дашборд `Custom Metrics Dashboard`.  
- Убедитесь, что метрики (Alloc, FreeMemory, TotalMemory, PollCount и т. д.) обновляются каждые 5 секунд.

**Остановка проекта**  
Чтобы остановить контейнеры:
```bash
docker-compose down
```

## Заключение

Этот проект показывает, как настроить полный цикл обработки метрик: их отправку в Kafka, обработку пользовательским коннектором в Kafka Connect, преобразование в Prometheus-формат и визуализацию в Grafana.
