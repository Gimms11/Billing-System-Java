package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.AfectacionProductos;
import pe.utp.sifacsw.infraestructura.dao.DAOTipoAfectacion;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;
import java.util.List;

public class AfectacionService {

    private final DAOTipoAfectacion repository;

    public AfectacionService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getAfectacionDAO();
    }

    public List<AfectacionProductos> listarAfectaciones() {
        return repository.obtenerTodas();
    }

    public AfectacionProductos obtenerAfectacion(Long id) {
        if (id == null) return null;
        return repository.obtener(id);
    }
}

