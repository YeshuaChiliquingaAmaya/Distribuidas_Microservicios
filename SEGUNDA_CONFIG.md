# Configuración de CockroachDB en el Proyecto

## 1. Configuración de Docker Compose

Se ha configurado un clúster de CockroachDB con 3 nodos utilizando Docker Compose. La configuración incluye:

- **Nodos**: 3 nodos (crdb-node1, crdb-node2, crdb-node3)
- **Puertos**:
  - Nodo 1: SQL (26257), UI (8080)
  - Nodo 2: SQL (26258), UI (8081)
  - Nodo 3: SQL (26259), UI (8082)
- **Volúmenes**: Se han creado volúmenes persistentes para cada nodo

## 2. Configuración de las Aplicaciones

### 2.1. Servicio de Publicaciones

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:26257/publicaciones_db?sslmode=disable&user=root&password=
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        default_schema: public
        jdbc:
          lob:
            non_contextual_creation: true
```

### 2.2. Servicio de Catálogo

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:26257/catalogo_db?sslmode=disable&user=root&password=
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
```

### 2.3. Servicio de Notificaciones

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:26257/notificaciones_db?sslmode=disable&user=root&password=
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
  jpa:
    hibernate:
      ddl-auto: update
```

## 3. Configuración de RabbitMQ

Se ha añadido RabbitMQ al archivo `docker-compose.yaml` con la siguiente configuración:

- **Imagen**: `rabbitmq:3.9-management`
- **Puertos**:
  - 5672: AMQP
  - 15672: Management UI
- **Credenciales**:
  - Usuario: admin
  - Contraseña: admin

## 4. Estado de las Bases de Datos

Las siguientes bases de datos han sido creadas exitosamente en el clúster de CockroachDB:

- `publicaciones_db` - Para el servicio de publicaciones
- `catalogo_db` - Para el servicio de catálogo
- `notificaciones_db` - Para el servicio de notificaciones

### Verificación de las bases de datos

Puedes verificar las bases de datos existentes ejecutando:

```sql
-- Conectarse a CockroachDB
$ cockroach sql --insecure --host=localhost:26257

-- Listar todas las bases de datos
SHOW DATABASES;
```

## 5. Comandos Útiles

### Iniciar los contenedores
```bash
cd bdd/
docker-compose up -d
```

### Detener los contenedores
```bash
cd bdd/
docker-compose down
```

### Acceder a la consola de administración de CockroachDB
- **URL**: http://localhost:8080

### Acceder a la consola de administración de RabbitMQ
- **URL**: http://localhost:15672
- **Usuario**: admin
- **Contraseña**: admin

## 6. Solución de Problemas

### Error de conexión a la base de datos
- Verificar que los contenedores de CockroachDB estén en ejecución: `docker ps`
- Verificar los logs de los contenedores: `docker logs <nombre_contenedor>`
- Asegurarse de que las credenciales en los archivos de configuración coincidan

### Error de RabbitMQ
- Verificar que el servicio de RabbitMQ esté en ejecución
- Comprobar que las credenciales en los archivos de configuración sean correctas
- Verificar que los puertos no estén siendo utilizados por otras aplicaciones
