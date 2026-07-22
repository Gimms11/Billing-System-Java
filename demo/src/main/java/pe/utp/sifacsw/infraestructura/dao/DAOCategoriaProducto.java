package pe.utp.sifacsw.infraestructura.dao;

import pe.utp.sifacsw.dominio.modelos.CategoriaProductos;
import java.util.List;

public interface DAOCategoriaProducto {
    List<CategoriaProductos> obtenerTodas();
    CategoriaProductos obtener(Long id);
}
