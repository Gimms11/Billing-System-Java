package pe.utp.sifacsw.dominio.modelos;

import java.math.BigDecimal;

public class TipoImpuestos {
    private Long idImpuesto;
    private String nombreImpuesto;      // Ej: "IGV", "ISC"
    private BigDecimal porcentajeImpuesto; // Ej: 0.18 (18%)

    public TipoImpuestos() {}

    public TipoImpuestos(Long idImpuesto, String nombreImpuesto, BigDecimal porcentajeImpuesto) {
        this.idImpuesto = idImpuesto;
        this.nombreImpuesto = nombreImpuesto;
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    // Getters
    public Long getIdImpuesto() { return idImpuesto; }
    public String getNombreImpuesto() { return nombreImpuesto; }
    public BigDecimal getPorcentajeImpuesto() { return porcentajeImpuesto; }

    // Setters
    public void setIdImpuesto(Long idImpuesto) { this.idImpuesto = idImpuesto; }
    public void setNombreImpuesto(String nombreImpuesto) { this.nombreImpuesto = nombreImpuesto; }
    public void setPorcentajeImpuesto(BigDecimal porcentajeImpuesto) {
        this.porcentajeImpuesto = porcentajeImpuesto;
    }
}
