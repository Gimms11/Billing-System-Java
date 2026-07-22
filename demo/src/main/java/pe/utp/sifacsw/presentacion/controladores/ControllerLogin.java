package pe.utp.sifacsw.presentacion.controladores;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pe.utp.sifacsw.presentacion.App;
import pe.utp.sifacsw.aplicacion.servicios.AutenticacioService;

import java.io.IOException;
import org.kordamp.ikonli.javafx.FontIcon;

import pe.utp.sifacsw.infraestructura.persistencia.DAOUsuarioImpl;
import pe.utp.sifacsw.dominio.modelos.Usuario;

public class ControllerLogin {
    @FXML
    private AnchorPane topMenu_total;

    @FXML
    private TextField txtusername;
    @FXML
    private PasswordField txtcontraseña;
    @FXML
    private CheckBox checkcontraseña;
    @FXML
    private FontIcon eyesIcon;
    @FXML
    private Button btningresar;
    private TextField txtcontraseñavisible;
    private DAOUsuarioImpl usuarioDAO = new DAOUsuarioImpl();
    private AutenticacioService autenticacioService = AutenticacioService.getInstance();

    @FXML
    private void initialize() {
        checkcontraseña.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                mostrarContraseña();
            } else {
                ocultarContraseña();
            }
        });
    }

    private void mostrarContraseña() {
        String texto = txtcontraseña.getText();

        TextField tempField = new TextField();
        tempField.setText(texto);
        tempField.setLayoutX(txtcontraseña.getLayoutX());
        tempField.setLayoutY(txtcontraseña.getLayoutY());
        tempField.setPrefSize(txtcontraseña.getPrefWidth(), txtcontraseña.getPrefHeight());
        tempField.setStyle(txtcontraseña.getStyle());
        tempField.setPromptText("Ingrese su contraseña");

        HBox parent = (HBox) txtcontraseña.getParent();
        int index = parent.getChildren().indexOf(txtcontraseña);
        parent.getChildren().set(index, tempField);

        this.txtcontraseñavisible = tempField;
        eyesIcon.setIconLiteral("fas-eye");
    }

    private void ocultarContraseña() {
        if (txtcontraseñavisible != null) {
            String texto = txtcontraseñavisible.getText();

            HBox parent = (HBox) txtcontraseñavisible.getParent();
            int index = parent.getChildren().indexOf(txtcontraseñavisible);
            parent.getChildren().set(index, txtcontraseña);

            txtcontraseña.setText(texto);
            txtcontraseñavisible = null;
            eyesIcon.setIconLiteral("fas-eye-slash");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void handleLogin() throws IOException {
        String username = txtusername.getText();
        String password;

        if (checkcontraseña.isSelected() && txtcontraseñavisible != null) {
            password = txtcontraseñavisible.getText();
        } else {
            password = txtcontraseña.getText();
        }

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Por favor complete todos los campos.");
            return;
        }

        Usuario usuario = usuarioDAO.autenticarUsuario(username, password);
        if (usuario != null) {
            mostrarAlerta("Exito",
                    "Login exitoso!\nBienvenido: " + usuario.getUsername() + "\nRol: " + usuario.getRol());
            autenticacioService.establecerUsuarioAutenticado(usuario);
            App.setRoot("Dashboard");
        } else {
            mostrarAlerta("Error", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    private void btnCerrar() {
        Stage stage = (Stage) topMenu_total.getScene().getWindow();
        stage.close();
    }
}

