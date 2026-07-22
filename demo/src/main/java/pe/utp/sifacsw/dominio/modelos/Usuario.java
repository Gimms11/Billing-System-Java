package pe.utp.sifacsw.dominio.modelos;

public class Usuario {
    private Long idUsuario;
    private String username;      // Nombre de usuario (único)
    private String password;      // Contraseña (almacenada como hash, ¡nunca en texto plano!)
    private String rol;           // Ej: "ADMIN", "EMISOR", "LECTOR"

    // Constructor vacío (requerido por frameworks como JPA)
    public Usuario() {}

    // Constructor con campos esenciales
    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Constructor completo
    public Usuario(Long idUsuario, String username, String password, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters
    public Long getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRol() { return rol; }

    // Setters
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(String rol) { this.rol = rol; }
}
