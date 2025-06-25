package ms_catalogo.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import ms_catalogo.dto.HoraClienteDto; 

@Service
public class RelojProducer {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private ObjectMapper mapper;

    private static final String NOMBRE_NODO = "ms-publicaciones";

    public void enviarHora() {
        try {
            HoraClienteDto dto = new HoraClienteDto(NOMBRE_NODO, Instant.now().toEpochMilli());
            String json = mapper.writeValueAsString(dto);
            template.convertAndSend("reloj.solicitud", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
