package pe.utp.sifacsw.dominio.modelos;

public class Distrito {
    private Long idDistrito;
    private String nombreDistrito;   // Ej: "San Isidro", "Miraflores"
    private Long idProvincia;        // FK → Provincia

    public Distrito() {}

    public Distrito(Long idDistrito, String nombreDistrito, Long idProvincia) {
        this.idDistrito = idDistrito;
        this.nombreDistrito = nombreDistrito;
        this.idProvincia = idProvincia;
    }

    // Getters y setters
    public Long getIdDistrito() { return idDistrito; }
    public void setIdDistrito(Long idDistrito) { this.idDistrito = idDistrito; }

    public String getNombreDistrito() { return nombreDistrito; }
    public void setNombreDistrito(String nombreDistrito) { this.nombreDistrito = nombreDistrito; }

    public Long getIdProvincia() { return idProvincia; }
    public void setIdProvincia(Long idProvincia) { this.idProvincia = idProvincia; }

    @Override
    public String toString() {
        return nombreDistrito;
    }
}
