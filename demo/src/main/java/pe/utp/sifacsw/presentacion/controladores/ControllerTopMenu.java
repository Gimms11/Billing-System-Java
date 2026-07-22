package pe.utp.sifacsw.presentacion.controladores;

import javafx.scene.control.Label;

import pe.utp.sifacsw.dominio.modelos.Usuario;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pe.utp.sifacsw.aplicacion.servicios.AutenticacioService;
import javafx.scene.Node;

public class ControllerTopMenu {

    @FXML
    private BorderPane topMenu_total;

    @FXML
    private Label txtUserName;
    @FXML
    private Label txtRol;

    private AutenticacioService servicioAut;

    // Variables para el arrastre
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void btnMinimizar() {
        Stage stage = (Stage) topMenu_total.getScene().getWindow();
        stage.setIconified(true); // Minimiza la ventana
    }

    @FXML
    private void btnCerrar() {
        // Mostrar diálogo de confirmación
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar aplicación");
        alert.setHeaderText("¿Está seguro que desea salir?");
        alert.setContentText("Se cerrará la aplicación.");

        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            Stage stage = (Stage) topMenu_total.getScene().getWindow();
            stage.close(); // Cierra la ventana
        }
    }

    @FXML
    public void initialize() {
        servicioAut = AutenticacioService.getInstance();
        // Hacer que la barra de título permita arrastrar la ventana
        topMenu_total.setOnMousePressed(this::handleMousePressed);
        topMenu_total.setOnMouseDragged(this::handleMouseDragged);

        cargarInfoUsuario();
    }

    private void cargarInfoUsuario() {
        Usuario usuario = servicioAut.getUsuarioActual();
        if (usuario != null) {
            txtUserName.setText(usuario.getUsername());
            txtRol.setText(usuario.getRol());
        }
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}
