package ms_catalogo.dto;

import lombok.Data;

@Data
public class LibroDto {

    private String titulo;
    private int anioPublicacion;
    private String editorial;
    private String isbn;
    private String resumen;
    private String genero;
    private int numeroPaginas;
}
