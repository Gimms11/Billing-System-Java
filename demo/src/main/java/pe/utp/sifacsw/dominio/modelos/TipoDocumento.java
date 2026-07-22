package pe.utp.sifacsw.dominio.modelos;

public class TipoDocumento {
    private Long idDocumento;
    private String nombreDocumento; // Ej: "DNI", "RUC", "Pasaporte"
    private String descripcion;

    public TipoDocumento() {}

    public TipoDocumento(Long idDocumento, String nombreDocumento, String descripcion) {
        this.idDocumento = idDocumento;
        this.nombreDocumento = nombreDocumento;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getIdDocumento() { return idDocumento; }
    public String getNombreDocumento() { return nombreDocumento; }
    public String getDescripcion() { return descripcion; }

    // Setters
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }
    public void setNombreDocumento(String nombreDocumento) { this.nombreDocumento = nombreDocumento; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreDocumento; // Mostrar solo el nombre en el ComboBox
    }
}
