package pe.utp.sifacsw.patrones.strategy;

import pe.utp.sifacsw.aplicacion.servicios.PermisosNavegacion;

public class NavFacturacion implements NavegacionStrategy {
    private PermisosNavegacion fachada;

    public NavFacturacion() {
        this.fachada = PermisosNavegacion.getInstance();
    }

    @Override
    public void navegar() throws java.io.IOException {
        fachada.navegarA("GenFactures");
    }

    @Override
    public boolean tienePermiso() {
        return fachada.getInstance().tienePermiso("GenFactures");
    }

    @Override
    public String getNombrePantalla() {
        return "Generacion de Facturas";
    }

}

