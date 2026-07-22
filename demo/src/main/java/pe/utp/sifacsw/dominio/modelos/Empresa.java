package pe.utp.sifacsw.dominio.modelos;

public class Empresa {
    private Long idEmpresa;
    private String ruc;               // RUC, NIT, RFC, etc. (identificador tributario)
    private String nombre;            // Razón social o nombre comercial
    private String correo;            // Correo corporativo
    private String direccion;         // Dirección fiscal
    private String telefono;          // Teléfono de contacto
    private String pais;              // País de operación
    private String descripcion;       // Descripción breve (opcional)

    // Constructor vacío
    public Empresa() {}

    // Constructor con campos esenciales
    public Empresa(String ruc, String nombre, String correo, String direccion) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
    }

    // Constructor completo
    public Empresa(Long idEmpresa, String ruc, String nombre, String correo,
                   String direccion, String telefono, String pais, String descripcion) {
        this.idEmpresa = idEmpresa;
        this.ruc = ruc;
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.pais = pais;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getIdEmpresa() { return idEmpresa; }
    public String getRuc() { return ruc; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getPais() { return pais; }
    public String getDescripcion() { return descripcion; }

    // Setters
    public void setIdEmpresa(Long idEmpresa) { this.idEmpresa = idEmpresa; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setPais(String pais) { this.pais = pais; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
