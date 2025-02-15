#!/usr/bin/env python3
"""
Этот скрипт автоматически проводит серию экспериментов для тестирования JDBC Source Connector.
Для каждого эксперимента:
  1. Обновляется конфигурация коннектора через REST API Kafka Connect.
  2. Ждёт CONNECTOR_WAIT секунд для стабилизации коннектора.
  3. Выполняется нагрузочный тест – в таблицу PostgreSQL вставляются LOAD_COUNT строк.
     Теперь данные добавляются без очистки таблицы, и для каждой итерации
     используются уникальные значения идентификатора (id).
  4. Ждёт POST_LOAD_WAIT секунд для стабилизации метрик.
  5. Считывается значение метрики из Prometheus.
  6. Результаты эксперимента записываются в CSV-файл.

Перед запуском скрипта необходимо, чтобы:
  - Kafka Connect был запущен и доступен по адресу, указанному в KAFKA_CONNECT_URL.
  - В PostgreSQL база данных запущена, а таблица "customers" создана (например, через init.sql).
  - Prometheus собирает метрики и доступен по PROMETHEUS_URL.
"""

import argparse
import time
import csv
import os
import requests
import psycopg2
import itertools

# Конфигурация подключения
KAFKA_CONNECT_URL = "http://kafka-connect:8083/connectors/jdbc-source-connector/config"
PROMETHEUS_URL = "http://prometheus:9090/api/v1/query"
POSTGRES_CONFIG = {
    'host': 'postgres',
    'port': 5432,
    'dbname': 'customers',
    'user': 'postgres-user',
    'password': 'postgres-pw'
}

# Путь к CSV-файлу для логирования результатов экспериментов
CSV_FILE = "/logs/experiment_results.csv"

# Параметры нагрузочного теста
LOAD_COUNT = 30000          # Количество строк для вставки
CONNECTOR_WAIT = 5       # Время ожидания после обновления конфигурации (сек.)
POST_LOAD_WAIT = 10         # Время ожидания после нагрузки для стабилизации метрик (сек.)

def wait_for_kafka_connect():
    """
    Ждет, пока Kafka Connect не станет доступен, проверяя его REST API каждые 5 секунд.
    Выводит HTTP-код ответа для отладки.
    """
    print("Ожидание запуска Kafka Connect...", flush=True)
    while True:
        try:
            response = requests.get("http://kafka-connect:8083/connectors", timeout=5)
            print(f"Получен HTTP код: {response.status_code}", flush=True)
            if response.status_code == 200:
                print("Kafka Connect запущен.", flush=True)
                break
        except Exception as e:
            print(f"Ошибка подключения к Kafka Connect: {e}", flush=True)
        time.sleep(5)

def update_connector_config(new_config):
    """
    Получает текущую конфигурацию коннектора, обновляет её параметрами new_config и отправляет PUT-запрос.
    Если операция проходит успешно, возвращает True, иначе — False.
    """
    try:
        response = requests.get(KAFKA_CONNECT_URL, timeout=10)
        response.raise_for_status()
        config = response.json()
        print("Текущая конфигурация коннектора получена.", flush=True)
    except Exception as e:
        print("Ошибка получения текущей конфигурации коннектора:", e, flush=True)
        return False
    config.update(new_config)
    try:
        response = requests.put(KAFKA_CONNECT_URL, json=config, timeout=10)
        response.raise_for_status()
        print("Обновлена конфигурация коннектора:", new_config, flush=True)
        return True
    except Exception as e:
        print("Ошибка обновления конфигурации коннектора:", e, flush=True)
        return False

def run_load_test(load_count):
    """
    Выполняет нагрузочный тест, вставляя load_count новых строк в таблицу "customers".
    Вместо очистки таблицы (TRUNCATE) функция получает текущее максимальное значение id и
    вставляет новые записи с уникальными id. Это позволяет коннектору видеть новые строки.
    Каждые 1000 вставленных строк производится commit, и выводится прогресс.
    Возвращает время выполнения теста (сек.) или None при ошибке.
    """
    try:
        conn = psycopg2.connect(**POSTGRES_CONFIG)
        cur = conn.cursor()
        # Получаем текущее максимальное значение id, если таблица уже содержит данные
        cur.execute("SELECT COALESCE(MAX(id), 0) FROM customers;")
        max_id = cur.fetchone()[0]
        print(f"Текущий max_id: {max_id}", flush=True)
        start_time = time.time()
        for i in range(1, load_count + 1):
            new_id = max_id + i
            cur.execute(
                "INSERT INTO customers (id, name, email) VALUES (%s, %s, %s)",
                (new_id, f"User {new_id}", f"user{new_id}@example.com")
            )
            if i % 10000 == 0:
                conn.commit()
                print(f"Вставлено {i} строк...", flush=True)
        conn.commit()
        end_time = time.time()
        cur.close()
        conn.close()
        duration = end_time - start_time
        print(f"Вставлено {load_count} строк за {duration:.2f} секунд.", flush=True)
        return duration
    except Exception as e:
        print("Ошибка проведения нагрузочного теста:", e, flush=True)
        return None

def get_metric():
    """
    Отправляет запрос в Prometheus для получения метрики "kafka_connect_source_task_metrics_source_record_write_rate"
    для коннектора "jdbc-source-connector". Если метрика найдена, выводит все найденные серии
    и возвращает значение первой найденной метрики (float). Если массив result пуст, выводит сырые данные.
    """
    query = 'kafka_connect_source_task_metrics_source_record_write_rate{connector="jdbc-source-connector"}'
    try:
        response = requests.get(PROMETHEUS_URL, params={"query": query}, timeout=10)
        response.raise_for_status()
        data = response.json()
        if not data["data"]["result"]:
            print("Метрика не найдена. Сырые данные от Prometheus:", data, flush=True)
            return None
        print("Найденные метрики:", flush=True)
        for result in data["data"]["result"]:
            print(result, flush=True)
        value = float(data["data"]["result"][0]["value"][1])
        print(f"Получена метрика: {value}", flush=True)
        return value
    except Exception as e:
        print("Ошибка получения метрики:", e, flush=True)
        return None

def run_experiments_auto():
    """
    Генерирует набор из 20 экспериментов (комбинации параметров) для обновления конфигурации коннектора.
    Для каждого эксперимента:
      - Обновляется конфигурация коннектора.
      - Ждёт CONNECTOR_WAIT секунд для стабилизации.
      - Выполняется нагрузочный тест (вставка новых строк).
      - Ждёт POST_LOAD_WAIT секунд для стабилизации метрик.
      - Считывается метрика из Prometheus.
      - Результаты записываются в CSV-файл.
    """
    # Генерируем комбинации параметров
    batch_sizes = ["500", "5000"]
    linger_values = ["0", "3000"]
    compression_types = ["none", "snappy"]
    buffer_memories = ["33554432", "134217728"]

    combinations = list(itertools.product(batch_sizes, linger_values, compression_types, buffer_memories))
    
    experiments = []
    for combo in combinations[:16]:
        experiments.append({
            "producer.override.batch.size": combo[0],
            "producer.override.linger.ms": combo[1],
            "producer.override.compression.type": combo[2],
            "producer.override.buffer.memory": combo[3]
        })
    
    # Создаем CSV-файл и записываем заголовок
    with open(CSV_FILE, "w", newline="") as f:
        writer = csv.writer(f)
        writer.writerow([
            "Timestamp",
            "Experiment",
            "batch.size",
            "linger.ms",
            "compression.type",
            "buffer.memory",
            "Load Duration (s)",
            "Source Record Write Rate (ops/sec)"
        ])
    
    experiment_number = 0
    for exp in experiments:
        experiment_number += 1
        print(f"\nЗапуск эксперимента #{experiment_number} с параметрами: {exp}", flush=True)
        if not update_connector_config(exp):
            print("Пропуск эксперимента из-за ошибки обновления конфигурации.", flush=True)
            continue
        print(f"Ожидание {CONNECTOR_WAIT} сек. для стабилизации коннектора...", flush=True)
        time.sleep(CONNECTOR_WAIT)
        load_duration = run_load_test(LOAD_COUNT)
        if load_duration is None:
            print("Нагрузочный тест завершился с ошибкой, пропуск эксперимента.", flush=True)
            continue
        print(f"Ожидание {POST_LOAD_WAIT} сек. после нагрузки для стабилизации метрик...", flush=True)
        time.sleep(POST_LOAD_WAIT)
        metric_value = get_metric()
        timestamp = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
        with open(CSV_FILE, "a", newline="") as f:
            writer = csv.writer(f)
            writer.writerow([
                timestamp,
                experiment_number,
                exp.get("producer.override.batch.size"),
                exp.get("producer.override.linger.ms"),
                exp.get("producer.override.compression.type"),
                exp.get("producer.override.buffer.memory"),
                load_duration,
                metric_value
            ])
        print(f"Результат эксперимента #{experiment_number}: Время нагрузки = {load_duration:.2f} сек, метрика = {metric_value}", flush=True)
        time.sleep(10)

def run_experiment_manual(batch_size, linger_ms, compression_type, buffer_memory, load_count):
    """
    Выполняет одиночный эксперимент в ручном режиме с заданными параметрами.
    """
    exp = {
        "producer.override.batch.size": batch_size,
        "producer.override.linger.ms": linger_ms,
        "producer.override.compression.type": compression_type,
        "producer.override.buffer.memory": buffer_memory
    }
    if not os.path.exists(CSV_FILE):
        with open(CSV_FILE, "w", newline="") as f:
            writer = csv.writer(f)
            writer.writerow([
                "Timestamp",
                "Experiment",
                "batch.size",
                "linger.ms",
                "compression.type",
                "buffer.memory",
                "Load Duration (s)",
                "Source Record Write Rate (ops/sec)"
            ])
    experiment_number = 1
    print(f"\nЗапуск ручного эксперимента с параметрами: {exp}", flush=True)
    if not update_connector_config(exp):
        print("Ошибка обновления конфигурации.", flush=True)
        return
    print(f"Ожидание {CONNECTOR_WAIT} сек. для стабилизации коннектора...", flush=True)
    time.sleep(CONNECTOR_WAIT)
    load_duration = run_load_test(load_count)
    if load_duration is None:
        print("Нагрузочный тест завершился с ошибкой.", flush=True)
        return
    print(f"Ожидание {POST_LOAD_WAIT} сек. после нагрузки для стабилизации метрик...", flush=True)
    time.sleep(POST_LOAD_WAIT)
    metric_value = get_metric()
    timestamp = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    with open(CSV_FILE, "a", newline="") as f:
        writer = csv.writer(f)
        writer.writerow([
            timestamp,
            experiment_number,
            batch_size,
            linger_ms,
            compression_type,
            buffer_memory,
            load_duration,
            metric_value
        ])
    print(f"Ручной эксперимент: Время нагрузки = {load_duration:.2f} сек, метрика = {metric_value}", flush=True)

def main():
    parser = argparse.ArgumentParser(
        description="Автоматизированный запуск экспериментов для тестирования JDBC Source Connector"
    )
    parser.add_argument("--mode", choices=["auto", "manual"], default="auto",
                        help="Режим: auto (по умолчанию) или manual")
    parser.add_argument("--batch_size", type=str, help="Ручной режим: producer.override.batch.size")
    parser.add_argument("--linger_ms", type=str, help="Ручной режим: producer.override.linger.ms")
    parser.add_argument("--compression_type", type=str, help="Ручной режим: producer.override.compression.type")
    parser.add_argument("--buffer_memory", type=str, help="Ручной режим: producer.override.buffer.memory")
    parser.add_argument("--load_count", type=int, default=50000,
                        help="Количество строк для нагрузочного теста (по умолчанию 50000)")
    args = parser.parse_args()
    
    wait_for_kafka_connect()
    
    if args.mode == "auto":
        run_experiments_auto()
    else:
        if not (args.batch_size and args.linger_ms and args.compression_type and args.buffer_memory):
            print("Для ручного режима укажите --batch_size, --linger_ms, --compression_type, --buffer_memory", flush=True)
            return
        run_experiment_manual(args.batch_size, args.linger_ms, args.compression_type, args.buffer_memory, args.load_count)

if __name__ == "__main__":
    main()
