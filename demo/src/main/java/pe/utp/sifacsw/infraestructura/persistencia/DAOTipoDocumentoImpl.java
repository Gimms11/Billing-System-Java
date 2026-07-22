package pe.utp.sifacsw.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOTipoDocumento;
import pe.utp.sifacsw.dominio.modelos.TipoDocumento;

public class DAOTipoDocumentoImpl implements DAOTipoDocumento{

    @Override
    public List<TipoDocumento> obtenerTodos() {
        List<TipoDocumento> documentos = new ArrayList<>();
        String sql = "SELECT iddocumento, nombre, descripcion FROM tipodocumento ORDER BY nombre";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TipoDocumento td = new TipoDocumento();
                td.setIdDocumento(rs.getLong("iddocumento"));
                td.setNombreDocumento(rs.getString("nombre"));
                td.setDescripcion(rs.getString("descripcion"));
                documentos.add(td);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar los tipos de documento", e);
        }

        return documentos;
    }

    @Override
    public TipoDocumento obtener(Long id) {
        String sql = "SELECT iddocumento, nombre, descripcion FROM tipodocumento WHERE iddocumento = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TipoDocumento td = new TipoDocumento();
                    td.setIdDocumento(rs.getLong("iddocumento"));
                    td.setNombreDocumento(rs.getString("nombre"));
                    td.setDescripcion(rs.getString("descripcion"));
                    return td;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener tipo de documento", e);
        }
        return null;
    }
}
