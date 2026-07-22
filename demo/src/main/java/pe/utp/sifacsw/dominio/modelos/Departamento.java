package pe.utp.sifacsw.dominio.modelos;

public class Departamento {
    private Long idDepartamento;
    private String nombreDepartamento; // Ej: "Lima", "Arequipa", "Cusco"

    public Departamento() {}

    public Departamento(Long idDepartamento, String nombreDepartamento) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
    }

    // Getters y setters
    public Long getIdDepartamento() { return idDepartamento; }
    public void setIdDepartamento(Long idDepartamento) { this.idDepartamento = idDepartamento; }

    public String getNombreDepartamento() { return nombreDepartamento; }
    public void setNombreDepartamento(String nombreDepartamento) { this.nombreDepartamento = nombreDepartamento; }

    @Override
    public String toString() {
        return nombreDepartamento;
    }
}
