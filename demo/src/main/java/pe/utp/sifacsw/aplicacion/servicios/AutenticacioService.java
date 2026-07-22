package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.Usuario;

public class AutenticacioService {
    private static AutenticacioService instance;
    private Usuario usuarioActual;
    private ManejadorService manejadorService;
    
    private AutenticacioService() {
        this.manejadorService = new ManejadorService();
        cargarUsuarioDeSesion();
    }
    
    public static AutenticacioService getInstance() {
        if (instance == null) {
            instance = new AutenticacioService();
        }
        return instance;
    }
    
    private void cargarUsuarioDeSesion() {
        try {
            Usuario usuarioSesion = manejadorService.leerUsuario();
            if (usuarioSesion != null && 
                usuarioSesion.getUsername() != null && 
                !usuarioSesion.getUsername().trim().isEmpty() &&
                usuarioSesion.getRol() != null) {
                this.usuarioActual = usuarioSesion;
            }
        } catch (Exception e) {
            this.usuarioActual = null;
        }
    }
    
    public void establecerUsuarioAutenticado(Usuario usuario) {
        this.usuarioActual = usuario;
        manejadorService.guardarUsuario(usuario);
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public boolean esAdmin() {
        return usuarioActual != null && "ADMIN".equalsIgnoreCase(usuarioActual.getRol());
    }
    
    public boolean esEmisor() {
        return usuarioActual != null && "EMISOR".equalsIgnoreCase(usuarioActual.getRol());
    }
    
    public boolean esLector() {
        return usuarioActual != null && "LECTOR".equalsIgnoreCase(usuarioActual.getRol());
    }
    
    public boolean tienePermiso(String... rolesRequeridos) {
        if (usuarioActual == null) return false;
        for (String rol : rolesRequeridos) {
            if (rol.equalsIgnoreCase(usuarioActual.getRol())) {
                return true;
            }
        }
        return false;
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    
}
