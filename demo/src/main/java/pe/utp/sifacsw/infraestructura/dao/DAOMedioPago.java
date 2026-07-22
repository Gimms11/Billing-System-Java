package pe.utp.sifacsw.infraestructura.dao;

import pe.utp.sifacsw.dominio.modelos.MedioPago;
import java.util.List;

public interface DAOMedioPago {
    List<MedioPago> obtenerTodos();
}
