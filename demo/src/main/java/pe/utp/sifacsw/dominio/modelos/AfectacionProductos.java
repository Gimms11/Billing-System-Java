package pe.utp.sifacsw.dominio.modelos;

public class AfectacionProductos {
    private Long idAfectacion;
    private String nombreAfectacion; // Ej: "Gravado", "Exonerado", "Inafecto"
    private String descripcion;

    public AfectacionProductos() {}

    public AfectacionProductos(Long idAfectacion, String nombreAfectacion, String descripcion) {
        this.idAfectacion = idAfectacion;
        this.nombreAfectacion = nombreAfectacion;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getIdAfectacion() { return idAfectacion; }
    public String getNombreAfectacion() { return nombreAfectacion; }
    public String getDescripcion() { return descripcion; }

    // Setters
    public void setIdAfectacion(Long idAfectacion) { this.idAfectacion = idAfectacion; }
    public void setNombreAfectacion(String nombreAfectacion) { this.nombreAfectacion = nombreAfectacion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreAfectacion;
    }
}

