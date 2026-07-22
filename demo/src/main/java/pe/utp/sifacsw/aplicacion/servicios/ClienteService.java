package pe.utp.sifacsw.aplicacion.servicios;

import java.sql.SQLException;
import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import pe.utp.sifacsw.dominio.modelos.Cliente;
import pe.utp.sifacsw.infraestructura.dao.DAOCliente;
import pe.utp.sifacsw.infraestructura.dao.DAOFactory;

public class ClienteService {
    private final DAOCliente repository;
    
    public ClienteService() {
        DAOFactory factory = DAOFactory.getDAOFactory();
        this.repository = factory.getClienteDAO();
    }

    public List<Cliente> obtenerTodos() {
        return repository.listarClientes();
    }

    public void insertarCliente(Cliente cliente) throws RuntimeException {
        try {
            repository.registrarCliente(cliente);
        } catch (SQLException e) {
            // Convertir SQLException en RuntimeException con mensaje amigable
            // El mensaje de error ya viene formateado del DAO
            // Solo agregamos el encabezado si no es un error de duplicación
            String mensaje;
            if (e.getMessage().contains("ya está registrado") || 
                e.getMessage().contains("ya está en uso")) {
                mensaje = e.getMessage();
            } else {
                mensaje = "Error al registrar cliente:\n" + 
                         "------------------------\n" +
                         e.getMessage();
            }
            throw new RuntimeException(mensaje);
        }
    }

    public List<Cliente> filtrarClientes(String numDocumento, String nombres, String apellidos, Integer idDistrito,
            Integer idDocumento) {
        try {
            List<Cliente> productos = repository.filtrarClientes(numDocumento, nombres, apellidos, idDistrito, idDocumento);
            return productos != null ? productos : java.util.Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener Clientes: " + e.getMessage());
        }
    }

    public Cliente obtenerPorId(Long id) {
        return repository.buscarCliente(id);
    }

    public void actualizarCliente(Cliente cliente){
        if(cliente == null) return;
        repository.actualizarCliente(cliente);
    }
}

