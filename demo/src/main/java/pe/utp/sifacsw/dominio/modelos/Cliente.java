package pe.utp.sifacsw.dominio.modelos;

public class Cliente {
    private Long idCliente;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String correo;
    private String direccion;
    private String numDocumento;
    private Long idDistrito;
    private Long idDocumento;

    // Campos adicionales para nombres
    private String nombreDistrito;
    private String nombreTipoDocumento;

    // ✅ Constructor vacío (necesario para frameworks y JavaFX)
    public Cliente() {}

    // ✅ Constructor completo
    public Cliente(Long idCliente, String nombres, String apellidos, String telefono,
                   String correo, String direccion, String numDocumento,
                   Long idDistrito, Long idDocumento, String nombreDistrito, String nombreTipoDocumento) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.numDocumento = numDocumento;
        this.idDistrito = idDistrito;
        this.idDocumento = idDocumento;
    }

    // ✅ Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getNumDocumento() { return numDocumento; }
    public void setNumDocumento(String numDocumento) { this.numDocumento = numDocumento; }

    public Long getIdDistrito() { return idDistrito; }
    public void setIdDistrito(Long idDistrito) { this.idDistrito = idDistrito; }

    public Long getIdDocumento() { return idDocumento; }
    public void setIdDocumento(Long idDocumento) { this.idDocumento = idDocumento; }

    // Getters y Setters para los campos adicionales
    public String getNombreDistrito() { return nombreDistrito; }
    public void setNombreDistrito(String nombreDistrito) { this.nombreDistrito = nombreDistrito; }
    public String getNombreTipoDocumento() { return nombreTipoDocumento; }
    public void setNombreTipoDocumento(String nombreTipoDocumento) { this.nombreTipoDocumento = nombreTipoDocumento; }

    @Override
    public String toString() {
        return nombres + " " + apellidos;
    }
}

