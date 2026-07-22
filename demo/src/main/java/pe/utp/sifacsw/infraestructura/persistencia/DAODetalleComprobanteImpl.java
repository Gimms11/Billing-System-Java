package pe.utp.sifacsw.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAODetalleComprobante;
import pe.utp.sifacsw.dominio.modelos.DetalleComprobante;

public class DAODetalleComprobanteImpl implements DAODetalleComprobante {

    private static final String SQLregistrar =
        "INSERT INTO detallecomprobante (iddetalle, cantproductos, preciounitario, subtotal, total, idcomprobante, idproducto) " +
        "VALUES ((SELECT COALESCE(MAX(iddetalle), 0) + 1 FROM detallecomprobante), ?, ?, ?, ?, ?, ?)";
    private static final String SQLeliminar = "DELETE FROM detallecomprobante WHERE iddetalle = ?";
    private static final String SQLlista = "SELECT c.*,p.nombre AS nombreProducto FROM detallecomprobante "+
            "  JOIN producto p ON detallecomprobante.idproducto = p.idproducto;";
    private static final String SQLlistaPorComprobante = "SELECT c.*,p.nombre AS nombreProducto FROM detallecomprobante c\n" + //
                "  JOIN producto p ON c.idproducto = p.idproducto\n" + //
                "  WHERE idcomprobante = ?;";

    @Override
    public void registrarDetalleComprobante(DetalleComprobante detalle) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLregistrar)) {

            ps.setInt(1, detalle.getCantidadProductos());
            ps.setBigDecimal(2, detalle.getPrecioUnitario());
            ps.setBigDecimal(3, detalle.getSubtotal());
            ps.setBigDecimal(4, detalle.getTotal());
            ps.setLong(5, detalle.getIdComprobante());
            ps.setLong(6, detalle.getIdProducto());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar detalle de comprobante: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarDetalleComprobante(DetalleComprobante detalle) {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLeliminar)) {
            
            ps.setLong(1, detalle.getIdDetalle());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar detalle de comprobante: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DetalleComprobante> listarDetalleComprobante() {
        List<DetalleComprobante> listaDetalles = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLlista);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaDetalles.add(mapearDetalleComprobante(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar detalles de comprobante: " + e.getMessage(), e);
        }
        return listaDetalles;
    }

    @Override
    public List<DetalleComprobante> listarDetallesPorComprobante(Long idComprobante) {
        List<DetalleComprobante> listaDetalles = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQLlistaPorComprobante)) {
            
            ps.setLong(1, idComprobante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaDetalles.add(mapearDetalleComprobante(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar detalles por comprobante: " + e.getMessage(), e);
        }
        return listaDetalles;
    }

    private DetalleComprobante mapearDetalleComprobante(ResultSet rs) throws SQLException {
        DetalleComprobante detalle = new DetalleComprobante();
        detalle.setIdDetalle(rs.getLong("iddetalle"));
        detalle.setCantidadProductos(rs.getInt("cantproductos"));
        detalle.setPrecioUnitario(rs.getBigDecimal("preciounitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        detalle.setTotal(rs.getBigDecimal("total"));
        detalle.setIdComprobante(rs.getLong("idcomprobante"));
        detalle.setIdProducto(rs.getLong("idproducto"));
        detalle.setNombreProducto(rs.getString("nombreProducto"));
        return detalle;
    }
}
