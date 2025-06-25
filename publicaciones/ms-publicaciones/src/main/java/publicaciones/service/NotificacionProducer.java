package publicaciones.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Queue;

import com.fasterxml.jackson.databind.ObjectMapper;

import publicaciones.dto.NotificacionDto;

@Service
public class NotificacionProducer {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private ObjectMapper mapper;

    // Declaración de las colas
    @Bean
    public Queue publicacionesQueue() {
        return new Queue("cola.publicaciones", true);
    }

    @Bean
    public Queue catalogoQueue() {
        return new Queue("catalogo.cola", true);
    }

    // Enviar notificación a ms-notificaciones
    public void enviarNotificacion(String mensaje, String tipo) {
        try {
            NotificacionDto dto = new NotificacionDto(mensaje, tipo);
            String json = mapper.writeValueAsString(dto);
            template.convertAndSend("cola.publicaciones", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Enviar DTO de libro o artículo a ms-catalogo
    public void enviarCatalogo(Object objeto) {
        try {
            String json = mapper.writeValueAsString(objeto);
            template.convertAndSend("catalogo.cola", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
