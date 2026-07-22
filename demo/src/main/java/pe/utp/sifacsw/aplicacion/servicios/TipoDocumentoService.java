package pe.utp.sifacsw.aplicacion.servicios;

import java.util.List;

import pe.utp.sifacsw.dominio.modelos.TipoDocumento;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;
import pe.utp.sifacsw.infraestructura.dao.DAOTipoDocumento;

public class TipoDocumentoService {
    private final DAOTipoDocumento repository;

    public TipoDocumentoService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getTipoDocumentoDAO();
    }

    public List<TipoDocumento> cargarTipoDocumentos() {
        return repository.obtenerTodos();
    }

    public TipoDocumento obtenerTipoDocumento(Long id) {
        if (id == null) return null;
        return repository.obtener(id);
    }
}

