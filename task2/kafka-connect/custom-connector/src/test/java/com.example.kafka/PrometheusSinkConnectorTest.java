package org.example;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkTask;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PrometheusSinkConnectorTest {

    @Test
    public void testStartAndTaskConfigs() {
        PrometheusSinkConnector connector = new PrometheusSinkConnector();
        Map<String, String> props = new HashMap<>();
        props.put("connector.class", "org.example.PrometheusSinkConnector");
        props.put("tasks.max", "1");
        props.put("topics", "test-topic");
        // Дополнительные настройки можно добавить при необходимости.
        connector.start(props);

        // Проверяем, что taskConfigs возвращает список с одной конфигурацией.
        List<Map<String, String>> taskConfigs = connector.taskConfigs(1);
        assertNotNull(taskConfigs, "taskConfigs не должен быть null");
        assertEquals(1, taskConfigs.size(), "Размер списка taskConfigs должен быть 1");
        assertEquals(props, taskConfigs.get(0), "Конфигурация задачи должна совпадать с переданной");
    }

    @Test
    public void testTaskClass() {
        PrometheusSinkConnector connector = new PrometheusSinkConnector();
        Class<?> taskClass = connector.taskClass();
        assertTrue(PrometheusSinkTask.class.isAssignableFrom(taskClass),
                "taskClass должен быть наследником PrometheusSinkTask");
    }
    

    @Test
    public void testVersion() {
        PrometheusSinkConnector connector = new PrometheusSinkConnector();
        assertEquals("1.0.0", connector.version(), "Версия коннектора должна быть '1.0.0'");
    }

    @Test
    public void testConfig() {
        PrometheusSinkConnector connector = new PrometheusSinkConnector();
        ConfigDef configDef = connector.config();
        assertNotNull(configDef, "Конфигурация не должна быть null");
        // При необходимости можно добавить дополнительные проверки параметров конфигурации.
    }

    @Test
    public void testStop() {
        PrometheusSinkConnector connector = new PrometheusSinkConnector();
        // Метод stop() в текущей реализации ничего не делает, проверяем, что не выбрасывается исключений.
        assertDoesNotThrow(() -> connector.stop(), "Метод stop() не должен выбрасывать исключения");
    }
}
