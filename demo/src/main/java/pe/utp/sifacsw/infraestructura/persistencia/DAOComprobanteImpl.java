package pe.utp.sifacsw.infraestructura.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.utp.sifacsw.infraestructura.database.ConexionBD;
import pe.utp.sifacsw.infraestructura.dao.DAOComprobante;
import pe.utp.sifacsw.dominio.modelos.Comprobante;

public class DAOComprobanteImpl implements DAOComprobante {

    private Comprobante mapearComprobante(ResultSet res) throws SQLException {
        Comprobante co = new Comprobante();
        co.setIdComprobante(res.getLong("idcomprobante"));
        co.setFechaEmision(res.getDate("fechaemision").toLocalDate());
        co.setSerie(res.getString("numserie"));
        co.setTotalFinal(res.getBigDecimal("totalfinal"));
        co.setDireccionEnvio(res.getString("direccionenvio"));
        co.setDevengado(res.getBigDecimal("devengado"));
        co.setIdMedioPago(res.getLong("idmediopago"));
        co.setIdCliente(res.getLong("idcliente"));
        co.setIdUsuario(res.getLong("idusuario"));
        co.setIdTipoComprobante(res.getLong("idtipocomprobante"));
        co.setNombreCliente(res.getString("nombrecliente"));
        co.setClienteDistrito(res.getString("nombredistrito"));
        co.setMedioPago(res.getString("mediopago"));
        co.setClienteDocumento(res.getString("clientedocumento"));
        return co;
    }

    private static final String SQLregistrar = "INSERT INTO comprobante (fechaemision, numserie, totalfinal, direccionenvio, devengado, idmediopago, idcliente, idusuario, idtipocomprobante) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQLeliminar = "Delete from Comprobante where idComprobante = ?";
    private static final String SQLbuscar = "Select * from Comprobante where idComprobante = ?";

    private static final String SQLlista = "SELECT c.*, cl.nombres AS nombrecliente, d.nombre AS nombredistrito, " +
            "mp.nombre AS mediopago, cl.numdocumento AS clientedocumento " +
            "FROM comprobante c " +
            "JOIN cliente cl ON c.idcliente = cl.idcliente " +
            "JOIN distrito d ON cl.iddistrito = d.iddistrito " +
            "JOIN mediopago mp ON c.idmediopago = mp.idmediopago";

    @Override
    public void registarComprobante(Comprobante comp) {
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLregistrar)) {

            ps.setDate(1, java.sql.Date.valueOf(comp.getFechaEmision())); // 👈 importante
            ps.setString(2, comp.getSerie());
            ps.setBigDecimal(3, comp.getTotalFinal());
            ps.setString(4, comp.getDireccionEnvio());
            ps.setBigDecimal(5, comp.getDevengado());
            ps.setLong(6, comp.getIdMedioPago());
            ps.setLong(7, comp.getIdCliente());
            ps.setLong(8, comp.getIdUsuario());
            ps.setLong(9, comp.getIdTipoComprobante());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarComprobante(Comprobante comp) {
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQLeliminar);
            ps.setLong(1, comp.getIdComprobante());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Comprobante> listarComprobante() {
        List<Comprobante> listaComprobante = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(SQLlista);
                ResultSet res = ps.executeQuery()) {
            while (res.next()) {
                listaComprobante.add(mapearComprobante(res));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al listar comprobantes: " + e.getMessage(), e);
        }
        return listaComprobante;
    }

    @Override
    public Comprobante buscarComprobante(Comprobante comp) {
        Comprobante co = null;
        try {
            Connection conn = ConexionBD.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQLbuscar);
            ps.setLong(1, comp.getIdComprobante());
            ;
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                co = new Comprobante(
                        res.getLong("idComprobante"),
                        res.getDate("FechaEmision").toLocalDate(),
                        res.getString("numSerie"),
                        res.getBigDecimal("TotalFinal"),
                        res.getString("direccionEnvio"),
                        res.getBigDecimal("devengado"),
                        res.getLong("idImpuesto"),
                        res.getLong("idMedioPago"),
                        res.getLong("idCliente"),
                        res.getLong("idUsuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return co;
    }

    @Override
    public List<Comprobante> filtrarComprobantes(Integer tiempoIndex, String numDocumentoCliente, String numSerie)
            throws SQLException {
        List<Comprobante> listaComprobantes = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT c.*, cl.nombres AS nombrecliente, d.nombre AS nombredistrito, " +
                        "mp.nombre AS mediopago, cl.numdocumento AS clientedocumento " +
                        "FROM comprobante c " +
                        "JOIN cliente cl ON c.idcliente = cl.idcliente " +
                        "JOIN distrito d ON cl.iddistrito = d.iddistrito " +
                        "JOIN mediopago mp ON c.idmediopago = mp.idmediopago");

        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        // Filtro por tiempo
        if (tiempoIndex != null && tiempoIndex >= 0 && tiempoIndex <= 3) {
            switch (tiempoIndex) {
                case 0: // Hoy
                    conditions.add(
                            "c.fechaemision >= (CURRENT_DATE - INTERVAL '1 days') AND c.fechaemision <= CURRENT_DATE");
                    break;
                case 1: // Semana
                    conditions.add(
                            "c.fechaemision >= (CURRENT_DATE - INTERVAL '7 days') AND c.fechaemision <= CURRENT_DATE");
                    break;
                case 2: // Mes
                    conditions.add(
                            "c.fechaemision >= (CURRENT_DATE - INTERVAL '1 month') AND c.fechaemision <= CURRENT_DATE");
                    break;
                case 3: // Año
                    conditions.add(
                            "c.fechaemision >= (CURRENT_DATE - INTERVAL '1 year') AND c.fechaemision <= CURRENT_DATE");
                    break;
            }
        }

        // Filtro por número de documento del cliente
        if (numDocumentoCliente != null && !numDocumentoCliente.trim().isEmpty()) {
            conditions.add("cl.numdocumento ILIKE ?");
            params.add("%" + numDocumentoCliente + "%");
        }

        // Filtro por número de serie
        if (numSerie != null && !numSerie.trim().isEmpty()) {
            conditions.add("c.numserie = ?");
            params.add(numSerie);
        }

        // Agregar condiciones WHERE si hay alguna
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        // Ordenar por fecha de emisión descendente
        sql.append(" ORDER BY c.fechaemision DESC");

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Establecer los parámetros
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet res = ps.executeQuery()) {
                while (res.next()) {
                    listaComprobantes.add(mapearComprobante(res));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar comprobantes: " + e.getMessage());
            throw e;
        }

        return listaComprobantes;
    }
}
