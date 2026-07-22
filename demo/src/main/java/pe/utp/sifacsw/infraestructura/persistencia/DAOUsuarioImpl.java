package pe.utp.sifacsw.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOUsuario;
import pe.utp.sifacsw.dominio.modelos.Usuario;

public class DAOUsuarioImpl implements DAOUsuario {
    private static final String SQL_INSERTAR="Insert into usuario (username, password, rol) values (?, ?, ?)";
    private static final String SQL_ACTUALIZAR="Update usuario set username = ?, password = ?, rol = ? where idusuario = ?";
    private static final String SQL_ELIMINAR="Delete from usuario where idUsuario = ?";
    private static final String SQL_LISTA="Select * from usuario";
    private static final String SQL_BUSCAR="Select * from usuario where idusuario = ?";
    private static final String SQL_AUTENTIFICAR = "Select * from usuario where username = ? and password = ?";

    @Override
    public void registarUsuario(Usuario usu) {
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_INSERTAR)) {

            ps.setString(1, usu.getUsername());
            ps.setString(2, usu.getPassword());
            ps.setString(3, usu.getRol());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
    }
    @Override
    public void actualizarUsuario(Usuario usu) {
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_ACTUALIZAR)) {

            ps.setString(1, usu.getUsername());
            ps.setString(2, usu.getPassword());
            ps.setString(3, usu.getRol());
            ps.setLong(4, usu.getIdUsuario());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
    }
    @Override
    public void eliminarUsuario(Usuario usu) {
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_ELIMINAR)) {

            ps.setLong(1, usu.getIdUsuario());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }
    @Override
    public List<Usuario> listarUsuario() {
        List<Usuario> listaUsuario = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_LISTA);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getLong("idUsuario"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("rol")
                );
                listaUsuario.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return listaUsuario;
    }
    @Override
    public Usuario buscarUsuario(Usuario usu) {
        Usuario usuario = null;
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_BUSCAR)) {

            ps.setLong(1, usu.getIdUsuario());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                        rs.getLong("idUsuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return usuario;
    }    

    public Usuario autenticarUsuario(String username, String password) {
        Usuario usuario = null;
        try (Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL_AUTENTIFICAR)) {

            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                        rs.getLong("idUsuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return usuario;
    }
}

