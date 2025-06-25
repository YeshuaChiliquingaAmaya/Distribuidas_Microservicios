package publicaciones.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Declaración de cola para solicitudes de sincronización
    @Bean
    public Queue relojSolicitud() {
        return QueueBuilder.durable("reloj.solicitud").build();
    }

    // Declaración de cola para respuestas de sincronización
    @Bean
    public Queue relojRespuesta() {
        return QueueBuilder.durable("reloj.respuesta").build();
    }

    // Bean para usar RabbitTemplate en envíos
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
