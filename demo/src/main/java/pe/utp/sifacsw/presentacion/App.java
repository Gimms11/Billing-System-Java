package pe.utp.sifacsw.presentacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import pe.utp.sifacsw.infraestructura.database.ConexionBD;

/**
 * SIFAC-SW — Sistema de Facturación Académica
 * <p>
 * Punto de entrada principal. Implementa caché de vistas FXML para evitar
 * recargar desde disco en cada navegación (Arquitectura de Aplicación — Unidad
 * III).
 * </p>
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Caché de vistas ya instanciadas: clave = nombre de vista, valor = nodo raíz
     */
    private static final Map<String, Parent> vistaCache = new HashMap<>();

    /**
     * Vistas que NO deben cachearse porque necesitan recargar datos frescos
     * cada vez que se navega a ellas (formularios de alta/edición).
     */
    private static final java.util.Set<String> VISTAS_SIN_CACHE = java.util.Set.of(
            "GenFactures", "ModCliente", "ModProducto");

    @Override
    public void init() {
        // Inicializar el pool de conexiones en segundo plano antes de mostrar la UI.
        // Esto evita que el primer login pague el costo del handshake SSL con NeonDB.
        Thread warmup = new Thread(() -> {
            try {
                ConexionBD.getConnection().close();
            } catch (Exception ignored) {
            }
        }, "db-pool-warmup");
        warmup.setDaemon(true);
        warmup.start();

        Font.loadFont(App.class.getResourceAsStream("/pe/utp/sifacsw/fonts/Roboto-Thin.ttf"), 10);
        Font.loadFont(App.class.getResourceAsStream("/pe/utp/sifacsw/fonts/Roboto-Regular.ttf"), 10);
        Font.loadFont(App.class.getResourceAsStream("/pe/utp/sifacsw/fonts/Roboto-Bold.ttf"), 10);
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(cargarVista("login"), 900, 700);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("SIFAC-SW");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navega a la vista indicada. Si ya está en caché, la reutiliza directamente
     * sin leer el archivo FXML del disco nuevamente.
     *
     * @param fxml Nombre del archivo FXML sin extensión (ej. "GenClientes")
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(cargarVista(fxml));
    }

    /**
     * Invalida la caché de una vista específica, obligando a recargarla
     * desde disco en la próxima navegación.
     *
     * @param fxml Nombre de la vista a invalidar
     */
    public static void invalidarCache(String fxml) {
        vistaCache.remove(fxml);
    }

    /**
     * Carga una vista desde caché o desde disco si no existe en caché.
     */
    private static Parent cargarVista(String fxml) throws IOException {
        // Las vistas sin caché siempre se recargan
        if (VISTAS_SIN_CACHE.contains(fxml)) {
            return cargarFXML(fxml);
        }

        // Retornar desde caché si existe
        if (vistaCache.containsKey(fxml)) {
            return vistaCache.get(fxml);
        }

        // Primera carga: leer desde disco y guardar en caché
        Parent vista = cargarFXML(fxml);
        vistaCache.put(fxml, vista);
        return vista;
    }

    /**
     * Lee el archivo FXML del disco y devuelve el nodo raíz.
     */
    private static Parent cargarFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/pe/utp/sifacsw/vistas/" + fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    /** Cierra el pool de conexiones al cerrar la aplicación */
    @Override
    public void stop() {
        ConexionBD.cerrarPool();
    }
}
