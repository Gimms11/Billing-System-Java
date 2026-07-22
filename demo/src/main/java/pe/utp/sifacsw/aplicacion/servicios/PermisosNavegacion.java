package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.presentacion.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PermisosNavegacion {
    private static PermisosNavegacion instance;
    private AutenticacioService autenticacioService;
    private Map<String, String> permisosPantallas;

    private PermisosNavegacion() {
        this.autenticacioService = AutenticacioService.getInstance();
        inicializarPermisos();
    }

    public static PermisosNavegacion getInstance() {
        if (instance == null) {
            instance = new PermisosNavegacion();
        } else {
        }
        return instance;
    }

    private void inicializarPermisos() {
        permisosPantallas = new HashMap<>();
        permisosPantallas.put("GenClientes", "ADMIN,EMISOR");
        permisosPantallas.put("GenFactures", "ADMIN,EMISOR");
        permisosPantallas.put("GenProducts", "ADMIN,EMISOR");
        permisosPantallas.put("History", "ADMIN,EMISOR,LECTOR");
        permisosPantallas.put("Configurations", "ADMIN");
        permisosPantallas.put("Dashboard", "ADMIN,EMISOR,LECTOR");
    }

    public void navegarA(String pantalla) throws IOException {

        if (!tienePermiso(pantalla)) {
            throw new SecurityException("Acceso denegado a la pantalla: " + pantalla);
        }
        App.setRoot(pantalla);
    }

    public boolean tienePermiso(String pantalla) {
        String rolesPermitidos = permisosPantallas.get(pantalla);
        if (rolesPermitidos == null) {
            return false; // Si no hay restricciones, permitir acceso
        }
        String[] roles = rolesPermitidos.split(",");
        return autenticacioService.tienePermiso(roles);
    }
}

