package pe.utp.sifacsw.infraestructura.dao;

import java.util.List;
import pe.utp.sifacsw.dominio.modelos.Distrito;

public interface DAODistrito {
    List<Distrito> listarDistritos();
    Distrito obtenerDistrito(Long id);
    String obtenerNombreDistrito(Long id);
}
