package pe.utp.sifacsw.infraestructura.persistencia;

import pe.utp.sifacsw.dominio.modelos.CategoriaProductos;
import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOCategoriaProducto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCategoriaProductoImpl implements DAOCategoriaProducto {

    @Override
    public List<CategoriaProductos> obtenerTodas() {
        List<CategoriaProductos> lista = new ArrayList<>();
        String sql = "SELECT idcategoria, nombre, descripcion FROM tipocategoria";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoriaProductos cat = new CategoriaProductos();
                cat.setIdCategoria(rs.getLong("idcategoria"));
                cat.setNombreCategoria(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                lista.add(cat);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener las categorías: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public CategoriaProductos obtener(Long id) {
        CategoriaProductos categoria = null;
        String sql = "SELECT idcategoria, nombre, descripcion FROM tipocategoria WHERE idcategoria = ?";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoria = new CategoriaProductos();
                    categoria.setIdCategoria(rs.getLong("idcategoria"));
                    categoria.setNombreCategoria(rs.getString("nombre"));
                    categoria.setDescripcion(rs.getString("descripcion"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener la categoría: " + e.getMessage());
        }

        return categoria;
    }
}

