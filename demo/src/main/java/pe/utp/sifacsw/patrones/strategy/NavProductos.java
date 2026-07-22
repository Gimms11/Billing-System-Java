package pe.utp.sifacsw.patrones.strategy;

import pe.utp.sifacsw.aplicacion.servicios.PermisosNavegacion;

public class NavProductos implements NavegacionStrategy {
    private PermisosNavegacion fachada;

    public NavProductos() {
        this.fachada = PermisosNavegacion.getInstance();
    }

    @Override
    public void navegar() throws java.io.IOException {
        fachada.navegarA("GenProducts");
    }

    @Override
    public boolean tienePermiso() {
        return fachada.getInstance().tienePermiso("GenProducts");
    }

    @Override
    public String getNombrePantalla() {
        return "Generacion de Productos";
    }

}

