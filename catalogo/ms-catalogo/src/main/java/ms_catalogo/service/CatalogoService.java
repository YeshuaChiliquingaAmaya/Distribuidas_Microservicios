package ms_catalogo.service;

import ms_catalogo.dto.ArticuloCientificoDto;
import ms_catalogo.dto.LibroDto;
import ms_catalogo.entity.Catalogo;
import ms_catalogo.repository.CatalogoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogoService {

    @Autowired
    private CatalogoRepository repository;

    public void registrarLibro(LibroDto dto) {
        Catalogo c = new Catalogo();
        c.setTipo("LIBRO");
        c.setTitulo(dto.getTitulo());
        c.setEditorial(dto.getEditorial());
        c.setAnioPublicacion(dto.getAnioPublicacion());
        c.setResumen(dto.getResumen());
        c.setIsbn(dto.getIsbn());
        repository.save(c);
    }

    public void registrarArticulo(ArticuloCientificoDto dto) {
        Catalogo c = new Catalogo();
        c.setTipo("PAPER");
        c.setTitulo(dto.getTitulo());
        c.setEditorial(dto.getEditorial());
        c.setAnioPublicacion(dto.getAnioPublicacion());
        c.setResumen(dto.getResumen());
        c.setIsbn(dto.getIsbn());
        repository.save(c);
    }
}
