package pe.utp.sifacsw.presentacion.controladores;

import java.io.IOException;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import pe.utp.sifacsw.presentacion.App;
import pe.utp.sifacsw.aplicacion.servicios.AutenticacioService;
import pe.utp.sifacsw.aplicacion.servicios.PermisosNavegacion;

public class ControllerMenu {
    @FXML
    private FontIcon das;
    @FXML
    private FontIcon agr1;
    @FXML
    private FontIcon agr2;
    @FXML
    private FontIcon agr3;
    @FXML
    private FontIcon historial;

    private PermisosNavegacion permisosnav;
    private AutenticacioService servicioAut;

    // Variable estática para mantener la vista actual entre re-cargas del menú
    private static String vistaActual = "Dashboard";

    @FXML
    public void initialize() {
        permisosnav = PermisosNavegacion.getInstance();
        servicioAut = AutenticacioService.getInstance();

        if (servicioAut.getUsuarioActual() == null) {
            redirigirALogin();
            return;
        }

        configurarTooltip();
        aplicarPermisos();

        // Marcar como activo la vista actual (persistida estáticamente)
        marcarMenuActivo(vistaActual);
    }

    @FXML
    private void goToDashboard() throws IOException {
        navegarConValidacion("Dashboard");
    }

    @FXML
    private void goToClients() throws IOException {
        navegarConValidacion("GenClientes");
    }

    @FXML
    private void goToGenFactures() throws IOException {
        navegarConValidacion("GenFactures");
    }

    @FXML
    private void gotoGenProducts() throws IOException {
        navegarConValidacion("GenProducts");
    }

    @FXML
    private void goToHistory() throws IOException {
        navegarConValidacion("History");
    }

    @FXML
    private void goToConfigurations() throws IOException {
        navegarConValidacion("Configurations");
    }

    @FXML
    private void goToSingOut() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText("¿Está seguro que desea cerrar sesión?");
        alert.setContentText("Se perderá la sesión actual.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            servicioAut.cerrarSesion();
            vistaActual = "Dashboard"; // Reset al cerrar sesión
            App.setRoot("Login");
        }
    }

    private void configurarTooltip() {
        // Crear tooltips con delay instantáneo
        Tooltip tooltipDashboard = new Tooltip("Dashboard");
        Tooltip tooltipFacturas = new Tooltip("Generar Comprobantes");
        Tooltip tooltipClientes = new Tooltip("Agregar Clientes");
        Tooltip tooltipProductos = new Tooltip("Agregar Productos");
        Tooltip tooltipHistorial = new Tooltip("Historial de Comprobantes");

        // Configurar delay instantáneo (0 milisegundos)
        configurarDelayTooltip(tooltipDashboard);
        configurarDelayTooltip(tooltipFacturas);
        configurarDelayTooltip(tooltipClientes);
        configurarDelayTooltip(tooltipProductos);
        configurarDelayTooltip(tooltipHistorial);

        // Instalar tooltips
        Tooltip.install(das, tooltipDashboard);
        Tooltip.install(agr1, tooltipFacturas);
        Tooltip.install(agr2, tooltipClientes);
        Tooltip.install(agr3, tooltipProductos);
        Tooltip.install(historial, tooltipHistorial);
    }

    private void configurarDelayTooltip(Tooltip tooltip) {
        tooltip.setShowDelay(javafx.util.Duration.millis(100)); // Aparece casi instantáneamente
        tooltip.setShowDuration(javafx.util.Duration.seconds(10)); // Permanece visible 10 segundos
        tooltip.setHideDelay(javafx.util.Duration.millis(100)); // Se oculta rápido
    }

    private void aplicarPermisos() {
        boolean esAdmin = servicioAut.esAdmin();
        boolean esEmisor = servicioAut.esEmisor();

        agr3.setVisible(esAdmin);
        agr3.setManaged(esAdmin);

        boolean puedeEmitir = esAdmin || esEmisor;
        agr1.setVisible(puedeEmitir);
        agr1.setManaged(puedeEmitir);
        agr2.setVisible(puedeEmitir);
        agr2.setManaged(puedeEmitir);
        historial.setVisible(true);
    }

    private void navegarConValidacion(String pantalla) {
        try {
            // Actualizar vista actual ANTES de navegar para que el menú se inicialice
            // correctamente
            vistaActual = pantalla;
            permisosnav.navegarA(pantalla);
        } catch (SecurityException e) {
            // Si falla, revertir el cambio
            mostrarAlertaError("Acceso Denegado",
                    "No tiene permisos para acceder a: " + pantalla + "\n\n" +
                            "Rol requerido: " + obtenerRolesRequeridos(pantalla));
        } catch (IOException e) {
            mostrarAlertaError("Error de Navegación",
                    "No se pudo cargar la pantalla: " + pantalla);
        }
    }

    private String obtenerRolesRequeridos(String pantalla) {
        switch (pantalla) {
            case "GenProducts":
            case "Configurations":
                return "ADMIN";
            case "GenClientes":
            case "GenFactures":
                return "ADMIN, EMISOR";
            case "History":
            case "Dashboard":
                return "ADMIN, EMISOR, LECTOR";
            default:
                return "No definido";
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void redirigirALogin() {
        try {
            App.setRoot("Login");
        } catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Marca visualmente el ítem de menú activo
     * 
     * @param pantalla Nombre de la pantalla actual
     */
    private void marcarMenuActivo(String pantalla) {
        // Primero, remover la clase 'active' de todos los íconos
        das.getStyleClass().remove("active");
        agr1.getStyleClass().remove("active");
        agr2.getStyleClass().remove("active");
        agr3.getStyleClass().remove("active");
        historial.getStyleClass().remove("active");

        // Luego, agregar la clase 'active' solo al ícono correspondiente
        switch (pantalla) {
            case "Dashboard":
                das.getStyleClass().add("active");
                break;
            case "GenFactures":
                agr1.getStyleClass().add("active");
                break;
            case "GenClientes":
                agr2.getStyleClass().add("active");
                break;
            case "GenProducts":
                agr3.getStyleClass().add("active");
                break;
            case "History":
                historial.getStyleClass().add("active");
                break;
        }
    }
}

