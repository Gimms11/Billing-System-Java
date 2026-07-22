package pe.utp.sifacsw.infraestructura.dao;

import java.util.List;
import pe.utp.sifacsw.dominio.modelos.DetalleComprobante;

public interface DAODetalleComprobante {
    void registrarDetalleComprobante(DetalleComprobante detalle);
    void eliminarDetalleComprobante(DetalleComprobante detalle);
    List<DetalleComprobante> listarDetalleComprobante();
    List<DetalleComprobante> listarDetallesPorComprobante(Long idComprobante);
}

