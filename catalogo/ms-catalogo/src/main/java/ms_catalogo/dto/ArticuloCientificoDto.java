package ms_catalogo.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class ArticuloCientificoDto {
    private int orcid;
    private Date fechaPublicacion;
    private String revista;
    private String areaInvestigacion;
    private String titulo;
    private int anioPublicacion;
    private String resumen;
    private String editorial;
    private String isbn;
}
