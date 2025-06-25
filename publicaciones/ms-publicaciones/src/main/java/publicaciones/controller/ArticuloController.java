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

import publicaciones.dto.ArticuloDto;
import publicaciones.dto.ResponseDto;
import publicaciones.service.ArticuloService;

@RestController
@RequestMapping("/api/articulos")
@CrossOrigin(origins = "*")
public class ArticuloController {
    
    @Autowired
    private ArticuloService articuloService;
    
    @GetMapping
    public ResponseEntity<List<ArticuloDto>> getAllArticulos() {
        List<ArticuloDto> articulos = articuloService.getAllArticulos();
        return ResponseEntity.ok(articulos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ArticuloDto> getArticuloById(@PathVariable Long id) {
        return articuloService.getArticuloById(id)
                .map(articulo -> ResponseEntity.ok(articulo))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<ArticuloDto>> getArticulosByAutor(@PathVariable Long autorId) {
        List<ArticuloDto> articulos = articuloService.getArticulosByAutor(autorId);
        return ResponseEntity.ok(articulos);
    }
    
    @GetMapping("/area/{areaInvestigacion}")
    public ResponseEntity<List<ArticuloDto>> getArticulosByArea(@PathVariable String areaInvestigacion) {
        List<ArticuloDto> articulos = articuloService.getArticulosByAreaInvestigacion(areaInvestigacion);
        return ResponseEntity.ok(articulos);
    }
    
    @GetMapping("/revista/{revista}")
    public ResponseEntity<List<ArticuloDto>> getArticulosByRevista(@PathVariable String revista) {
        List<ArticuloDto> articulos = articuloService.getArticulosByRevista(revista);
        return ResponseEntity.ok(articulos);
    }
    
    @PostMapping
    public ResponseEntity<ResponseDto> createArticulo(@RequestBody ArticuloDto articuloDto) {
        try {
            ArticuloDto createdArticulo = articuloService.createArticulo(articuloDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto("Artículo creado exitosamente", "ID: " + createdArticulo.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Error al crear artículo: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateArticulo(@PathVariable Long id, @RequestBody ArticuloDto articuloDto) {
        try {
            ArticuloDto updatedArticulo = articuloService.updateArticulo(id, articuloDto);
            return ResponseEntity.ok(new ResponseDto("Artículo actualizado exitosamente", "ID: " + updatedArticulo.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Error al actualizar artículo: " + e.getMessage(), null));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteArticulo(@PathVariable Long id) {
        try {
            articuloService.deleteArticulo(id);
            return ResponseEntity.ok(new ResponseDto("Artículo eliminado exitosamente", "ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Error al eliminar artículo: " + e.getMessage(), null));
        }
    }
}