package pe.utp.sifacsw.infraestructura.dao;

import java.sql.SQLException;
import java.util.List;
import pe.utp.sifacsw.dominio.modelos.Cliente;

public interface DAOCliente {
    void registrarCliente(Cliente cliente) throws SQLException;
    void actualizarCliente(Cliente cliente);
    void eliminarCliente(Cliente cliente);
    Cliente buscarCliente(Long idCliente);
    List<Cliente> listarClientes();
    List<Cliente> filtrarClientes(String numDocumento, String nombres, String apellidos, Integer idDistrito,
            Integer idDocumento)
            throws SQLException;
}

