package pe.utp.sifacsw.patrones.strategy;

import pe.utp.sifacsw.aplicacion.servicios.PermisosNavegacion;

public class NavDashboard implements NavegacionStrategy {
    private PermisosNavegacion fachada;

    public NavDashboard() {
        this.fachada = PermisosNavegacion.getInstance();
    }

    @Override
    public void navegar() throws java.io.IOException {
        fachada.navegarA("Dashboard");
    }

    @Override
    public boolean tienePermiso() {
        return fachada.getInstance().tienePermiso("Dashboard");
    }

    @Override
    public String getNombrePantalla() {
        return "Dashboard";
    }

}

