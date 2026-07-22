package pe.utp.sifacsw.infraestructura.dao;

import pe.utp.sifacsw.dominio.modelos.AfectacionProductos;
import java.util.List;

public interface DAOTipoAfectacion {
    List<AfectacionProductos> obtenerTodas();
    AfectacionProductos obtener(Long id);
}
