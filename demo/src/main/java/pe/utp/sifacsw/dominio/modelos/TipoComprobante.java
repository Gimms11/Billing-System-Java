package pe.utp.sifacsw.dominio.modelos;

public class TipoComprobante {
    private Long idTipoComprobante;
    private String nombreTipo;      // Ej: "Factura", "Boleta", "Nota de Crédito"
    private String descripcion;

    public TipoComprobante() {}

    public TipoComprobante(Long idTipoComprobante, String nombreTipo, String descripcion) {
        this.idTipoComprobante = idTipoComprobante;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getIdTipoComprobante() { return idTipoComprobante; }
    public String getNombreTipo() { return nombreTipo; }
    public String getDescripcion() { return descripcion; }

    // Setters
    public void setIdTipoComprobante(Long idTipoComprobante) { this.idTipoComprobante = idTipoComprobante; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreTipo; // Mostrar solo el nombre en el ComboBox
    }
}

