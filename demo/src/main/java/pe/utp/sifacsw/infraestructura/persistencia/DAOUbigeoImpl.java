package pe.utp.sifacsw.infraestructura.persistencia;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOUbigeo;
import pe.utp.sifacsw.dominio.modelos.Departamento;
import pe.utp.sifacsw.dominio.modelos.Provincia;
import pe.utp.sifacsw.dominio.modelos.Distrito;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOUbigeoImpl implements DAOUbigeo {

    @Override
    public List<Departamento> obtenerTodosDepartamentos() {
        List<Departamento> departamentos = new ArrayList<>();
        String sql = "SELECT iddepartamento, nombre FROM departamento ORDER BY nombre";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Departamento d = new Departamento();
                d.setIdDepartamento(rs.getLong("iddepartamento"));
                d.setNombreDepartamento(rs.getString("nombre"));
                departamentos.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar departamentos", e);
        }
        return departamentos;
    }

    @Override
    public List<Provincia> obtenerProvinciasPorDepartamento(Long idDepartamento) {
        List<Provincia> provincias = new ArrayList<>();
        String sql = "SELECT idprovincia, nombre FROM provincia WHERE iddepartamento = ? ORDER BY nombre";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idDepartamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Provincia p = new Provincia();
                    p.setIdProvincia(rs.getLong("idprovincia"));
                    p.setNombreProvincia(rs.getString("nombre"));
                    p.setIdDepartamento(idDepartamento);
                    provincias.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar provincias", e);
        }
        return provincias;
    }

    @Override
    public List<Distrito> obtenerDistritosPorProvincia(Long idProvincia) {
        List<Distrito> distritos = new ArrayList<>();
        String sql = "SELECT iddistrito, nombre FROM distrito WHERE idprovincia = ? ORDER BY nombre";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idProvincia);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Distrito d = new Distrito();
                    d.setIdDistrito(rs.getLong("iddistrito"));
                    d.setNombreDistrito(rs.getString("nombre"));
                    d.setIdProvincia(idProvincia);
                    distritos.add(d);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar distritos", e);
        }
        return distritos;
    }

    @Override
    public Distrito obtenerDistrito(Long id) {
        String sql = "SELECT iddistrito, nombre, idprovincia FROM distrito WHERE iddistrito = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Distrito d = new Distrito();
                    d.setIdDistrito(rs.getLong("iddistrito"));
                    d.setNombreDistrito(rs.getString("nombre"));
                    d.setIdProvincia(rs.getLong("idprovincia"));
                    return d;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener distrito", e);
        }
        return null;
    } 

    @Override
    public List<Long> obtenerIds(Long idDistrito){
        String sqlString =
                "SELECT d.iddistrito, p.idprovincia, dep.iddepartamento"
                + " FROM distrito d"
                + " JOIN provincia p ON d.idprovincia = p.idprovincia"
                + " JOIN departamento dep ON p.iddepartamento = dep.iddepartamento"
                + " WHERE d.iddistrito = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlString)) {

            stmt.setLong(1, idDistrito);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Long> lista = new ArrayList<>();
                    lista.add(rs.getLong("iddistrito"));
                    lista.add(rs.getLong("idprovincia"));
                    lista.add(rs.getLong("iddepartamento"));
                    return lista;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener distrito", e);
        }
        return null;
    }
}
