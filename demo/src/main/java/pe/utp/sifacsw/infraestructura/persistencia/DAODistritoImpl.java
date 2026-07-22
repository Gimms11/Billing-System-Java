package pe.utp.sifacsw.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAODistrito;
import pe.utp.sifacsw.dominio.modelos.Distrito;

public class DAODistritoImpl implements DAODistrito {
    
    private static final String SQL_LISTAR = "SELECT * FROM distrito";
    private static final String SQL_OBTENER = "SELECT * FROM distrito WHERE iddistrito = ?";
    private static final String SQL_OBTENER_NOMBRE = "SELECT nombredistrito FROM distrito WHERE iddistrito = ?";

    @Override
    public List<Distrito> listarDistritos() {
        List<Distrito> distritos = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Distrito distrito = new Distrito();
                distrito.setIdDistrito(rs.getLong("iddistrito"));
                distrito.setNombreDistrito(rs.getString("nombre"));
                distrito.setIdProvincia(rs.getLong("idprovincia"));
                distritos.add(distrito);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distritos;
    }

    @Override
    public Distrito obtenerDistrito(Long id) {
        Distrito distrito = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_OBTENER)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    distrito = new Distrito();
                    distrito.setIdDistrito(rs.getLong("iddistrito"));
                    distrito.setNombreDistrito(rs.getString("nombre"));
                    distrito.setIdProvincia(rs.getLong("idprovincia"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return distrito;
    }

    @Override
    public String obtenerNombreDistrito(Long id) {
        String nombre = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_OBTENER_NOMBRE)) {
            
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombre = rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombre;
    }
}
