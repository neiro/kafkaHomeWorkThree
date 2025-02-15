FROM python:3.9-slim
RUN pip install requests psycopg2-binary
WORKDIR /app
COPY experiment_runner.py .
CMD ["python", "experiment_runner.py"]
