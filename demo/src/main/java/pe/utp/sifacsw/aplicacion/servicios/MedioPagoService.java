package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.MedioPago;
import pe.utp.sifacsw.infraestructura.dao.DAOMedioPago;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;
import java.util.List;

public class MedioPagoService {

    private final DAOMedioPago repository;

    public MedioPagoService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getMedioPagoDAO();
    }

    public List<MedioPago> listarMedioPagos() {
        return repository.obtenerTodos();
    }
}

