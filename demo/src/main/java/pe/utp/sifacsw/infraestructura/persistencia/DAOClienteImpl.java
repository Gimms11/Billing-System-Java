package pe.utp.sifacsw.infraestructura.persistencia;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOCliente;
import pe.utp.sifacsw.dominio.modelos.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOClienteImpl implements DAOCliente {

    private static final String SQL_INSERT = "INSERT INTO cliente (nombres, apellidos, telefono, correo, direccion, numdocumento, iddistrito, iddocumento) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE cliente SET nombres = ?, apellidos = ?, telefono = ?, correo = ?, direccion = ?, "
            +
            "numdocumento = ?, iddistrito = ?, iddocumento = ? WHERE idcliente = ?";

    private static final String SQL_DELETE = "DELETE FROM cliente WHERE idcliente = ?";

    private static final String SQL_SELECT_BY_ID = "SELECT c.*, d.nombre AS nombreDistrito, t.nombre AS nombreTipoDocumento "
            +
            "FROM cliente c " +
            "INNER JOIN distrito d ON d.iddistrito = c.iddistrito " +
            "INNER JOIN tipodocumento t ON t.iddocumento = c.iddocumento" +
            " WHERE c.idcliente = ?";

    private static final String SQL_SELECT_ALL = "SELECT c.*, d.nombre AS nombreDistrito, t.nombre AS nombreTipoDocumento "
            +
            "FROM cliente c " +
            "INNER JOIN distrito d ON d.iddistrito = c.iddistrito " +
            "INNER JOIN tipodocumento t ON t.iddocumento = c.iddocumento";

    @Override
    public void registrarCliente(Cliente cliente) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getNumDocumento());
            ps.setLong(7, cliente.getIdDistrito());
            ps.setLong(8, cliente.getIdDocumento());
            
            try {
                ps.executeUpdate();
            } catch (SQLException e) {
                // Verificar si es un error de duplicación
                if (e.getSQLState().equals("23505")) { // Código PostgreSQL para unique_violation
                    // Verificar qué campo único fue violado
                    if (e.getMessage().contains("numdocumento")) {
                        throw new SQLException(String.format(
                            "El número de documento\n'%s'\nya está registrado en el\nsistema.",
                            cliente.getNumDocumento()));
                    } else if (e.getMessage().contains("correo")) {
                        throw new SQLException(String.format(
                            "El correo electrónico\n'%s'\nya está en uso.",
                            cliente.getCorreo()));
                    } else if (e.getMessage().contains("telefono")) {
                        throw new SQLException(String.format(
                            "El número de teléfono\n'%s'\nya está registrado.",
                            cliente.getTelefono()));
                    } else {
                        throw new SQLException(
                            "Los datos ingresados ya\n" +
                            "existen para otro cliente\n" +
                            "en el sistema.");
                    }
                }
                // Si es otro tipo de error, lo propagamos
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            throw e; // Propagar el error para que la capa superior pueda manejarlo
        }
    }

    @Override
    public void actualizarCliente(Cliente cliente) {
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidos());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getNumDocumento());
            ps.setLong(7, cliente.getIdDistrito());
            ps.setLong(8, cliente.getIdDocumento());
            ps.setLong(9, cliente.getIdCliente());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
        }
    }

    @Override
    public void eliminarCliente(Cliente cliente) {
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setLong(1, cliente.getIdCliente());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    @Override
    public Cliente buscarCliente(Long idCliente) {
        Cliente cliente = null;
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setLong(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                            rs.getLong("idcliente"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("telefono"),
                            rs.getString("correo"),
                            rs.getString("direccion"),
                            rs.getString("numdocumento"),
                            rs.getLong("iddistrito"),
                            rs.getLong("iddocumento"),
                            rs.getString("nombreDistrito"),
                            rs.getString("nombreTipoDocumento"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return cliente;
    }

    @Override
    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getLong("idcliente"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion"),
                        rs.getString("numdocumento"),
                        rs.getLong("iddistrito"),
                        rs.getLong("iddocumento"),
                        rs.getString("nombredistrito"),
                        rs.getString("nombretipodocumento"));
                cliente.setNombreDistrito(rs.getString("nombredistrito"));
                cliente.setNombreTipoDocumento(rs.getString("nombretipodocumento"));
                lista.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // 🔍 Nuevo método: filtrado flexible
    @Override
    public List<Cliente> filtrarClientes(String numDocumento, String nombres, String apellidos, Integer idDistrito,
            Integer idDocumento)
            throws SQLException {

        List<Cliente> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, d.nombre AS nombreDistrito, t.nombre AS nombreTipoDocumento " +
                        "FROM cliente c " +
                        "INNER JOIN distrito d ON d.iddistrito = c.iddistrito " +
                        "INNER JOIN tipodocumento t ON t.iddocumento = c.iddocumento");

        List<Object> params = new ArrayList<>();
        boolean whereAdded = false;

        if (numDocumento != null && !numDocumento.trim().isEmpty()) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" c.numdocumento ILIKE ?");
            params.add("%" + numDocumento + "%");
            whereAdded = true;
        }

        if (nombres != null && !nombres.trim().isEmpty()) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" c.nombres ILIKE ?");
            params.add("%" + nombres + "%");
            whereAdded = true;
        }

        if (apellidos != null && !apellidos.trim().isEmpty()) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" c.apellidos ILIKE ?");
            params.add("%" + apellidos + "%");
            whereAdded = true;
        }

        if (idDistrito != null) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" c.iddistrito = ?");
            params.add(idDistrito);
            whereAdded = true;
        }

        if (idDocumento != null) {
            sql.append(whereAdded ? " AND" : " WHERE").append(" c.iddocumento = ?");
            params.add(idDocumento);
            whereAdded = true;
        }

        sql.append(" ORDER BY c.nombres");

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getLong("idcliente"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("telefono"),
                            rs.getString("correo"),
                            rs.getString("direccion"),
                            rs.getString("numdocumento"),
                            rs.getLong("iddistrito"),
                            rs.getLong("iddocumento"),
                            rs.getString("nombredistrito"),
                            rs.getString("nombretipodocumento"));
                    cliente.setNombreDistrito(rs.getString("nombredistrito"));
                    cliente.setNombreTipoDocumento(rs.getString("nombretipodocumento"));
                    lista.add(cliente);
                }
            }
        }

        return lista;
    }
}

