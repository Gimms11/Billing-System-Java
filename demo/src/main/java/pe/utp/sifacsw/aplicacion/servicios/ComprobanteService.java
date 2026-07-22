package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.Comprobante;
import pe.utp.sifacsw.infraestructura.dao.DAOComprobante;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;
import java.util.List;
import java.util.ArrayList;

public class ComprobanteService {

    private final DAOComprobante repository;

    public ComprobanteService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getComprobanteDAO();
    }

    public List<Comprobante> listarComprobante() {
        try {
            List<Comprobante> comprobantes = repository.listarComprobante();
            return comprobantes != null ? comprobantes : new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar comprobantes: " + e.getMessage(), e);
        }
    }

    public List<Comprobante> listarComprobanteFiltro(Integer index, String numDocumentoCliente, String numSerie) {
        try {
            List<Comprobante> comprobantes = repository.filtrarComprobantes(index, numDocumentoCliente, numSerie);
            return comprobantes != null ? comprobantes : new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar comprobantes del día: " + e.getMessage(), e);
        }
    }

    public void subirComprobante(Comprobante comp) {
        if (comp == null) {
            throw new IllegalArgumentException("El comprobante no puede ser nulo");
        }
        try {
            repository.registarComprobante(comp);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar comprobante: " + e.getMessage(), e);
        }
    }
}

