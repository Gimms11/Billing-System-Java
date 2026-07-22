package pe.utp.sifacsw.aplicacion.servicios;

import pe.utp.sifacsw.dominio.modelos.Empresa;
import pe.utp.sifacsw.dominio.modelos.Usuario;
import pe.utp.sifacsw.patrones.strategy.ManejadorJsonI;
import pe.utp.sifacsw.patrones.strategy.ManejadorEmpresa;
import pe.utp.sifacsw.patrones.strategy.ManejadorSesion;

/**
 * PATRÓN FACADE (Fachada)
 * 
 * Esta clase actúa como una fachada que simplifica el acceso al subsistema
 * de gestión de archivos JSON. Oculta la complejidad de instanciar y gestionar
 * las estrategias de lectura/escritura de JSON (ManejadorJsonI).
 * 
 * Proporciona una interfaz simple y unificada para:
 * - Leer/escribir datos de Empresa (usa ManejadorEmpresa)
 * - Leer/escribir datos de Usuario/Sesión (usa ManejadorSesion)
 * 
 * Sin esta fachada, los clientes tendrían que conocer e instanciar
 * manualmente las estrategias correctas para cada tipo de archivo.
 */
public class ManejadorService {
    private final ManejadorJsonI<Empresa> manejadorEmpresa;
    private final ManejadorJsonI<Usuario> manejadorUsuario;

    public ManejadorService() {
        this.manejadorEmpresa = new ManejadorEmpresa();
        this.manejadorUsuario = new ManejadorSesion();
    }

    public ManejadorService(ManejadorJsonI<Empresa> manejadorEmpresa,
            ManejadorJsonI<Usuario> manejadorUsuario) {
        this.manejadorEmpresa = manejadorEmpresa;
        this.manejadorUsuario = manejadorUsuario;
    }

    // Lectura
    public Empresa leerEmpresa() {
        return manejadorEmpresa.leerArchivoJson(null);
    }

    public Usuario leerUsuario() {
        return manejadorUsuario.leerArchivoJson(null);
    }

    // Escritura
    public void guardarEmpresa(Empresa empresa) {
        manejadorEmpresa.escribirArchivoJson(empresa, null);
    }

    public void guardarUsuario(Usuario usuario) {
        manejadorUsuario.escribirArchivoJson(usuario, null);
    }
}
