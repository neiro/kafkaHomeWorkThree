### Логи работы Kafka Connect

Пример логов работы Kafka Connect с использованием Debezium для PostgreSQL:

```plaintext
2025-02-17 04:09:59 [2025-02-16 23:09:59,711] INFO [0:0:0:0:0:0:0:1] - - [16/Feb/2025:23:09:59 +0000] "GET /connectors HTTP/1.1" 200 39 "-" "curl/7.61.1" 2 (org.apache.kafka.connect.runtime.rest.RestServer)
2025-02-17 04:10:02 [2025-02-16 23:10:02,962] INFO Loading the custom source info struct maker plugin: io.debezium.connector.postgresql.PostgresSourceInfoStructMaker (io.debezium.config.CommonConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,971] INFO Successfully tested connection for jdbc:postgresql://postgres:5432/customers with user 'postgres-user' (io.debezium.connector.postgresql.PostgresConnector)
2025-02-17 04:10:02 [2025-02-16 23:10:02,976] INFO Connection gracefully closed (io.debezium.jdbc.JdbcConnection)
2025-02-17 04:10:02 [2025-02-16 23:10:02,977] INFO AbstractConfig values: 
2025-02-17 04:10:02  (org.apache.kafka.common.config.AbstractConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,985] INFO [Worker clientId=connect-localhost:8083, groupId=kafka-connect] Connector pg-connector config updated (org.apache.kafka.connect.runtime.distributed.DistributedHerder)
2025-02-17 04:10:02 [2025-02-16 23:10:02,986] INFO [Worker clientId=connect-localhost:8083, groupId=kafka-connect] Handling connector-only config update by restarting connector pg-connector (org.apache.kafka.connect.runtime.distributed.DistributedHerder)
2025-02-17 04:10:02 [2025-02-16 23:10:02,986] INFO Stopping connector pg-connector (org.apache.kafka.connect.runtime.Worker)
2025-02-17 04:10:02 [2025-02-16 23:10:02,986] INFO Scheduled shutdown for WorkerConnector{id=pg-connector} (org.apache.kafka.connect.runtime.WorkerConnector)
2025-02-17 04:10:02 [2025-02-16 23:10:02,987] INFO Completed shutdown for WorkerConnector{id=pg-connector} (org.apache.kafka.connect.runtime.WorkerConnector)
2025-02-17 04:10:02 [2025-02-16 23:10:02,990] INFO [Worker clientId=connect-localhost:8083, groupId=kafka-connect] Starting connector pg-connector (org.apache.kafka.connect.runtime.distributed.DistributedHerder)
2025-02-17 04:10:02 [2025-02-16 23:10:02,990] INFO Creating connector pg-connector of type io.debezium.connector.postgresql.PostgresConnector (org.apache.kafka.connect.runtime.Worker)
2025-02-17 04:10:02 [2025-02-16 23:10:02,991] INFO SourceConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.SourceConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,991] INFO EnrichedConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 transforms.unwrap.add.fields = []
2025-02-17 04:10:02 transforms.unwrap.add.fields.prefix = __
2025-02-17 04:10:02 transforms.unwrap.add.headers = []
2025-02-17 04:10:02 transforms.unwrap.add.headers.prefix = __
2025-02-17 04:10:02 transforms.unwrap.delete.handling.mode = rewrite
2025-02-17 04:10:02 transforms.unwrap.drop.fields.from.key = false
2025-02-17 04:10:02 transforms.unwrap.drop.fields.header.name = null
2025-02-17 04:10:02 transforms.unwrap.drop.fields.keep.schema.compatible = true
2025-02-17 04:10:02 transforms.unwrap.drop.tombstones = false
2025-02-17 04:10:02 transforms.unwrap.negate = false
2025-02-17 04:10:02 transforms.unwrap.predicate = null
2025-02-17 04:10:02 transforms.unwrap.route.by.field = 
2025-02-17 04:10:02 transforms.unwrap.type = class io.debezium.transforms.ExtractNewRecordState
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.ConnectorConfig$EnrichedConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,992] INFO EnrichedSourceConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.default.exclude = []
2025-02-17 04:10:02 topic.creation.default.include = [.*]
2025-02-17 04:10:02 topic.creation.default.partitions = 1
2025-02-17 04:10:02 topic.creation.default.replication.factor = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.SourceConnectorConfig$EnrichedSourceConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,993] INFO EnrichedConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.default.exclude = []
2025-02-17 04:10:02 topic.creation.default.include = [.*]
2025-02-17 04:10:02 topic.creation.default.partitions = 1
2025-02-17 04:10:02 topic.creation.default.replication.factor = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 transforms.unwrap.add.fields = []
2025-02-17 04:10:02 transforms.unwrap.add.fields.prefix = __
2025-02-17 04:10:02 transforms.unwrap.add.headers = []
2025-02-17 04:10:02 transforms.unwrap.add.headers.prefix = __
2025-02-17 04:10:02 transforms.unwrap.delete.handling.mode = rewrite
2025-02-17 04:10:02 transforms.unwrap.drop.fields.from.key = false
2025-02-17 04:10:02 transforms.unwrap.drop.fields.header.name = null
2025-02-17 04:10:02 transforms.unwrap.drop.fields.keep.schema.compatible = true
2025-02-17 04:10:02 transforms.unwrap.drop.tombstones = false
2025-02-17 04:10:02 transforms.unwrap.negate = false
2025-02-17 04:10:02 transforms.unwrap.predicate = null
2025-02-17 04:10:02 transforms.unwrap.route.by.field = 
2025-02-17 04:10:02 transforms.unwrap.type = class io.debezium.transforms.ExtractNewRecordState
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.ConnectorConfig$EnrichedConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,994] INFO Instantiated connector pg-connector with version 3.0.6.Final of type class io.debezium.connector.postgresql.PostgresConnector (org.apache.kafka.connect.runtime.Worker)
2025-02-17 04:10:02 [2025-02-16 23:10:02,994] INFO Finished creating connector pg-connector (org.apache.kafka.connect.runtime.Worker)
2025-02-17 04:10:02 [2025-02-16 23:10:02,996] INFO SourceConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.SourceConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,996] INFO 172.18.0.1 - - [16/Feb/2025:23:10:02 +0000] "PUT /connectors/pg-connector/config HTTP/1.1" 200 779 "-" "curl/8.10.1" 38 (org.apache.kafka.connect.runtime.rest.RestServer)
2025-02-17 04:10:02 [2025-02-16 23:10:02,996] INFO EnrichedConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 transforms.unwrap.add.fields = []
2025-02-17 04:10:02 transforms.unwrap.add.fields.prefix = __
2025-02-17 04:10:02 transforms.unwrap.add.headers = []
2025-02-17 04:10:02 transforms.unwrap.add.headers.prefix = __
2025-02-17 04:10:02 transforms.unwrap.delete.handling.mode = rewrite
2025-02-17 04:10:02 transforms.unwrap.drop.fields.from.key = false
2025-02-17 04:10:02 transforms.unwrap.drop.fields.header.name = null
2025-02-17 04:10:02 transforms.unwrap.drop.fields.keep.schema.compatible = true
2025-02-17 04:10:02 transforms.unwrap.drop.tombstones = false
2025-02-17 04:10:02 transforms.unwrap.negate = false
2025-02-17 04:10:02 transforms.unwrap.predicate = null
2025-02-17 04:10:02 transforms.unwrap.route.by.field = 
2025-02-17 04:10:02 transforms.unwrap.type = class io.debezium.transforms.ExtractNewRecordState
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.ConnectorConfig$EnrichedConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,997] INFO EnrichedSourceConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.default.exclude = []
2025-02-17 04:10:02 topic.creation.default.include = [.*]
2025-02-17 04:10:02 topic.creation.default.partitions = 1
2025-02-17 04:10:02 topic.creation.default.replication.factor = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.SourceConnectorConfig$EnrichedSourceConnectorConfig)
2025-02-17 04:10:02 [2025-02-16 23:10:02,998] INFO EnrichedConnectorConfig values: 
2025-02-17 04:10:02 config.action.reload = restart
2025-02-17 04:10:02 connector.class = io.debezium.connector.postgresql.PostgresConnector
2025-02-17 04:10:02 errors.log.enable = false
2025-02-17 04:10:02 errors.log.include.messages = false
2025-02-17 04:10:02 errors.retry.delay.max.ms = 60000
2025-02-17 04:10:02 errors.retry.timeout = 0
2025-02-17 04:10:02 errors.tolerance = none
2025-02-17 04:10:02 exactly.once.support = requested
2025-02-17 04:10:02 header.converter = null
2025-02-17 04:10:02 key.converter = null
2025-02-17 04:10:02 name = pg-connector
2025-02-17 04:10:02 offsets.storage.topic = null
2025-02-17 04:10:02 predicates = []
2025-02-17 04:10:02 tasks.max = 1
2025-02-17 04:10:02 topic.creation.default.exclude = []
2025-02-17 04:10:02 topic.creation.default.include = [.*]
2025-02-17 04:10:02 topic.creation.default.partitions = 1
2025-02-17 04:10:02 topic.creation.default.replication.factor = 1
2025-02-17 04:10:02 topic.creation.groups = []
2025-02-17 04:10:02 transaction.boundary = poll
2025-02-17 04:10:02 transaction.boundary.interval.ms = null
2025-02-17 04:10:02 transforms = [unwrap]
2025-02-17 04:10:02 transforms.unwrap.add.fields = []
2025-02-17 04:10:02 transforms.unwrap.add.fields.prefix = __
2025-02-17 04:10:02 transforms.unwrap.add.headers = []
2025-02-17 04:10:02 transforms.unwrap.add.headers.prefix = __
2025-02-17 04:10:02 transforms.unwrap.delete.handling.mode = rewrite
2025-02-17 04:10:02 transforms.unwrap.drop.fields.from.key = false
2025-02-17 04:10:02 transforms.unwrap.drop.fields.header.name = null
2025-02-17 04:10:02 transforms.unwrap.drop.fields.keep.schema.compatible = true
2025-02-17 04:10:02 transforms.unwrap.drop.tombstones = false
2025-02-17 04:10:02 transforms.unwrap.negate = false
2025-02-17 04:10:02 transforms.unwrap.predicate = null
2025-02-17 04:10:02 transforms.unwrap.route.by.field = 
2025-02-17 04:10:02 transforms.unwrap.type = class io.debezium.transforms.ExtractNewRecordState
2025-02-17 04:10:02 value.converter = null
2025-02-17 04:10:02  (org.apache.kafka.connect.runtime.ConnectorConfig$EnrichedConnectorConfig)
2025-02-17 04:10:04 [2025-02-16 23:10:04,756] INFO [0:0:0:0:0:0:0:1] - - [16/Feb/2025:23:10:04 +0000] "GET /connectors HTTP/1.1" 200 39 "-" "curl/7.61.1" 2 (org.apache.kafka.connect.runtime.rest.RestServer)
2025-02-17 04:10:05 [2025-02-16 23:10:05,376] INFO 172.18.0.6 - - [16/Feb/2025:23:10:05 +0000] "GET /connectors/pg-connector/config HTTP/1.1" 200 682 "-" "Apache-HttpClient/4.5.14 (Java/11.0.21)" 4 (org.apache.kafka.connect.runtime.rest.RestServer)
2025-02-17 04:10:05 [2025-02-16 23:10:05,695] INFO 172.18.0.6 - - [16/Feb/2025:23:10:05 +0000] "GET /connectors/pg-connector/config HTTP/1.1" 200 682 "-" "Apache-HttpClient/4.5.14 (Java/11.0.21)" 3 (org.apache.kafka.connect.runtime.rest.RestServer)
2025-02-17 04:10:09 [2025-02-16 23:10:09,795] INFO [0:0:0:0:0:0:0:1] - - [16/Feb/2025:23:10:09 +0000] "GET /connectors HTTP/1.1" 200 39 "-" "curl/7.61.1" 2 (org.apache.kafka.connect.runtime.rest.RestServer)
2025-02-17 04:10:14 [2025-02-16 23:10:14,836] INFO [0:0:0:0:0:0:0:1] - - [16/Feb/2025:23:10:14 +0000] "GET /connectors HTTP/1.1" 200 39 "-" "curl/7.61.1" 2 (org.apache.kafka.connect.runtime.rest.RestServer)
```

**Этот лог демонстрирует процесс обновления конфигурации коннектора pg-connector, его перезапуск и успешное подключение к PostgreSQL.**
