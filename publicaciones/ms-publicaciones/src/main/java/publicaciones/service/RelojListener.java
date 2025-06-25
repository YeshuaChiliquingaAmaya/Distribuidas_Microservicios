package publicaciones.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import publicaciones.dto.HoraClienteDto;
import publicaciones.dto.HoraServidorDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RelojListener {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RabbitTemplate template;

    private static final String NOMBRE_NODO = "ms-publicaciones";

    @RabbitListener(queues = "reloj.solicitud")
    public void recibirHora(String mensaje) {
        try {
            HoraClienteDto recibido = mapper.readValue(mensaje, HoraClienteDto.class);

            HoraServidorDto respuesta = new HoraServidorDto(
                NOMBRE_NODO,
                recibido.getHoraEnviada(),
                System.currentTimeMillis()
            );

            String json = mapper.writeValueAsString(respuesta);
            template.convertAndSend("reloj.respuesta", json);

            System.out.println(" Recibido de " + recibido.getNombreNodo() + " - reenviando hora servidor");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
