package pe.utp.sifacsw.presentacion.componentes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Ventana modal de carga programática.
 * No depende de ningún archivo FXML externo.
 */
public class LoadingStage {

    private Stage stage;

    public LoadingStage() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(50, 50);

        Label label = new Label("Cargando...");
        label.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        VBox root = new VBox(12, spinner, label);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(160, 110);
        root.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-radius: 8;");

        stage.setScene(new Scene(root));
    }

    public void show() {
        if (stage != null) stage.show();
    }

    public void close() {
        if (stage != null) stage.close();
    }
}
