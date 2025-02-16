package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс {@code PrometheusSinkTaskTest} содержит тесты для проверки корректного форматирования метрик 
 * в формате Prometheus с использованием приватного метода {@code formatMetric()} класса {@link PrometheusSinkTask}.
 */
public class PrometheusSinkTaskTest {

    /**
     * Тест проверяет, что форматирование gauge-метрики выполняется корректно.
     * Использует рефлексию для вызова приватного метода {@code formatMetric()}.
     *
     * @throws Exception если произошла ошибка при работе с рефлексией
     */
    @Test
    public void testFormatMetricGauge() throws Exception {
        // Пример входного JSON для gauge-метрики
        String json = "{\n" +
                "  \"Type\": \"gauge\",\n" +
                "  \"Name\": \"Alloc\",\n" +
                "  \"Description\": \"Alloc is bytes of allocated heap objects.\",\n" +
                "  \"Value\": 24293912\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode metricNode = mapper.readTree(json);

        // Создаем экземпляр тестируемого класса
        PrometheusSinkTask task = new PrometheusSinkTask();

        // Доступ к приватному методу formatMetric через рефлексию
        Method formatMetricMethod = PrometheusSinkTask.class.getDeclaredMethod("formatMetric", JsonNode.class);
        formatMetricMethod.setAccessible(true);

        // Вызываем приватный метод и получаем результат
        String result = (String) formatMetricMethod.invoke(task, metricNode);

        // Ожидаемый результат в формате Prometheus
        String expected = "# HELP Alloc Alloc is bytes of allocated heap objects.\n" +
                          "# TYPE Alloc gauge\n" +
                          "Alloc 24293912.000000\n";

        // Проверяем, что форматированная строка соответствует ожидаемой
        assertEquals(expected, result);
    }

    /**
     * Тест проверяет, что форматирование counter-метрики выполняется корректно.
     * Использует рефлексию для вызова приватного метода {@code formatMetric()}.
     *
     * @throws Exception если произошла ошибка при работе с рефлексией
     */
    @Test
    public void testFormatMetricCounter() throws Exception {
        // Пример входного JSON для counter-метрики
        String json = "{\n" +
                "  \"Type\": \"counter\",\n" +
                "  \"Name\": \"PollCount\",\n" +
                "  \"Description\": \"PollCount is quantity of metrics collection iteration.\",\n" +
                "  \"Value\": 3\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode metricNode = mapper.readTree(json);

        // Создаем экземпляр тестируемого класса
        PrometheusSinkTask task = new PrometheusSinkTask();

        // Доступ к приватному методу formatMetric через рефлексию
        Method formatMetricMethod = PrometheusSinkTask.class.getDeclaredMethod("formatMetric", JsonNode.class);
        formatMetricMethod.setAccessible(true);

        // Вызываем приватный метод и получаем результат
        String result = (String) formatMetricMethod.invoke(task, metricNode);

        // Ожидаемый результат в формате Prometheus
        String expected = "# HELP PollCount PollCount is quantity of metrics collection iteration.\n" +
                          "# TYPE PollCount counter\n" +
                          "PollCount 3.000000\n";

        // Проверяем, что форматированная строка соответствует ожидаемой
        assertEquals(expected, result);
    }
}
