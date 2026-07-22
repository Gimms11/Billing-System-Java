package pe.utp.sifacsw.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOProducto;
import pe.utp.sifacsw.dominio.modelos.Producto;

public class DAOProductoImpl implements DAOProducto {

    // -----------------------------
    // 🔹 CONSULTAS SQL BASE
    // -----------------------------
    private static final String SQLregistrar = 
        "INSERT INTO producto (nombre, precio, stock, descripcion, unidadMedida, idAfectacion, idCategoria) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQLeliminar = 
        "DELETE FROM producto WHERE idProducto = ?";

    private static final String SQLactualizar = 
        "UPDATE producto SET nombre = ?, precio = ?, stock = ?, descripcion = ?, unidadMedida = ?, idAfectacion = ?, idCategoria = ? " +
        "WHERE idProducto = ?";

    // 🔹 Incluye los joins (importante para mostrar nombres relacionados)
    private static final String SQLbuscar = 
        "SELECT p.*, " +
        "c.nombre AS nombreCategoria, " +
        "a.nombre AS nombreAfectacion " +
        "FROM producto p " +
        "LEFT JOIN tipocategoria c ON p.idcategoria = c.idcategoria " +
        "LEFT JOIN tipoafectacion a ON p.idafectacion = a.idafectacion " +
        "WHERE p.idproducto = ?";

    private static final String SQLlista = 
        "SELECT p.*, " +
        "c.nombre AS nombreCategoria, " +
        "a.nombre AS nombreAfectacion " +
        "FROM producto p " +
        "LEFT JOIN tipocategoria c ON p.idcategoria = c.idcategoria " +
        "LEFT JOIN tipoafectacion a ON p.idafectacion = a.idafectacion " +
        "ORDER BY p.nombre";


    // -----------------------------
    // 🔹 MÉTODOS CRUD
    // -----------------------------

    @Override
    public List<Producto> listarProducto() {
        List<Producto> listaProducto = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLlista);
             ResultSet res = ps.executeQuery()) {

            while (res.next()) {
                Producto p = new Producto();
                p.setIdProducto(res.getLong("idproducto"));
                p.setNombre(res.getString("nombre"));
                p.setPrecio(res.getBigDecimal("precio"));
                p.setStock(res.getInt("stock"));
                p.setDescripcion(res.getString("descripcion"));
                p.setUnidadMedida(res.getString("unidadmedida"));
                p.setIdTipoAfectacion(res.getLong("idafectacion"));
                p.setIdCategoria(res.getLong("idcategoria"));
                p.setNombreAfectacion(res.getString("nombreafectacion"));
                p.setNombreCategoria(res.getString("nombrecategoria"));
                listaProducto.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar productos: " + e.getMessage());
        }
        return listaProducto;
    }

    @Override
    public void registarProducto(Producto produc) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLregistrar)) {

            ps.setString(1, produc.getNombre());
            ps.setBigDecimal(2, produc.getPrecio());
            ps.setInt(3, produc.getStock());
            ps.setString(4, produc.getDescripcion());
            ps.setString(5, produc.getUnidadMedida());
            ps.setLong(6, produc.getIdTipoAfectacion());
            ps.setLong(7, produc.getIdCategoria());
            
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                // Verificar si es un error de duplicación (código PostgreSQL para unique_violation)
                if (e.getSQLState().equals("23505")) {
                    String mensajeError = "Ya existe un producto con el\n" +
                                        "nombre '" + produc.getNombre() + "'.\n" +
                                        "Por favor, elija otro nombre.";
                    throw new SQLException(mensajeError);
                }
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar producto: " + e.getMessage());
            throw e; // Propagar el error para que la capa superior pueda manejarlo
        }
    }

    @Override
    public void actualizarProducto(Producto produc) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLactualizar)) {

            ps.setString(1, produc.getNombre());
            ps.setBigDecimal(2, produc.getPrecio());
            ps.setInt(3, produc.getStock());
            ps.setString(4, produc.getDescripcion());
            ps.setString(5, produc.getUnidadMedida());
            ps.setLong(6, produc.getIdTipoAfectacion());
            ps.setLong(7, produc.getIdCategoria());
            ps.setLong(8, produc.getIdProducto());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage());
        }
    }

    @Override
    public void eliminarProducto(Producto produc) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLeliminar)) {

            ps.setLong(1, produc.getIdProducto());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage());
        }
    }

    @Override
    public Producto buscarProducto(Long idProducto) {
        Producto p = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLbuscar)) {

            ps.setLong(1, idProducto);

            try (ResultSet res = ps.executeQuery()) {
                if (res.next()) {
                    p = new Producto(
                        res.getLong("idproducto"),
                        res.getString("nombre"),
                        res.getBigDecimal("precio"),
                        res.getInt("stock"),
                        res.getString("descripcion"),
                        res.getString("unidadmedida"),
                        res.getLong("idafectacion"),
                        res.getLong("idcategoria"),
                        res.getString("nombrecategoria"),
                        res.getString("nombreafectacion")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar producto: " + e.getMessage());
        }
        return p;
    }

    // -----------------------------
    // 🔹 FILTRO DINÁMICO DE PRODUCTOS
    // -----------------------------

    @Override
    public List<Producto> filtrarProductos(
            String nombre,
            Double precio,
            Integer stock,
            String unidadMedida,
            Integer idCategoria,
            Integer idAfectacion) throws SQLException {

        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQLlista.replace("ORDER BY p.nombre", "")); // quitamos ORDER BY para insertar filtros
        List<Object> params = new ArrayList<>();
        boolean whereAdded = false;

        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" p.nombre ILIKE ?");
            params.add("%" + nombre + "%");
            whereAdded = true;
        }

        if (precio != null) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" p.precio = ?");
            params.add(precio);
            whereAdded = true;
        }

        if (stock != null) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" p.stock = ?");
            params.add(stock);
            whereAdded = true;
        }

        if (unidadMedida != null && !unidadMedida.trim().isEmpty()) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" p.unidadmedida ILIKE ?");
            params.add("%" + unidadMedida + "%");
            whereAdded = true;
        }

        if (idCategoria != null) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" p.idcategoria = ?");
            params.add(idCategoria);
            whereAdded = true;
        }

        if (idAfectacion != null) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" p.idafectacion = ?");
            params.add(idAfectacion);
            whereAdded = true;
        }

        // Reagregamos el ORDER BY
        sql.append(" ORDER BY p.nombre");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getLong("idproducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getBigDecimal("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setUnidadMedida(rs.getString("unidadmedida"));
                    p.setIdCategoria(rs.getLong("idcategoria"));
                    p.setIdTipoAfectacion(rs.getLong("idafectacion"));
                    p.setNombreCategoria(rs.getString("nombrecategoria"));
                    p.setNombreAfectacion(rs.getString("nombreafectacion"));
                    lista.add(p);
                }
            }

        }

        return lista;
    }
}

