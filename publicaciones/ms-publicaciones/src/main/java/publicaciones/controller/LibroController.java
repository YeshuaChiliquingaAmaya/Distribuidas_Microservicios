package publicaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import publicaciones.dto.LibroDto;
import publicaciones.dto.ResponseDto;
import publicaciones.service.LibroService;
import publicaciones.service.NotificacionProducer; // ➕ Importación añadida

@RestController
@RequestMapping("/demo/libros")
@CrossOrigin(origins = "*")
public class LibroController {
    
    @Autowired
    private LibroService libroService;

    @Autowired
    private NotificacionProducer producer; // ➕ Inyección del Producer

    @GetMapping
    public ResponseEntity<List<LibroDto>> getAllLibros() {
        List<LibroDto> libros = libroService.getAllLibros();
        return ResponseEntity.ok(libros);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LibroDto> getLibroById(@PathVariable Long id) {
        return libroService.getLibroById(id)
                .map(libro -> ResponseEntity.ok(libro))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<LibroDto>> getLibrosByAutor(@PathVariable Long autorId) {
        List<LibroDto> libros = libroService.getLibrosByAutor(autorId);
        return ResponseEntity.ok(libros);
    }
    
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LibroDto>> getLibrosByGenero(@PathVariable String genero) {
        List<LibroDto> libros = libroService.getLibrosByGenero(genero);
        return ResponseEntity.ok(libros);
    }
    
    @PostMapping
    public ResponseEntity<ResponseDto> createLibro(@RequestBody LibroDto libroDto) {
        try {
            LibroDto createdLibro = libroService.createLibro(libroDto);

            // ➕ Mensaje a cola.notificaciones
            producer.enviarNotificacion("📘 Nuevo libro creado: " + createdLibro.getTitulo(), "LIBRO");

            // ➕ Mensaje a catalogo.cola
            producer.enviarCatalogo(createdLibro);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto("Libro creado exitosamente", "ID: " + createdLibro.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Error al crear libro: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateLibro(@PathVariable Long id, @RequestBody LibroDto libroDto) {
        try {
            LibroDto updatedLibro = libroService.updateLibro(id, libroDto);
            return ResponseEntity.ok(new ResponseDto("Libro actualizado exitosamente", "ID: " + updatedLibro.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Error al actualizar libro: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteLibro(@PathVariable Long id) {
        try {
            libroService.deleteLibro(id);
            return ResponseEntity.ok(new ResponseDto("Libro eliminado exitosamente", "ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Error al eliminar libro: " + e.getMessage(), null));
        }
    }
}
