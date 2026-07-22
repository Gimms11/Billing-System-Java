package pe.utp.sifacsw.infraestructura.dao;
import java.util.List;
import pe.utp.sifacsw.dominio.modelos.Usuario;

public interface DAOUsuario {
    public void registarUsuario(Usuario Usu);
    public void actualizarUsuario(Usuario usu);
    public void eliminarUsuario(Usuario usu);
    public List<Usuario> listarUsuario();
    public Usuario buscarUsuario(Usuario usu);
}
