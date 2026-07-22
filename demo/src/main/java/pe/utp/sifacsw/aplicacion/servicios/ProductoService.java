package pe.utp.sifacsw.aplicacion.servicios;

import java.sql.SQLException;
import java.util.List;

import pe.utp.sifacsw.dominio.modelos.Producto;
import pe.utp.sifacsw.infraestructura.dao.DAOProducto;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;

public class ProductoService {
    private final DAOProducto repository;
    
    public ProductoService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getProductoDAO();
    }

    public List<Producto> obtenerTodos() {
        try {
            List<Producto> productos = repository.listarProducto();
            return productos != null ? productos : java.util.Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener productos: " + e.getMessage());
        }
    }

    public void registrarProducto(Producto produc) throws RuntimeException {
        try {
            repository.registarProducto(produc);
        } catch (SQLException e) {
            // El mensaje de error ya viene formateado del DAO
            String mensajeError = "Error al registrar producto:\n" + e.getMessage();
            // Dividir el mensaje en líneas más cortas si es necesario
            if (mensajeError.length() > 50) {
                mensajeError = mensajeError.replaceAll("(.{45,}?)\\s", "$1\n");
            }
            throw new RuntimeException(mensajeError);
        } catch (Exception e) {
            e.printStackTrace();
            String mensajeError = "Error inesperado al registrar\n" +
                                "el producto. Por favor,\n" +
                                "intente nuevamente.";
            throw new RuntimeException(mensajeError);
        }
    }

    public List<Producto> filtrarProductos(String nombre, Double precio, Integer stock, String unidad,
    Integer idAfectacion, Integer idCategoria) {
        try {
            List<Producto> productos = repository.filtrarProductos(nombre, precio, stock, unidad, idCategoria, idAfectacion);
            return productos != null ? productos : java.util.Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener productos: " + e.getMessage());
        }
    }

    public Producto obteneProducto(Long id){
        return repository.buscarProducto(id);
    }

    public void actualizarProducto(Producto producto){
        repository.actualizarProducto(producto);
    }
}

