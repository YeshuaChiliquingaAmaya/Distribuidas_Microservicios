package publicaciones.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import publicaciones.dto.AutorDto;
import publicaciones.model.Autor;
import publicaciones.repository.AutorRepository;

@Service
public class AutorService {
    
    @Autowired
    private AutorRepository autorRepository;
    
    @Autowired
    private NotificacionProducer producer;
    
    public List<AutorDto> getAllAutores() {
        return autorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<AutorDto> getAutorById(Long id) {
        return autorRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public AutorDto createAutor(AutorDto autorDto) {
        if (autorRepository.existsByEmail(autorDto.getEmail())) {
            throw new RuntimeException("Ya existe un autor con este email");
        }
        if (autorRepository.existsByOrcid(autorDto.getOrcid())) {
            throw new RuntimeException("Ya existe un autor con este ORCID");
        }
        Autor autor = convertToEntity(autorDto);
        Autor savedAutor = autorRepository.save(autor);
        
        // Enviar notificación usando tu NotificacionProducer
        try {
            producer.enviarNotificacion(
                "Nuevo autor registrado: " + savedAutor.getNombre() + " " + savedAutor.getApellido(),
                "nuevo-autor"
            );
        } catch (Exception e) {
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
        
        return convertToDto(savedAutor);
    }
    
    public AutorDto updateAutor(Long id, AutorDto autorDto) {
        return autorRepository.findById(id)
                .map(autor -> {
                    autor.setNombre(autorDto.getNombre());
                    autor.setApellido(autorDto.getApellido());
                    autor.setEmail(autorDto.getEmail());
                    autor.setOrcid(autorDto.getOrcid());
                    autor.setNacionalidad(autorDto.getNacionalidad());
                    autor.setTelefono(autorDto.getTelefono());
                    autor.setInstitucion(autorDto.getInstitucion());
                    return convertToDto(autorRepository.save(autor));
                })
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));
    }

    public void deleteAutor(Long id) {
        autorRepository.deleteById(id);
    }
    
    private AutorDto convertToDto(Autor autor) {
        return new AutorDto(
                autor.getId(),
                autor.getNombre(),
                autor.getApellido(),
                autor.getEmail(),
                autor.getOrcid(),
                autor.getNacionalidad(),
                autor.getTelefono(),
                autor.getInstitucion()
        );
    }
    
    private Autor convertToEntity(AutorDto autorDto) {
        Autor autor = new Autor();
        autor.setNombre(autorDto.getNombre());
        autor.setApellido(autorDto.getApellido());
        autor.setEmail(autorDto.getEmail());
        autor.setOrcid(autorDto.getOrcid());
        autor.setNacionalidad(autorDto.getNacionalidad());
        autor.setTelefono(autorDto.getTelefono());
        autor.setInstitucion(autorDto.getInstitucion());
        return autor;
    }
}