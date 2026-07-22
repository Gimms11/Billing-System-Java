package pe.utp.sifacsw.patrones.strategy;

import java.io.IOException;

import pe.utp.sifacsw.aplicacion.servicios.PermisosNavegacion;

public class NavClientes implements NavegacionStrategy {

    private PermisosNavegacion fachada;

    public NavClientes() {
        this.fachada = PermisosNavegacion.getInstance();
    }

    @Override
    public void navegar() throws IOException {
        fachada.navegarA("GenClientes");
    }

    @Override
    public boolean tienePermiso() {
        return fachada.getInstance().tienePermiso("GenClientes");
    }

    @Override
    public String getNombrePantalla() {
        return "Gestion de Clientes";
    }
}

