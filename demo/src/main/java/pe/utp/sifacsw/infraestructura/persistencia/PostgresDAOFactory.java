package pe.utp.sifacsw.infraestructura.persistencia;

import pe.utp.sifacsw.infraestructura.dao.*;

public class PostgresDAOFactory extends DAOFactory {

    @Override
    public DAOUbigeo getUbigeoDAO() {
        return new DAOUbigeoImpl();
    }

    @Override
    public DAOCategoriaProducto getCategoriaProductoDAO() {
        return new DAOCategoriaProductoImpl();
    }

    @Override
    public DAOTipoDocumento getTipoDocumentoDAO() {
        return new DAOTipoDocumentoImpl();
    }

    @Override
    public DAOProducto getProductoDAO() {
        return new DAOProductoImpl();
    }

    @Override
    public DAOCliente getClienteDAO() {
        return new DAOClienteImpl();
    }

    @Override
    public DAOComprobante getComprobanteDAO() {
        return new DAOComprobanteImpl();
    }

    @Override
    public DAOTipoAfectacion getAfectacionDAO() {
        return new DAOTipoAfectacionImpl();
    }

    @Override
    public DAOMedioPago getMedioPagoDAO() {
        return new DAOMedioPagoImpl();
    }

    @Override
    public DAOTipoComprobante getTipoComprobanteDAO() {
        return new DAOTipoComprobanteImpl();
    }
}

