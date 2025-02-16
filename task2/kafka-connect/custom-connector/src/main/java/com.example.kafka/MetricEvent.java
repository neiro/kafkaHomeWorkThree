/**
 * Класс MetricEvent представляет собой модель метрики, 
 * содержащей основную информацию о событии.
 */
public class MetricEvent {
    
    /** Название метрики */
    private String name;
    
    /** Тип метрики (например, производительность, использование ресурсов и т. д.) */
    private String type;
    
    /** Описание метрики, поясняющее ее значение */
    private String description;
    
    /** Значение метрики */
    private double value;

    /**
     * Конструктор по умолчанию.
     * Создает объект без инициализированных значений.
     */
    public MetricEvent() {}

    /**
     * Конструктор с параметрами.
     * Позволяет создать объект метрики с заданными значениями.
     * 
     * @param name        Название метрики
     * @param type        Тип метрики
     * @param description Описание метрики
     * @param value       Значение метрики
     */
    public MetricEvent(String name, String type, String description, double value) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.value = value;
    }

    /**
     * Получает название метрики.
     *
     * @return Название метрики
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название метрики.
     *
     * @param name Новое название метрики
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получает тип метрики.
     *
     * @return Тип метрики
     */
    public String getType() {
        return type;
    }

    /**
     * Устанавливает тип метрики.
     *
     * @param type Новый тип метрики
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Получает описание метрики.
     *
     * @return Описание метрики
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание метрики.
     *
     * @param description Новое описание метрики
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Получает значение метрики.
     *
     * @return Значение метрики
     */
    public double getValue() {
        return value;
    }

    /**
     * Устанавливает значение метрики.
     *
     * @param value Новое значение метрики
     */
    public void setValue(double value) {
        this.value = value;
    }
}
