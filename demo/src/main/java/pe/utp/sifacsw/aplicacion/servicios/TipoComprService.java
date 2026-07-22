package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.TipoComprobante;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;
import pe.utp.sifacsw.infraestructura.dao.DAOTipoComprobante;

import java.util.List;

public class TipoComprService {

    private final DAOTipoComprobante repository;

    public TipoComprService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getTipoComprobanteDAO();
    }

    public List<TipoComprobante> listarTipoComprobantes() {
        return repository.listaTipoComprobante();
    }
}

