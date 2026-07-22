package pe.utp.sifacsw.infraestructura.persistencia;

import pe.utp.sifacsw.dominio.modelos.AfectacionProductos;
import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOTipoAfectacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOTipoAfectacionImpl implements DAOTipoAfectacion {

    @Override
    public List<AfectacionProductos> obtenerTodas() {
        List<AfectacionProductos> lista = new ArrayList<>();
        String sql = "SELECT idafectacion, nombre, descripcion FROM tipoafectacion";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AfectacionProductos af = new AfectacionProductos();
                af.setIdAfectacion(rs.getLong("idafectacion"));
                af.setNombreAfectacion(rs.getString("nombre"));
                af.setDescripcion(rs.getString("descripcion"));
                lista.add(af);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener las afectaciones: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public AfectacionProductos obtener(Long id) {
        AfectacionProductos afectacion = null;
        String sql = "SELECT idafectacion, nombre, descripcion FROM tipoafectacion WHERE idafectacion = ?";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    afectacion = new AfectacionProductos();
                    afectacion.setIdAfectacion(rs.getLong("idafectacion"));
                    afectacion.setNombreAfectacion(rs.getString("nombre"));
                    afectacion.setDescripcion(rs.getString("descripcion"));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener la afectación: " + e.getMessage());
        }

        return afectacion;
    }
}

