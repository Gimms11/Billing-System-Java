package pe.utp.sifacsw.presentacion.componentes;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

import pe.utp.sifacsw.presentacion.controladores.ControllerMenu;
import pe.utp.sifacsw.presentacion.controladores.ControllerTopMenu;

public class ControllerFactory {
    public static ControllerMenu crearControllerMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ControllerFactory.class.getResource("/pe/utp/sifacsw/vistas/Menu.fxml"));
        loader.load();
        return loader.getController();
    }

    public static ControllerTopMenu crearControllerTopMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ControllerFactory.class.getResource("/pe/utp/sifacsw/vistas/TopMenu.fxml"));
        loader.load();
        return loader.getController();
    }
}

