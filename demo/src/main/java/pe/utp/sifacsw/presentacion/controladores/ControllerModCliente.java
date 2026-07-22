package pe.utp.sifacsw.presentacion.controladores;

import java.util.List;

import pe.utp.sifacsw.dominio.modelos.Cliente;
import pe.utp.sifacsw.dominio.modelos.Departamento;
import pe.utp.sifacsw.dominio.modelos.Distrito;
import pe.utp.sifacsw.dominio.modelos.Provincia;
import pe.utp.sifacsw.dominio.modelos.TipoDocumento;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import pe.utp.sifacsw.aplicacion.servicios.ClienteService;
import pe.utp.sifacsw.aplicacion.servicios.TipoDocumentoService;
import pe.utp.sifacsw.aplicacion.servicios.UbigeoService;

public class ControllerModCliente {

    // --- ComboBox ---
    @FXML
    private ComboBox<TipoDocumento> comboTipoDocumento;
    @FXML
    private ComboBox<Departamento> listDepartamento;
    @FXML
    private ComboBox<Provincia> listProvincia;
    @FXML
    private ComboBox<Distrito> listDistrito;

    // --- Inputs ---
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtNumDoc;
    @FXML
    private TextField txtDireccion;

    @FXML
    private Button btnEliminActivar;

    // --- DTO ---
    private Cliente clienteSeleccionado;

    // --- Services ---
    private ClienteService clienteService;
    private TipoDocumentoService documentoService;
    private UbigeoService ubigeoService;

    /*
     * ============================================================
     * Inicialización
     * ============================================================
     */
    @FXML
    public void initialize() {
        clienteService = new ClienteService();
        documentoService = new TipoDocumentoService();
        ubigeoService = new UbigeoService();
    }

    public void setClienteSeleccionado(Cliente cliente) {
        this.clienteSeleccionado = cliente;

        cargarDatosCliente();
        configurarComboTipoDocumento();
        configurarUbigeo();
        configurarEstadoBoton();
        bloquearCampos(!clienteEstaActivo()); // <-- BLOQUEA CAMPOS SI ESTÁ DESACTIVADO
    }

    /*
     * ============================================================
     * Bloquear / Desbloquear campos
     * ============================================================
     */
    private void bloquearCampos(boolean bloquear) {

        txtNombres.setDisable(bloquear);
        txtApellidos.setDisable(bloquear);
        txtTelefono.setDisable(bloquear);
        txtCorreo.setDisable(bloquear);
        txtNumDoc.setDisable(bloquear);
        txtDireccion.setDisable(bloquear);

        comboTipoDocumento.setDisable(bloquear);
        listDepartamento.setDisable(bloquear);
        listProvincia.setDisable(bloquear);
        listDistrito.setDisable(bloquear);
    }

    /*
     * ============================================================
     * Cargar datos
     * ============================================================
     */
    private void cargarDatosCliente() {
        txtNombres.setText(clienteSeleccionado.getNombres());
        txtApellidos.setText(clienteSeleccionado.getApellidos());
        txtTelefono.setText(clienteSeleccionado.getTelefono());
        txtCorreo.setText(clienteSeleccionado.getCorreo());
        txtNumDoc.setText(clienteSeleccionado.getNumDocumento());
        txtDireccion.setText(clienteSeleccionado.getDireccion());
    }

    /*
     * ============================================================
     * Tipo Documento
     * ============================================================
     */
    private void configurarComboTipoDocumento() {
        try {
            var documentos = documentoService.cargarTipoDocumentos();
            comboTipoDocumento.setItems(FXCollections.observableArrayList(documentos));

            documentos.forEach(doc -> {
                if (doc.getIdDocumento() == clienteSeleccionado.getIdDocumento()) {
                    comboTipoDocumento.getSelectionModel().select(doc);
                }
            });

        } catch (Exception e) {
            mostrarError("Error al cargar tipos de documento",
                    "No se pudo cargar la lista.",
                    e.getMessage());
        }
    }

    /*
     * ============================================================
     * UBIGEO
     * ============================================================
     */
    private void configurarUbigeo() {
        try {
            Long idDistrito = clienteSeleccionado.getIdDistrito();
            List<Long> ids = ubigeoService.obtenerIds(idDistrito);

            Long idDep = ids.get(2);
            Long idProv = ids.get(1);
            Long idDist = ids.get(0);

            var departamentos = ubigeoService.cargarDepartamentos();
            listDepartamento.setItems(FXCollections.observableArrayList(departamentos));

            departamentos.forEach(dep -> {
                if (dep.getIdDepartamento().equals(idDep))
                    listDepartamento.getSelectionModel().select(dep);
            });

            recargarProvincias();
            listProvincia.getItems().forEach(prov -> {
                if (prov.getIdProvincia().equals(idProv))
                    listProvincia.getSelectionModel().select(prov);
            });

            recargarDistritos();
            listDistrito.getItems().forEach(dist -> {
                if (dist.getIdDistrito().equals(idDist))
                    listDistrito.getSelectionModel().select(dist);
            });

            listDepartamento.setOnAction(e -> recargarProvincias());
            listProvincia.setOnAction(e -> recargarDistritos());

        } catch (Exception e) {
            mostrarError("Error al cargar ubicación",
                    "No se pudieron cargar los datos de ubicación.",
                    e.getMessage());
        }
    }

    private void recargarProvincias() {
        listProvincia.getItems().clear();
        listDistrito.getItems().clear();

        Departamento dep = listDepartamento.getValue();
        if (dep != null) {
            var provincias = ubigeoService.cargarProvincias(dep.getIdDepartamento());
            listProvincia.setItems(FXCollections.observableArrayList(provincias));
        }
    }

    private void recargarDistritos() {
        listDistrito.getItems().clear();
        Provincia prov = listProvincia.getValue();
        if (prov != null) {
            var distritos = ubigeoService.cargarDistritos(prov.getIdProvincia());
            listDistrito.setItems(FXCollections.observableArrayList(distritos));
        }
    }

    /*
     * ============================================================
     * VALIDACIONES
     * ============================================================
     */
    private boolean validarCampos() {

        if (txtNombres.getText().isBlank() ||
                txtApellidos.getText().isBlank() ||
                txtTelefono.getText().isBlank() ||
                txtCorreo.getText().isBlank() ||
                txtNumDoc.getText().isBlank() ||
                txtDireccion.getText().isBlank()) {

            mostrarAdvertencia("Campos vacíos", "Todos los campos son obligatorios.");
            return false;
        }

        if (!txtTelefono.getText().matches("\\d{9}")) {
            mostrarAdvertencia("Teléfono inválido", "Debe tener 9 dígitos.");
            return false;
        }

        if (!txtCorreo.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            mostrarAdvertencia("Correo inválido", "Ingrese un correo válido.");
            return false;
        }

        if (comboTipoDocumento.getValue() == null) {
            mostrarAdvertencia("Documento", "Seleccione un tipo de documento.");
            return false;
        }

        Long idDoc = comboTipoDocumento.getValue().getIdDocumento();
        String doc = txtNumDoc.getText();

        if (idDoc == 1 && doc.length() != 8) {
            mostrarAdvertencia("DNI Incorrecto", "Debe tener 8 caracteres.");
            return false;
        }

        if (idDoc == 2 && doc.length() != 11) {
            mostrarAdvertencia("RUC Incorrecto", "Debe tener 11 caracteres.");
            return false;
        }

        if (listDepartamento.getValue() == null ||
                listProvincia.getValue() == null ||
                listDistrito.getValue() == null) {

            mostrarAdvertencia("Ubicación incompleta",
                    "Seleccione departamento, provincia y distrito.");
            return false;
        }

        return true;
    }

    /*
     * ============================================================
     * ACTUALIZAR CLIENTE
     * ============================================================
     */
    @FXML
    private void actualizarCliente() {

        if (!validarCampos())
            return;

        try {

            clienteSeleccionado.setNombres(capitalizar(txtNombres.getText()));
            clienteSeleccionado.setApellidos(capitalizar(txtApellidos.getText()));
            clienteSeleccionado.setTelefono(txtTelefono.getText());
            clienteSeleccionado.setCorreo(txtCorreo.getText());
            clienteSeleccionado.setNumDocumento(txtNumDoc.getText());
            clienteSeleccionado.setDireccion(txtDireccion.getText());

            clienteSeleccionado.setIdDocumento(comboTipoDocumento.getValue().getIdDocumento());
            clienteSeleccionado.setIdDistrito(listDistrito.getValue().getIdDistrito());

            clienteService.actualizarCliente(clienteSeleccionado);

            mostrarInfo("Actualización exitosa",
                    "Los datos se actualizaron correctamente.");

        } catch (Exception e) {
            mostrarError("Error al actualizar",
                    "Ocurrió un error al guardar los cambios.",
                    e.getMessage());
        }

        cerrarVentana();
    }

    /*
     * ============================================================
     * CANCELAR
     * ============================================================
     */
    @FXML
    private void cancelar() {
        cargarDatosCliente();
        configurarComboTipoDocumento();
        configurarUbigeo();
        configurarEstadoBoton();
        bloquearCampos(!clienteEstaActivo());
        mostrarInfo("Cancelado", "Se revirtieron los cambios.");
        cerrarVentana();
    }

    /*
     * ============================================================
     * ACTIVAR / DESACTIVAR
     * ============================================================
     */

    private boolean estaDesactivado(String texto) {
        return texto.equals(texto.toUpperCase());
    }

    private boolean clienteEstaActivo() {
        return !(estaDesactivado(txtNombres.getText())
                && estaDesactivado(txtApellidos.getText()));
    }

    private void configurarEstadoBoton() {
        if (clienteEstaActivo())
            btnEliminActivar.setText("Desactivar");
        else
            btnEliminActivar.setText("Activar");
    }

    @FXML
    private void onClickEliminarActivar() {

        boolean estaActivo = clienteEstaActivo();

        try {
            if (estaActivo) {
                clienteSeleccionado.setNombres(txtNombres.getText().toUpperCase());
                clienteSeleccionado.setApellidos(txtApellidos.getText().toUpperCase());
                bloquearCampos(true);
                mostrarInfo("Cliente desactivado", "El cliente ahora está DESACTIVADO.");
            } else {
                clienteSeleccionado.setNombres(capitalizar(txtNombres.getText()));
                clienteSeleccionado.setApellidos(capitalizar(txtApellidos.getText()));
                bloquearCampos(false);
                mostrarInfo("Cliente activado", "El cliente ahora está ACTIVO.");
            }

            clienteService.actualizarCliente(clienteSeleccionado);

        } catch (Exception e) {
            mostrarError("Error al actualizar estado",
                    "No se pudo cambiar el estado del cliente.",
                    e.getMessage());
        }

        cerrarVentana();
    }

    /*
     * ============================================================
     * UTILIDADES
     * ============================================================
     */
    private String capitalizar(String texto) {
        if (texto == null || texto.isBlank())
            return texto;
        texto = texto.toLowerCase();
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje, String detalles) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje + "\n\nDetalles:\n" + detalles);
        alert.showAndWait();
    }

    /*
     * ============================================================
     * Cerrar ventana
     * ============================================================
     */
    @FXML
    private void cerrarVentana() {
        btnEliminActivar.getScene().getWindow().hide();
    }
}

