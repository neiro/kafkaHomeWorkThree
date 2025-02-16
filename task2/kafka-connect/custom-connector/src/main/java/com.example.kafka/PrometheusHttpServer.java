package org.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс {@code PrometheusHttpServer} реализует HTTP-сервер для предоставления метрик в формате Prometheus.
 * Использует Jetty для обработки HTTP-запросов.
 * Реализован по паттерну Singleton для обеспечения единственного экземпляра сервера.
 */
public class PrometheusHttpServer {

    /** Единственный экземпляр сервера (Singleton) */
    private static PrometheusHttpServer instance;

    /** Базовый URL для отладки */
    private String baseUrl;

    /** Экземпляр HTTP-сервера Jetty */
    private Server server;

    /** Хранилище метрик, полученных из Kafka, в виде пар "имя метрики - данные" */
    private final ConcurrentHashMap<String, String> metrics = new ConcurrentHashMap<>();

    /**
     * Приватный конструктор для предотвращения создания экземпляров извне (Singleton).
     */
    private PrometheusHttpServer() {}

    /**
     * Получает единственный экземпляр сервера. Если сервер еще не был создан, он запускается.
     *
     * @param url  базовый URL для отладки
     * @param port порт, на котором сервер будет запущен
     * @return единственный экземпляр {@code PrometheusHttpServer}
     * @throws Exception если сервер не удалось запустить
     */
    public static PrometheusHttpServer getInstance(String url, int port) throws Exception {
        if (instance == null) {
            instance = new PrometheusHttpServer();
            instance.baseUrl = url;
            instance.start(port);
        }
        return instance;
    }

    /**
     * Запускает HTTP-сервер на указанном порту и настраивает обработчик метрик.
     *
     * @param port порт, на котором сервер будет принимать HTTP-запросы
     * @throws Exception если сервер не удалось запустить
     */
    public void start(int port) throws Exception {
        server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler();

        // Добавляем обработчик для пути /metrics
        handler.addServlet(new ServletHolder(new MetricsServlet(metrics, baseUrl)), "/metrics");

        server.setHandler(handler);
        server.start();
        System.out.println("HTTP-сервер для метрик запущен на порту " + port);

    }

    /**
     * Добавляет или обновляет метрику в хранилище.
     *
     * @param name имя метрики
     * @param data форматированные данные метрики в формате Prometheus
     */
    public void addMetric(String name, String data) {
        System.out.println("Добавление метрики: " + name + " -> " + data);
        metrics.put(name, data);
    }

    /**
     * Внутренний класс {@code MetricsServlet} обрабатывает HTTP-запросы для получения метрик.
     */
    private static class MetricsServlet extends HttpServlet {

        /** Хранилище метрик */
        private final ConcurrentHashMap<String, String> metrics;

        /** Базовый URL сервера для отладки */
        private final String baseUrl;

        /**
         * Конструктор сервлета для обработки метрик.
         *
         * @param metrics хранилище метрик
         * @param baseUrl базовый URL сервера
         */
        public MetricsServlet(ConcurrentHashMap<String, String> metrics, String baseUrl) {
            this.metrics = metrics;
            this.baseUrl = baseUrl;
        }

        /**
         * Обрабатывает HTTP GET-запросы и возвращает метрики в формате Prometheus.
         *
         * @param req  объект запроса {@link HttpServletRequest}
         * @param resp объект ответа {@link HttpServletResponse}
         * @throws IOException если произошла ошибка ввода-вывода при обработке запроса
         */
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("text/plain");

            // Формируем ответ с заголовком и всеми метриками
            StringBuilder response = new StringBuilder("# Base URL: " + baseUrl + "\n");
            metrics.values().forEach(response::append);

            resp.getWriter().write(response.toString());
        }
    }
}
