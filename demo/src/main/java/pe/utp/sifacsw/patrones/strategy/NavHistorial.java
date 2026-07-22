package pe.utp.sifacsw.patrones.strategy;

import pe.utp.sifacsw.aplicacion.servicios.PermisosNavegacion;

public class NavHistorial implements NavegacionStrategy {
    private PermisosNavegacion fachada;

    public NavHistorial() {
        this.fachada = PermisosNavegacion.getInstance();
    }

    @Override
    public void navegar() throws java.io.IOException {
        fachada.navegarA("History");
    }

    @Override
    public boolean tienePermiso() {
        return fachada.getInstance().tienePermiso("History");
    }

    @Override
    public String getNombrePantalla() {
        return "Historial de Facturas";
    }

}

