package pe.utp.sifacsw.dominio.modelos;

public class MedioPago {
    private Long idMedioPago;
    private String nombreMedioPago; // Ej: "Efectivo", "Tarjeta Visa", "Transferencia"
    private String descripcion;

    public MedioPago() {}

    public MedioPago(Long idMedioPago, String nombreMedioPago, String descripcion) {
        this.idMedioPago = idMedioPago;
        this.nombreMedioPago = nombreMedioPago;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getIdMedioPago() { return idMedioPago; }
    public String getNombreMedioPago() { return nombreMedioPago; }
    public String getDescripcion() { return descripcion; }

    // Setters
    public void setIdMedioPago(Long idMedioPago) { this.idMedioPago = idMedioPago; }
    public void setNombreMedioPago(String nombreMedioPago) { this.nombreMedioPago = nombreMedioPago; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreMedioPago;
    }
}
