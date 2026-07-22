package pe.utp.sifacsw.infraestructura.dao;

import pe.utp.sifacsw.dominio.modelos.TipoDocumento;
import java.util.List;

public interface DAOTipoDocumento {
    List<TipoDocumento> obtenerTodos();
    TipoDocumento obtener(Long id);
}
