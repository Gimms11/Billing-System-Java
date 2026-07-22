package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.CategoriaProductos;
import pe.utp.sifacsw.infraestructura.dao.DAOCategoriaProducto;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;

import java.util.List;

public class CategoriaService {

    private final DAOCategoriaProducto repository;

    public CategoriaService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getCategoriaProductoDAO();
    }

    public List<CategoriaProductos> listarCategorias() {
        return repository.obtenerTodas();
    }

    public CategoriaProductos obtenerCategoria(Long id) {
        if (id == null) return null;
        return repository.obtener(id);
    }
}
