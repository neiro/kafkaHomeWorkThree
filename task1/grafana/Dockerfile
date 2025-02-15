FROM grafana/grafana:latest
# Копируем provisioning для дашбордов
COPY provisioning /etc/grafana/provisioning
# Копируем дашборды в папку для автоматической загрузки
COPY dashboards /var/lib/grafana/dashboards
