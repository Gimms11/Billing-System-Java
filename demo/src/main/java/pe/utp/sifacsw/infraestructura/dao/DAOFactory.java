package pe.utp.sifacsw.infraestructura.dao;

import pe.utp.sifacsw.infraestructura.persistencia.PostgresDAOFactory;

/**
 * PATRÓN ABSTRACT FACTORY (Fábrica Abstracta)
 * 
 * Esta clase define una interfaz para crear familias de objetos relacionados
 * (DAOs)
 * sin especificar sus clases concretas. Permite cambiar entre diferentes
 * implementaciones
 * de base de datos (PostgreSQL, MySQL, etc.) sin modificar el código cliente.
 * 
 * Ventajas:
 * - Aislamiento de clases concretas: El cliente no conoce las implementaciones
 * específicas
 * - Facilita el cambio de familia de productos: Cambiar de BD solo requiere
 * cambiar la factory
 * - Promueve consistencia: Todos los DAOs provienen de la misma familia (misma
 * BD)
 * 
 * Ejemplo de uso:
 * DAOFactory factory = DAOFactory.getDAOFactory();
 * DAOCliente daoCliente = factory.getClienteDAO();
 */
public abstract class DAOFactory {

    // Tipos de bases de datos soportadas
    public static final int POSTGRES = 1;
    // ... otros tipos de bases de datos

    // Método para obtener la instancia de Factory
    public static DAOFactory getDAOFactory(int whichFactory) {

        switch (whichFactory) {
            case POSTGRES:
                return new PostgresDAOFactory();
            default:
                return null;
        }
    }

    // Método por defecto que retorna PostgresDAOFactory
    public static DAOFactory getDAOFactory() {
        return getDAOFactory(POSTGRES);
    }

    public abstract DAOUbigeo getUbigeoDAO();

    public abstract DAOCategoriaProducto getCategoriaProductoDAO();

    public abstract DAOTipoAfectacion getAfectacionDAO();

    public abstract DAOTipoDocumento getTipoDocumentoDAO();

    public abstract DAOTipoComprobante getTipoComprobanteDAO();

    public abstract DAOProducto getProductoDAO();

    public abstract DAOCliente getClienteDAO();

    public abstract DAOComprobante getComprobanteDAO();

    public abstract DAOMedioPago getMedioPagoDAO();
}

