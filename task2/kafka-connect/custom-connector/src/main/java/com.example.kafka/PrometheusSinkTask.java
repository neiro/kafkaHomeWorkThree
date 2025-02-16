package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Класс {@code PrometheusSinkTask} представляет собой задачу Kafka Connect,
 * которая принимает данные из Kafka, преобразует их в формат JSON,
 * а затем передает метрики в HTTP-сервер, поддерживающий формат Prometheus.
 */
public class PrometheusSinkTask extends SinkTask {

    /** Объект для преобразования JSON-данных */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /** Экземпляр HTTP-сервера для предоставления метрик */
    private PrometheusHttpServer httpServer;

    /** Конвертер Kafka Connect для преобразования сообщений в JSON */
    private JsonConverter jsonConverter;

    /**
     * Инициализирует задачу, включая запуск HTTP-сервера для метрик и настройку JsonConverter.
     *
     * @param props конфигурационные параметры задачи
     */
    @Override
    public void start(Map<String, String> props) {
        try {
            // Читаем параметры конфигурации для HTTP-сервера Prometheus
            String listenerUrl = props.getOrDefault("prometheus.listener.url", "http://localhost");
            int listenerPort = Integer.parseInt(props.getOrDefault("prometheus.listener.port", "8090"));
            
            // Запускаем HTTP-сервер
            httpServer = PrometheusHttpServer.getInstance(listenerUrl, listenerPort);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка запуска HTTP-сервера для экспорта метрик", e);
        }
        
        // Настраиваем JsonConverter для преобразования Kafka Connect записей в JSON
        Map<String, Object> converterConfig = new HashMap<>();
        converterConfig.put("schemas.enable", false);
        jsonConverter = new JsonConverter();
        jsonConverter.configure(converterConfig, false);
    }

    /**
     * Обрабатывает коллекцию записей из Kafka.
     * Каждая запись преобразуется в JSON с помощью JsonConverter, затем парсится с помощью Jackson.
     * После этого метрика передается в Prometheus.
     *
     * @param records коллекция записей Kafka, поступивших в задачу
     */
    @Override
    public void put(Collection<SinkRecord> records) {
        for (SinkRecord record : records) {
            try {
                // Преобразуем данные Kafka Connect в JSON-строку
                byte[] jsonBytes = jsonConverter.fromConnectData(record.topic(), record.valueSchema(), record.value());
                String jsonString = new String(jsonBytes, StandardCharsets.UTF_8);
                System.out.println("Получен JSON: " + jsonString);
                
                // Парсим полученный JSON
                JsonNode payload = MAPPER.readTree(jsonString);
                
                if (payload != null && payload.isObject()) {
                    // Обрабатываем каждое поле JSON-объекта как отдельную метрику
                    payload.fieldNames().forEachRemaining(key -> {
                        JsonNode metric = payload.get(key);
                        String metricData = formatMetric(metric);
                        System.out.println("Форматированная метрика для " + key + ": " + metricData);
                        httpServer.addMetric(key, metricData);
                    });
                } else {
                    System.err.println("Полученный JSON не является объектом");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Форматирует метрику в формате Prometheus.
     * Пример формата:
     * <pre>
     * # HELP Alloc Alloc is bytes of allocated heap objects.
     * # TYPE Alloc gauge
     * Alloc 24293912.000000
     * </pre>
     *
     * @param metric JSON-объект, содержащий данные о метрике
     * @return строка с форматированной метрикой
     */
    private String formatMetric(JsonNode metric) {
        String name = metric.get("Name").asText();
        String type = metric.get("Type").asText();
        String description = metric.get("Description").asText();
        double value = metric.get("Value").asDouble();
        
        return String.format(Locale.US,
                "# HELP %s %s\n# TYPE %s %s\n%s %f\n",
                name, description, name, type.toLowerCase(), name, value);
    }

    /**
     * Завершает работу задачи и освобождает ресурсы.
     */
    @Override
    public void stop() {
        // Здесь можно добавить логику остановки, если необходимо
    }

    /**
     * Возвращает версию задачи.
     *
     * @return строка с версией задачи
     */
    @Override
    public String version() {
        return "1.0.0";
    }
}
