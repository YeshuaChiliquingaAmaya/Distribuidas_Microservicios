package ms_catalogo.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import ms_catalogo.dto.ArticuloCientificoDto;
import ms_catalogo.dto.LibroDto;
import ms_catalogo.service.CatalogoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CatalogoListener {

    @Autowired
    private CatalogoService service;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "catalogo.cola")
    public void recibirMensaje(String mensaje) {
        try {
            if (mensaje.contains("numeroPaginas")) {
                LibroDto libro = mapper.readValue(mensaje, LibroDto.class);
                service.registrarLibro(libro);
            } else if (mensaje.contains("areaInvestigacion")) {
                ArticuloCientificoDto articulo = mapper.readValue(mensaje, ArticuloCientificoDto.class);
                service.registrarArticulo(articulo);
            } else {
                System.out.println("Mensaje no reconocido: " + mensaje);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
