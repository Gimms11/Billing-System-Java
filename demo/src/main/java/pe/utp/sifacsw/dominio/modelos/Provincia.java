package pe.utp.sifacsw.dominio.modelos;

public class Provincia {
    private Long idProvincia;
    private String nombreProvincia;   // Ej: "Lima", "Callao"
    private Long idDepartamento;      // FK → Departamento

    public Provincia() {}

    public Provincia(Long idProvincia, String nombreProvincia, Long idDepartamento) {
        this.idProvincia = idProvincia;
        this.nombreProvincia = nombreProvincia;
        this.idDepartamento = idDepartamento;
    }

    // Getters y setters
    public Long getIdProvincia() { return idProvincia; }
    public void setIdProvincia(Long idProvincia) { this.idProvincia = idProvincia; }

    public String getNombreProvincia() { return nombreProvincia; }
    public void setNombreProvincia(String nombreProvincia) { this.nombreProvincia = nombreProvincia; }

    public Long getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(Long idDepartamento) { this.idDepartamento = idDepartamento; }

    @Override
    public String toString() {
        return nombreProvincia;
    }
}
