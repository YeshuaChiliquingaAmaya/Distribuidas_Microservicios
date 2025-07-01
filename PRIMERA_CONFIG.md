# Configuración Inicial de Microservicios

## 1. Configuración del Servidor Eureka

### `ms-eureka-server/src/main/resources/application.yml`
```yaml
server:
  port: 8761

eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

## 2. Configuración del API Gateway

### `ms-api-gateway/src/main/resources/application.yml`
```yaml
server:
  port: 8090

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        - id: ms-publicaciones
          uri: lb://SERVICIO-PUBLICACIONES
          predicates:
            - Path=/api/publicaciones/**
          filters:
            - StripPrefix=2
        - id: ms-catalogo
          uri: lb://SERVICIO-CATALOGO
          predicates:
            - Path=/api/catalogo/**
          filters:
            - StripPrefix=2

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
```

## 3. Configuración del Microservicio de Publicaciones

### `publicaciones/ms-publicaciones/src/main/resources/application.yaml`
```yaml
spring:
  application:
    name: SERVICIO-PUBLICACIONES
  datasource:
    url: jdbc:postgresql://localhost:5432/publicaciones_db
    username: postgres
    password: admin
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
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin

server:
  port: 8001

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
```

## 4. Configuración del Microservicio de Catálogo

### `catalogo/ms-catalogo/src/main/resources/application.yml`
```yaml
spring:
  application:
    name: SERVICIO-CATALOGO
  datasource:
    url: jdbc:postgresql://localhost:5432/catalogo_db
    username: postgres
    password: admin
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin

server:
  port: 8087

reloj:
  intervalo: 10

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
```

## 5. Configuración del Microservicio de Notificaciones

### `notificaciones/notificaciones/src/main/resources/application.yml`
```yaml
spring:
  application:
    name: SERVICIO-NOTIFICACIONES
  datasource:
    url: jdbc:postgresql://localhost:5432/notificaciones_db
    username: postgres
    password: admin
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 5
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin

server:
  port: 8088

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    preferIpAddress: true
```

## 6. Configuración de Docker Compose para Servicios de Base de Datos

### `bdd/docker-compose.yaml`
```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    container_name: postgres-publications
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: publicaciones_db
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  rabbitmq:
    image: rabbitmq:3.9-management
    container_name: rabbitmq
    hostname: rabbitmq
    ports:
      - "5672:5672"  # AMQP
      - "15672:15672"  # Management UI
    environment:
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=admin
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

volumes:
  postgres_data:
  rabbitmq_data:
```

## 7. Dependencias Comunes en `pom.xml`

### Dependencias principales para cada microservicio:
```xml
<!-- Spring Boot Starter Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot Starter Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL JDBC -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Spring Boot Starter AMQP (RabbitMQ) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>

<!-- Spring Cloud Starter Netflix Eureka Client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<!-- Spring Boot Starter Actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Dependencias específicas para el API Gateway:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

### Dependencias específicas para Eureka Server:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

## 8. Configuración de Versiones

### Versión de Spring Boot:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.3</version>
    <relativePath/>
</parent>
```

### Versión de Spring Cloud:
```xml
<properties>
    <java.version>17</java.version>
    <spring-cloud.version>2025.0.0</spring-cloud.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 9. Pasos para Iniciar los Servicios

1. Iniciar los servicios de base de datos con Docker Compose:
   ```bash
   cd bdd
   docker-compose up -d
   ```

2. Iniciar el servidor Eureka:
   ```bash
   cd ms-eureka-server
   mvn spring-boot:run
   ```

3. Iniciar los microservicios (en terminales separadas):
   ```bash
   # En el directorio de cada microservicio
   mvn spring-boot:run
   ```

4. Verificar que los servicios estén registrados en Eureka:
   - Abrir en el navegador: http://localhost:8761

5. Acceder a la interfaz de RabbitMQ:
   - Abrir en el navegador: http://localhost:15672
   - Usuario: admin
   - Contraseña: admin

## 10. Notas Importantes

- Asegúrate de que todos los servicios tengan nombres únicos en `spring.application.name`
- Verifica que los puertos no estén en uso antes de iniciar los servicios
- La configuración de conexión a la base de datos debe coincidir con la configuración de tus contenedores Docker
- Los servicios se comunican a través de Eureka usando los nombres de servicio definidos en `spring.application.name`
