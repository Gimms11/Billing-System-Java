package pe.utp.sifacsw.presentacion.controladores;

import java.util.List;
import java.util.ArrayList;

import pe.utp.sifacsw.dominio.modelos.Cliente;
import pe.utp.sifacsw.dominio.modelos.Comprobante;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import pe.utp.sifacsw.aplicacion.servicios.ClienteService;
import pe.utp.sifacsw.aplicacion.servicios.ComprobanteService;
import pe.utp.sifacsw.aplicacion.servicios.XMLService;
import pe.utp.sifacsw.aplicacion.servicios.genPDFService;

public class ControllerHistorial {
    
    // --- TableView y sus columnas ---
    @FXML private TableView<Comprobante> tablaComprobantes;
    @FXML private TableColumn<Comprobante, String> col_serie;
    @FXML private TableColumn<Comprobante, String> col_docCliente;
    @FXML private TableColumn<Comprobante, String> col_nomClientes;
    @FXML private TableColumn<Comprobante, String> col_distritoCliente;
    @FXML private TableColumn<Comprobante, String> col_direccionEnvio;
    @FXML private TableColumn<Comprobante, String> col_fechaEmision;
    @FXML private TableColumn<Comprobante, String> col_medioPago;
    @FXML private TableColumn<Comprobante, String> col_totalFinal;

    // === CAMPOS DE FILTRO ===
    @FXML private ComboBox<String> listPeriodo;
    @FXML private TextField txtFactura;
    @FXML private TextField txtDocumento;

    // === SERVICIOS ===
    private ComprobanteService comprobanteService;

    @FXML
    public void initialize() {
        try {
            // Inicialización de servicios
            this.comprobanteService = new ComprobanteService();

            // Configurar el listener para txtFactura
            configurarListeners();

            // Configuración de la tabla
            configurarTabla();

            // Cargar datos iniciales (últimos 7 días por defecto)
            cargarDatosTabla();

        } catch (Exception e) {
            mostrarError("Error de inicialización", 
                "No se pudo inicializar la ventana de historial.", 
                e.getMessage());
        }
    }

    private void configurarListeners() {
        // Listener para el campo de número de factura
        txtFactura.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean hasSerieFilter = newValue != null && !newValue.trim().isEmpty();
            
            // Deshabilitar/habilitar controles según el contenido de txtFactura
            listPeriodo.setDisable(hasSerieFilter);
            txtDocumento.setDisable(hasSerieFilter);
            
            // Limpiar controles si txtFactura tiene contenido
            if (hasSerieFilter) {
                limpiarCombo(listPeriodo);
                txtDocumento.clear();
            } else {
                // Cuando se limpia txtFactura, habilitar los otros controles
                listPeriodo.setDisable(false);
                txtDocumento.setDisable(false);
            }
        });
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

    private void configurarTabla() {
        //Añadir Ninguno, hoy, hace una semana, hace un mes, hace un año a listPeriodo
        listPeriodo.getItems().addAll("hoy", "una semana", "un mes", "un año", "ninguno");

        // Configurar columna Serie
        col_serie.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getSerie() != null ? 
                cellData.getValue().getSerie() : ""
            );
        });

        // Configurar columna documento Cliente
        col_docCliente.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getClienteDocumento() != null ? 
                cellData.getValue().getClienteDocumento () : ""
            );
        });

        // Configurar columna Medio de Pago
        col_medioPago.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getMedioPago() != null ? 
                cellData.getValue().getMedioPago() : ""
            );
        });

        // Configurar columna Nombre Cliente
        col_nomClientes.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getNombreCliente() != null ? 
                cellData.getValue().getNombreCliente() : ""
            );
        });

        // Configurar columna Distrito Cliente
        col_distritoCliente.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getClienteDistrito() != null ? 
                cellData.getValue().getClienteDistrito() : ""
            );
        });

        // Configurar columna Dirección de Envío
        col_direccionEnvio.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getDireccionEnvio() != null ? 
                cellData.getValue().getDireccionEnvio() : ""
            );
        });

        // Configurar columna Fecha de Emisión
        col_fechaEmision.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getFechaEmision() != null ? 
                cellData.getValue().getFechaEmision().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""
            );
        });

        // Configurar columna Total Final
        col_totalFinal.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getTotalFinal() != null ? 
                String.format("S/ %.2f", cellData.getValue().getTotalFinal()) : ""
            );
        });
    }

    // === MÉTODOS DE CARGA DE DATOS ===

    private void cargarDatosTabla(List<Comprobante> comprobantes) {
        try {
            if (comprobantes == null) {
                comprobantes = new ArrayList<>();
            }
            tablaComprobantes.setItems(FXCollections.observableArrayList(comprobantes));
            tablaComprobantes.refresh();
        } catch (Exception e) {
            mostrarError("Error al cargar datos", 
                        "No se pudieron cargar los comprobantes.", 
                        e.getMessage());
        }
    }

    public void cargarDatosTabla() {
        try {
            List<Comprobante> comprobantes = comprobanteService.listarComprobante();
            cargarDatosTabla(comprobantes);
            actualizarResumenVentas();
        } catch (Exception e) {
            mostrarError("Error al cargar datos", 
                        "No se pudieron cargar los comprobantes.", 
                        e.getMessage());
        }
    }

    // === MÉTODO DE FILTRO ===
    @FXML
    private void filtrarComprobantes() {
        // 1. Obtener valores de los filtros
        Integer periodoSelect = null;
        if (listPeriodo != null && !listPeriodo.getSelectionModel().isEmpty()) {
            int selectedIndex = listPeriodo.getSelectionModel().getSelectedIndex();
            // Si se selecciona "ninguno" (índice 4), dejamos periodoSelect como null
            if (selectedIndex != 4) {
                periodoSelect = selectedIndex;
            }
        }

        // 2. Obtener NumFactura y numDocumento
        String numFactura = txtFactura.getText().trim();
        String numDocumento = txtDocumento.getText().trim();

        try {
            // Solo enviar valores si no están vacíos
            String docFiltro = numDocumento.isEmpty() ? null : numDocumento;
            String serieFiltro = numFactura.isEmpty() ? null : numFactura;
            
            List<Comprobante> comprobantes = comprobanteService.listarComprobanteFiltro(
                periodoSelect, // Ya es Integer, no necesita casting
                docFiltro,    // Filtro por número de documento del cliente
                serieFiltro   // Filtro por número de serie/factura
            );
            cargarDatosTabla(comprobantes);
            actualizarResumenVentas();
        } catch (Exception e) {
            mostrarError("Error al cargar datos", 
                        "No se pudieron cargar los comprobantes.", 
                        e.getMessage());
        }
    }

    // === MÉTODO PARA GENERAR PDF ===

    @FXML
    private void generarPDFComprobante() {
        Comprobante comprobanteSeleccionado = tablaComprobantes.getSelectionModel().getSelectedItem();
        if (comprobanteSeleccionado == null) {
            mostrarAdvertencia("Selección requerida", "Por favor, seleccione un comprobante para generar el PDF.");
            return;
        }

        try {
            genPDFService gen = new genPDFService();
            gen.generarPDF(comprobanteSeleccionado);

            mostrarInfo("PDF generado", "El PDF del comprobante ha sido generado exitosamente.");
        } catch (Exception e) {
            mostrarError("Error al generar PDF", 
                        "No se pudo generar el PDF del comprobante.", 
                        e.getMessage());
        }
    }

    @FXML
    private void generarYEnviarXMLComprobante() {
        // 1. Obtener comprobante seleccionado
        Comprobante comprobanteSeleccionado = tablaComprobantes
            .getSelectionModel().getSelectedItem();
        
        // 2. Validar selección
        if (comprobanteSeleccionado == null) {
            mostrarAdvertencia(
                "Selección requerida",
                "Por favor, seleccione un comprobante."
            );
            return;
        }
        
        ClienteService clienteService = new ClienteService();
        Cliente cliente = clienteService.obtenerPorId(comprobanteSeleccionado.getIdCliente());
        
        // 3. Validar que el cliente tenga al menos un correo
        String correoDefault = cliente.getCorreo();
        if (correoDefault == null || correoDefault.trim().isEmpty()) {
            mostrarAdvertencia(
                "Email no disponible",
                "El cliente no tiene email registrado."
            );
            return;
        }
        
        // 4. Mostrar panel de selección de correo
        String correoDestino = mostrarPanelSeleccionCorreo(correoDefault);
        if (correoDestino == null) {
            // Usuario canceló
            return;
        }
        
        // 5. Usar el FACADE para generar y enviar
        try {
            XMLService xmlService = new XMLService();
            xmlService.generarYEnviarXML(comprobanteSeleccionado, correoDestino);
            
            // 6. Mostrar éxito
            mostrarInfo(
                "XML generado",
                "El XML ha sido generado y el correo enviado exitosamente.\n\n" +
                "Destinatario: " + correoDestino
            );
        } catch (Exception e) {
            mostrarError(
                "Error al generar XML",
                "No se pudo generar el XML del comprobante.",
                e.getMessage()
            );
        }
    }

    // === MÉTODOS DE PARA SACAR DATOS DE LISTA ===
    @FXML private Label txtCantVentas;
    @FXML private Label txtTotalVentas;
    @FXML private Label txtTotalImpuestos;
    @FXML private Label txtTotalFinal;

    private void actualizarResumenVentas() {
        List<Comprobante> comprobantes = tablaComprobantes.getItems();
        if (comprobantes == null || comprobantes.isEmpty()) {
            txtCantVentas.setText("0");
            txtTotalVentas.setText("S/ 0.00");
            txtTotalImpuestos.setText("S/ 0.00");
            txtTotalFinal.setText("S/ 0.00");
            return;
        }

        int cantidadVentas = comprobantes.size();
        double totalVentas = 0.0;
        double totalImpuestos = 0.0;
        double totalFinal = 0.0;

        for (Comprobante c : comprobantes) {
            if (c.getDevengado() != null) {
                totalVentas += c.getDevengado().doubleValue();
            }
            if (c.getTotalFinal() != null || c.getDevengado() != null) {
                totalImpuestos += c.getTotalFinal().doubleValue() - c.getDevengado().doubleValue();
            }
            if (c.getTotalFinal() != null) {
                totalFinal += c.getTotalFinal().doubleValue();
            }
        }

        txtCantVentas.setText(String.valueOf(cantidadVentas));
        txtTotalVentas.setText(String.format("S/ %.2f", totalVentas));
        txtTotalImpuestos.setText(String.format("S/ %.2f", totalImpuestos));
        txtTotalFinal.setText(String.format("S/ %.2f", totalFinal));
    }

    // === MÉTODOS DE ALERTA ===

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje, String detalles) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje + "\n\nDetalles: " + detalles);
        alert.showAndWait();
    }

    // === MÉTODOS PARA SELECCIONAR CORREO ===

    /**
     * Muestra un panel de diálogo para que el usuario elija entre
     * enviar el XML al correo por defecto o a uno personalizado.
     * 
     * @param correoDefault El correo por defecto del cliente
     * @return El correo elegido o null si el usuario canceló
     */
    private String mostrarPanelSeleccionCorreo(String correoDefault) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Seleccionar destinatario");
        dialog.setHeaderText("¿A qué correo desea enviar el XML?");
        dialog.setResizable(true);
        
        // Crear controles
        RadioButton rbCorreoDefault = new RadioButton("Correo por defecto del cliente");
        RadioButton rbCorreoCustom = new RadioButton("Correo personalizado");
        ToggleGroup group = new ToggleGroup();
        rbCorreoDefault.setToggleGroup(group);
        rbCorreoCustom.setToggleGroup(group);
        rbCorreoDefault.setSelected(true);
        
        Label lblCorreoDefault = new Label("Correo: " + correoDefault);
        lblCorreoDefault.setStyle("-fx-text-fill: #666; -fx-font-size: 11;");
        
        TextField txtCorreoCustom = new TextField();
        txtCorreoCustom.setPromptText("Ej: cliente@email.com");
        txtCorreoCustom.setDisable(true);
        
        // Listener para habilitar/deshabilitar campo de texto
        rbCorreoCustom.selectedProperty().addListener((obs, oldVal, newVal) -> {
            txtCorreoCustom.setDisable(!newVal);
            if (newVal) {
                txtCorreoCustom.requestFocus();
            }
        });
        
        rbCorreoDefault.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                txtCorreoCustom.setDisable(true);
                txtCorreoCustom.clear();
            }
        });
        
        // Crear contenedor
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));
        vbox.getChildren().addAll(
            rbCorreoDefault,
            lblCorreoDefault,
            new Separator(),
            rbCorreoCustom,
            txtCorreoCustom
        );
        
        dialog.getDialogPane().setContent(vbox);
        
        // Agregar botones
        ButtonType btnOK = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOK, btnCancelar);
        
        // Configurar validación
        Button botonEnviar = (Button) dialog.getDialogPane().lookupButton(btnOK);
        botonEnviar.setDisable(false);
        
        // Listener para validar correo personalizado
        txtCorreoCustom.textProperty().addListener((obs, oldVal, newVal) -> {
            if (rbCorreoCustom.isSelected()) {
                boolean esValido = esCorreoValido(newVal);
                botonEnviar.setDisable(!esValido);
            }
        });
        
        // Listener para el cambio de selección de radio button
        rbCorreoDefault.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                botonEnviar.setDisable(false);
            }
        });
        
        // Configurar resultado
        dialog.setResultConverter(buttonType -> {
            if (buttonType == btnOK) {
                if (rbCorreoDefault.isSelected()) {
                    return correoDefault;
                } else if (rbCorreoCustom.isSelected()) {
                    String correoCustom = txtCorreoCustom.getText().trim();
                    if (esCorreoValido(correoCustom)) {
                        return correoCustom;
                    }
                }
            }
            return null;
        });
        
        return dialog.showAndWait().orElse(null);
    }

    /**
     * Valida si el formato del correo es correcto.
     * 
     * @param correo El correo a validar
     * @return true si el correo es válido, false en caso contrario
     */
    private boolean esCorreoValido(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        // Expresión regular simple para validar email
        return correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}

