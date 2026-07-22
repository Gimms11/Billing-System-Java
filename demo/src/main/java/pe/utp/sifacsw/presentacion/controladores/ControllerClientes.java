package pe.utp.sifacsw.presentacion.controladores;

import pe.utp.sifacsw.dominio.modelos.Cliente;
import pe.utp.sifacsw.dominio.modelos.Departamento;
import pe.utp.sifacsw.dominio.modelos.Provincia;
import pe.utp.sifacsw.dominio.modelos.Distrito;
import pe.utp.sifacsw.dominio.modelos.TipoDocumento;
import pe.utp.sifacsw.aplicacion.servicios.UbigeoService;
import pe.utp.sifacsw.aplicacion.servicios.ClienteService;
import pe.utp.sifacsw.aplicacion.servicios.TipoDocumentoService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;
import java.util.ArrayList;

public class ControllerClientes {

    // --- ComboBox del primer formulario ---
    @FXML
    private ComboBox<TipoDocumento> comboTipoDocumento;
    @FXML
    private ComboBox<Departamento> listDepartamento;
    @FXML
    private ComboBox<Provincia> listProvincia;
    @FXML
    private ComboBox<Distrito> listDistrito;

    // --- ComboBox del formulario de filtro ---
    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox<TipoDocumento> comboTipoDocumento1;
    @FXML
    private ComboBox<Departamento> listDepartamento1;
    @FXML
    private ComboBox<Provincia> listProvincia1;
    @FXML
    private ComboBox<Distrito> listDistrito1;

    // --- Campos de filtrado ---
    @FXML
    private TextField txtNumDoc1;
    @FXML
    private TextField txtNombres1;
    @FXML
    private TextField txtApellidos1;

    // --- TableView y sus columnas ---
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, String> col_nombres;
    @FXML
    private TableColumn<Cliente, String> col_apellidos;
    @FXML
    private TableColumn<Cliente, String> col_telefono;
    @FXML
    private TableColumn<Cliente, String> col_tipoDoc;
    @FXML
    private TableColumn<Cliente, String> col_numDoc;
    @FXML
    private TableColumn<Cliente, String> col_distrito;
    @FXML
    private TableColumn<Cliente, Hyperlink> col_modificar;

    // --- Datos de insercción del cliente ---

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
    private TextArea txtDireccion;

    // --- Servicios ---
    private UbigeoService ubigeoService;
    private TipoDocumentoService tipoDocumentoService;
    private ClienteService clienteService;

    /** ProgressIndicator central reutilizado durante cargas async */
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * Método que se ejecuta automáticamente al cargar la ventana (FXML).
     * NUNCA ejecuta queries directamente — delega todo a Tasks en segundo plano.
     */
    @FXML
    public void initialize() {
        try {
            this.ubigeoService = new UbigeoService();
            this.tipoDocumentoService = new TipoDocumentoService();
            this.clienteService = new ClienteService();

            if (progressIndicator != null) progressIndicator.setVisible(false);

            // Configura bindings de columnas (sin queries)
            configurarTabla();

            // Lanzar carga asíncrona de departamentos (ambos formularios)
            cargarDepartamentosAsync(listDepartamento, listProvincia, listDistrito);
            cargarDepartamentosAsync(listDepartamento1, listProvincia1, listDistrito1);

            // Lanzar carga asíncrona de tipos de documento
            cargarTiposDocumentoAsync(comboTipoDocumento);
            cargarTiposDocumentoAsync(comboTipoDocumento1);

            tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
                if (newTab != null) { /* extensible */ }
            });

        } catch (Exception e) {
            mostrarError("Error de inicialización", "No se pudieron cargar los datos iniciales.", e.getMessage());
        }
    }

    /**
     * Carga departamentos en segundo plano y configura listeners encadenados para
     * provincias y distritos — todo fuera del JavaFX Application Thread.
     */
    private void cargarDepartamentosAsync(
            ComboBox<Departamento> comboDep,
            ComboBox<Provincia> comboProv,
            ComboBox<Distrito> comboDist) {

        Task<List<Departamento>> task = new Task<>() {
            @Override protected List<Departamento> call() {
                return ubigeoService.cargarDepartamentos();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() -> {
            comboDep.setItems(FXCollections.observableArrayList(task.getValue()));

            comboDep.setOnAction(ev -> {
                Departamento dep = comboDep.getValue();
                if (dep == null) return;
                comboProv.getItems().clear();
                comboDist.getItems().clear();

                Task<List<Provincia>> tProv = new Task<>() {
                    @Override protected List<Provincia> call() {
                        return ubigeoService.cargarProvincias(dep.getIdDepartamento());
                    }
                };
                tProv.setOnSucceeded(ep -> Platform.runLater(() -> {
                    comboProv.setItems(FXCollections.observableArrayList(tProv.getValue()));
                    comboProv.setOnAction(ep2 -> {
                        Provincia prov = comboProv.getValue();
                        if (prov == null) return;
                        comboDist.getItems().clear();
                        Task<List<Distrito>> tDist = new Task<>() {
                            @Override protected List<Distrito> call() {
                                return ubigeoService.cargarDistritos(prov.getIdProvincia());
                            }
                        };
                        tDist.setOnSucceeded(ed -> Platform.runLater(() ->
                            comboDist.setItems(FXCollections.observableArrayList(tDist.getValue()))
                        ));
                        new Thread(tDist).start();
                    });
                }));
                new Thread(tProv).start();
            });
        }));
        task.setOnFailed(e -> Platform.runLater(() ->
            mostrarError("Error al cargar departamentos",
                "No se pudieron cargar los datos de ubicación.",
                task.getException().getMessage())
        ));
        new Thread(task).start();
    }

    /**
     * Carga tipos de documento en segundo plano.
     */
    private void cargarTiposDocumentoAsync(ComboBox<TipoDocumento> combo) {
        Task<List<TipoDocumento>> task = new Task<>() {
            @Override protected List<TipoDocumento> call() {
                return tipoDocumentoService.cargarTipoDocumentos();
            }
        };
        task.setOnSucceeded(e -> Platform.runLater(() ->
            combo.setItems(FXCollections.observableArrayList(task.getValue()))
        ));
        task.setOnFailed(e -> Platform.runLater(() ->
            mostrarError("Error al cargar documentos",
                "No se pudieron cargar los tipos de documento.",
                task.getException().getMessage())
        ));
        new Thread(task).start();
    }

    /**
     * Obtiene el ID del distrito del primer formulario
     */
    public Long obtenerIdDistritoSeleccionado() {
        Distrito d = listDistrito.getValue();
        return (d != null) ? d.getIdDistrito() : null;
    }

    /**
     * Obtiene el ID del distrito del segundo formulario
     */
    public Long obtenerIdDistritoSeleccionado1() {
        Distrito d = listDistrito1.getValue();
        return (d != null) ? d.getIdDistrito() : null;
    }

    /**
     * Obtiene el ID del tipo de documento del primer ComboBox
     */
    public Long obtenerIdTipoDocumentoSeleccionado() {
        TipoDocumento doc = comboTipoDocumento.getValue();
        return (doc != null) ? doc.getIdDocumento() : null;
    }

    /**
     * Obtiene el ID del tipo de documento del segundo ComboBox
     */
    public Long obtenerIdTipoDocumentoSeleccionado1() {
        TipoDocumento doc = comboTipoDocumento1.getValue();
        return (doc != null) ? doc.getIdDocumento() : null;
    }

    /**
     * Método de ejemplo: insertar cliente (a completar)
     * Aquí puedes agregar validaciones antes de insertar a la BD.
     */
    @FXML
    public void insertarCliente() {

        // Validaciones
        String telefono = txtTelefono.getText();
        if (telefono.length() > 9) {
            mostrarAdvertencia("Datos incorrectos", "El teléfono no puede tener más de 9 caracteres.");
            return;
        }

        String correo = txtCorreo.getText();
        if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            mostrarAdvertencia("Datos incorrectos", "El formato del correo electrónico no es válido.");
            return;
        }

        Long idDocumento = obtenerIdTipoDocumentoSeleccionado();
        String numDoc = txtNumDoc.getText();

        if (idDocumento == 1 && numDoc.length() != 8) {
            mostrarAdvertencia("Datos incorrectos", "El DNI debe tener 8 caracteres.");
            return;
        }

        if (idDocumento == 2 && numDoc.length() != 11) {
            mostrarAdvertencia("Datos incorrectos", "El RUC debe tener 11 caracteres.");
            return;
        }

        Cliente cliente = new Cliente();

        cliente.setNombres(txtNombres.getText());
        cliente.setApellidos(txtApellidos.getText());
        cliente.setTelefono(telefono);
        cliente.setCorreo(correo);
        cliente.setDireccion(txtDireccion.getText());
        cliente.setNumDocumento(numDoc);
        cliente.setIdDistrito(obtenerIdDistritoSeleccionado());
        cliente.setIdDocumento(idDocumento);

        try {
            clienteService.insertarCliente(cliente);
            mostrarInfo("Registro exitoso", "El cliente se ha registrado correctamente.");

            // Limpiar campos después de inserción exitosa
            limpiarCampos();

            // Actualizar la tabla
            cargarDatosTabla();
        } catch (Exception e) {
            mostrarError("Error al registrar", "No se pudo registrar el cliente.", e.getMessage());
        }
    }

    // --- MÉTODOS DE ALERTAS (ventanas emergentes) ---

    /**
     * Muestra una alerta de información
     */
    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de advertencia
     */
    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de error con detalles
     */
    private void mostrarError(String titulo, String mensaje, String detalles) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje + "\n\nDetalles: " + detalles);
        alert.showAndWait();
    }

    /**
     * Configura la tabla de clientes y sus columnas
     */
    private void configurarTabla() {
        // Configurar las columnas
        col_nombres.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null)
                return new SimpleStringProperty("");
            return new SimpleStringProperty(
                    cellData.getValue().getNombres() != null ? cellData.getValue().getNombres() : "");
        });

        col_apellidos.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null)
                return new SimpleStringProperty("");
            return new SimpleStringProperty(
                    cellData.getValue().getApellidos() != null ? cellData.getValue().getApellidos() : "");
        });

        col_telefono.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null)
                return new SimpleStringProperty("");
            return new SimpleStringProperty(
                    cellData.getValue().getTelefono() != null ? cellData.getValue().getTelefono() : "");
        });

        col_numDoc.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null)
                return new SimpleStringProperty("");
            return new SimpleStringProperty(
                    cellData.getValue().getNumDocumento() != null ? cellData.getValue().getNumDocumento() : "");
        });

        // Configurar columna de tipo documento
        col_tipoDoc.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null)
                return new SimpleStringProperty("");
            return new SimpleStringProperty(
                    cellData.getValue().getNombreTipoDocumento() != null ? cellData.getValue().getNombreTipoDocumento()
                            : "");
        });

        // Configurar columna de distrito
        col_distrito.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null)
                return new SimpleStringProperty("");
            return new SimpleStringProperty(
                    cellData.getValue().getNombreDistrito() != null ? cellData.getValue().getNombreDistrito() : "");
        });

        // Configurar columna de modificar
        col_modificar.setCellFactory(param -> new TableCell<>() {
            private final Hyperlink link = new Hyperlink("Modificar");
            {
                link.setStyle("-fx-text-fill: blue;"); // Estilo para que parezca un enlace
            }

            @Override
            protected void updateItem(Hyperlink item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    link.setOnAction(event -> {
                        Cliente cliente = getTableView().getItems().get(getIndex());
                        abrirVentanaModificar(cliente);
                    });
                    setGraphic(link);
                }
            }
        });

        // Cargar datos iniciales
        cargarDatosTabla();
    }

    /**
     * Recarga la tabla de clientes en un hilo secundario.
     * Muestra ProgressIndicator durante la carga.
     */
    private void cargarDatosTabla() {
        if (progressIndicator != null) progressIndicator.setVisible(true);
        tablaClientes.setDisable(true);

        Task<List<Cliente>> task = new Task<>() {
            @Override
            protected List<Cliente> call() {
                List<Cliente> clientes = clienteService.obtenerTodos();
                return clientes != null ? clientes : new ArrayList<>();
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            tablaClientes.setItems(FXCollections.observableArrayList(task.getValue()));
            tablaClientes.refresh();
            tablaClientes.setDisable(false);
            if (progressIndicator != null) progressIndicator.setVisible(false);
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            tablaClientes.setDisable(false);
            if (progressIndicator != null) progressIndicator.setVisible(false);
            mostrarError("Error al cargar datos",
                "No se pudieron cargar los datos de los clientes.",
                task.getException().getMessage());
        }));

        new Thread(task).start();
    }

    /**
     * Limpia todos los campos del formulario de registro
     */
    private void limpiarCampos() {
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtNumDoc.clear();
        txtDireccion.clear();

        limpiarCombo(comboTipoDocumento);
        limpiarCombo(listDepartamento);
        limpiarCombo(listProvincia);
        limpiarCombo(listDistrito);
    }

    private <T> void limpiarCombo(ComboBox<T> comboBox) {
        comboBox.getSelectionModel().clearSelection();
        comboBox.setValue(null);

        comboBox.setButtonCell(new javafx.scene.control.ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? comboBox.getPromptText() : item.toString());
            }
        });
    }

    /**
     * Abre la ventana de modificación de cliente
     */
    private void abrirVentanaModificar(Cliente cliente) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/utp/sifacsw/vistas/ModCliente.fxml"));
            Parent root = loader.load();

            // Obtener el controller de la ventana emergente
            ControllerModCliente controller = loader.getController();

            // Enviar el cliente seleccionado al controller
            controller.setClienteSeleccionado(cliente);

            // Crear ventana modal
            Stage stage = new Stage();
            stage.setTitle("Modificar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Modal: bloquea la ventana principal
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.showAndWait(); // Espera a que se cierre

            // Luego de cerrarse, recargar la tabla
            cargarDatosTabla();

        } catch (Exception e) {
            mostrarError("Error al abrir ventana",
                    "No se pudo abrir la ventana de modificación.",
                    e.getMessage());
        }
    }

    /**
     * Método para filtrar clientes
     */
    @FXML
    private void filtrarClientes() {
        try {
            int index = tabPane.getSelectionModel().getSelectedIndex();
            String numDocumento = null, nombres = null, apellidos = null;
            Integer idDistrito = null, idDocumento = null;

            switch (index) {
                case 0:
                    // Filtro solo por Documento
                    nombres = apellidos = null;
                    idDistrito = null;

                    // Comprobación de tipo de documento y número
                    TipoDocumento tipoDoc = comboTipoDocumento1.getValue();
                    numDocumento = txtNumDoc1.getText().trim();

                    // Si ambos campos están vacíos, mostrar todos
                    if (tipoDoc == null && numDocumento.isEmpty()) {
                        cargarDatosTabla();
                        return;
                    }

                    // Si hay tipo de documento seleccionado, validar número
                    if (tipoDoc != null) {
                        if (!numDocumento.isEmpty()) {
                            // Validación de longitud según tipo
                            if (tipoDoc.getIdDocumento() == 1 && numDocumento.length() != 8) {
                                mostrarAdvertencia("Datos incorrectos", "El DNI debe tener 8 caracteres.");
                                return;
                            }
                            if (tipoDoc.getIdDocumento() == 2 && numDocumento.length() != 11) {
                                mostrarAdvertencia("Datos incorrectos", "El RUC debe tener 11 caracteres.");
                                return;
                            }
                        }
                        idDocumento = tipoDoc.getIdDocumento().intValue();
                    }
                    break;

                case 1:
                    // Filtro por nombres y apellidos
                    numDocumento = null;
                    idDistrito = idDocumento = null;

                    nombres = txtNombres1.getText().trim();
                    apellidos = txtApellidos1.getText().trim();

                    // Si ambos campos están vacíos, mostrar todos los clientes
                    if (nombres.isEmpty() && apellidos.isEmpty()) {
                        cargarDatosTabla();
                        return;
                    }
                    break;

                case 2:
                    // Filtro por Ubicación
                    numDocumento = nombres = apellidos = null;
                    idDocumento = null;

                    Distrito distrito = listDistrito1.getValue();

                    // Si no hay distrito seleccionado, mostrar todos los clientes
                    if (distrito == null) {
                        cargarDatosTabla();
                        return;
                    }

                    idDistrito = distrito.getIdDistrito().intValue();
                    break;

                default:
                    return;
            }

            // Aplicar filtros en segundo plano
            final String fNumDoc = numDocumento;
            final String fNombres = nombres;
            final String fApellidos = apellidos;
            final Integer fDistrito = idDistrito;
            final Integer fDocumento = idDocumento;

            if (progressIndicator != null) progressIndicator.setVisible(true);
            tablaClientes.setDisable(true);

            Task<List<Cliente>> filtroTask = new Task<>() {
                @Override
                protected List<Cliente> call() {
                    return clienteService.filtrarClientes(
                        fNumDoc, fNombres, fApellidos, fDistrito, fDocumento);
                }
            };

            filtroTask.setOnSucceeded(ev -> Platform.runLater(() -> {
                tablaClientes.setDisable(false);
                if (progressIndicator != null) progressIndicator.setVisible(false);
                List<Cliente> resultado = filtroTask.getValue();
                if (resultado.isEmpty()) {
                    mostrarInfo("Sin resultados", "No se encontraron clientes con los criterios especificados.");
                }
                tablaClientes.setItems(FXCollections.observableArrayList(resultado));
                tablaClientes.refresh();
            }));

            filtroTask.setOnFailed(ev -> Platform.runLater(() -> {
                tablaClientes.setDisable(false);
                if (progressIndicator != null) progressIndicator.setVisible(false);
                mostrarError("Error al filtrar",
                    "No se pudieron aplicar los filtros especificados.",
                    filtroTask.getException().getMessage());
            }));

            new Thread(filtroTask).start();

        } catch (Exception e) {
            mostrarError("Error al filtrar",
                    "No se pudieron aplicar los filtros especificados.",
                    e.getMessage());
        }
    }
}

