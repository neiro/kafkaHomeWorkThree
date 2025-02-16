package org.example;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.common.config.ConfigDef;

import java.util.List;
import java.util.Map;

/**
 * Класс {@code PrometheusSinkConnector} представляет собой пользовательский Kafka Sink-коннектор 
 * для передачи метрик из Kafka в Prometheus. Он управляет конфигурацией и запуском задач, 
 * которые обрабатывают поступающие сообщения из Kafka.
 */
public class PrometheusSinkConnector extends SinkConnector {

    /** Хранилище конфигурационных параметров коннектора */
    private Map<String, String> configProps;

    /**
     * Инициализирует коннектор с заданными параметрами.
     * Данный метод вызывается при старте коннектора.
     *
     * @param props свойства коннектора, заданные пользователем
     */
    @Override
    public void start(Map<String, String> props) {
        this.configProps = props;
        // Можно добавить дополнительные проверки или инициализацию здесь
    }

    /**
     * Определяет класс задачи, который будет использоваться для обработки записей Kafka.
     *
     * @return Класс {@code PrometheusSinkTask}, реализующий {@link Task}
     */
    @Override
    public Class<? extends Task> taskClass() {
        return PrometheusSinkTask.class;
    }

    /**
     * Возвращает список конфигураций для каждой задачи.
     * В данном случае используется одна и та же конфигурация для всех задач.
     *
     * @param maxTasks максимальное количество задач (обычно 1)
     * @return список конфигурационных параметров для каждой задачи
     */
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        return List.of(configProps);
    }

    /**
     * Останавливает коннектор и освобождает ресурсы.
     * Данный метод вызывается при завершении работы коннектора.
     */
    @Override
    public void stop() {
        // Здесь можно добавить логику для остановки коннектора, если необходимо
    }

    /**
     * Определяет и возвращает схему конфигурации коннектора.
     * Используется для проверки и валидации параметров коннектора.
     *
     * @return объект {@link ConfigDef}, описывающий доступные настройки
     */
    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define("prometheus.listener.url", ConfigDef.Type.STRING, "http://localhost",
                        ConfigDef.Importance.HIGH, "Базовый URL для HTTP-сервера Prometheus")
                .define("prometheus.listener.port", ConfigDef.Type.INT, 8090,
                        ConfigDef.Importance.HIGH, "Порт для HTTP-сервера Prometheus")
                .define("topics", ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH, "Список топиков Kafka для извлечения метрик");
    }

    /**
     * Возвращает версию коннектора.
     * 
     * @return строка, содержащая версию коннектора
     */
    @Override
    public String version() {
        return "1.0.0";
    }
}
