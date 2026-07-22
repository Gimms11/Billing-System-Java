package pe.utp.sifacsw.presentacion.controladores;

import pe.utp.sifacsw.dominio.modelos.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import pe.utp.sifacsw.aplicacion.servicios.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class ControllerFacturas {

    // === ELEMENTOS DE LA VISTA vistosa ===
    @FXML
    private ComboBox<Producto> listProductos;
    @FXML
    private ComboBox<MedioPago> listMediosPago;

    @FXML
    private TextField prodPrecio;
    @FXML
    private TextField prodStock;
    @FXML
    private TextField prodCat;
    @FXML
    private TextField prodAfec;
    @FXML
    private TextField prodUnidad;
    @FXML
    private TextField prodImp;

    @FXML
    private TextField numDocumento;
    @FXML
    private TextField nomCliente;
    @FXML
    private TextField apeCliente;

    @FXML
    private TextField txtNumSerie;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtFechaEmision;
    @FXML
    private TextField txtComprobante;

    @FXML
    private TableView<DetalleComprobante> listDetalles;

    @FXML
    private TableColumn<DetalleComprobante, String> colNameProducto;
    @FXML
    private TableColumn<DetalleComprobante, Integer> colCantidad;
    @FXML
    private TableColumn<DetalleComprobante, BigDecimal> colPrecioUnit;
    @FXML
    private TableColumn<DetalleComprobante, BigDecimal> colSubtotal;
    @FXML
    private TableColumn<DetalleComprobante, BigDecimal> colTotal;

    // === SERVICIOS Y LISTAS ===
    private ProductoService productoService;
    private AfectacionService afectacionService;
    private CategoriaService categoriaService;
    private ClienteService clienteService;
    private MedioPagoService medioPagoService;
    private TipoComprService tipoComprobanteService;
    private ComprobanteService comprobanteService;
    private DetalleComprobanteService detalleComprobanteService;

    private List<AfectacionProductos> listAfectacionProductos;
    private List<CategoriaProductos> listCategoriaProductos;
    private List<Cliente> listClientes;
    private List<Comprobante> listComprobantes;
    private List<TipoComprobante> listTipoComprobantes;
    private TipoComprobante tipocomprobanteActual;

    private List<DetalleComprobante> listaDetalle;
    private List<Producto> productosEnDetalle;  // Lista temporal para mantener los productos agregados

    // =============================================================
    // INITIALIZE
    // =============================================================
    @FXML
    public void initialize() {
        try {
            // Generar listas vacias
            listaDetalle = new ArrayList<>();
            productosEnDetalle = new ArrayList<>();

            // Inyección manual de servicios
            this.productoService = new ProductoService();
            this.afectacionService = new AfectacionService();
            this.categoriaService = new CategoriaService();
            this.clienteService = new ClienteService();
            this.medioPagoService = new MedioPagoService();
            this.tipoComprobanteService = new TipoComprService();
            this.comprobanteService = new ComprobanteService();
            this.detalleComprobanteService = new DetalleComprobanteService();

            // Cargar datos base
            this.listAfectacionProductos = afectacionService.listarAfectaciones();
            this.listCategoriaProductos = categoriaService.listarCategorias();
            this.listClientes = clienteService.obtenerTodos();
            this.listComprobantes = comprobanteService.listarComprobante();
            this.listTipoComprobantes = tipoComprobanteService.listarTipoComprobantes();

            // Configurar combos
            configurarComboProd(listProductos);
            configurarComboMedioPago(listMediosPago);
            configurarTabla();

            // Listener para el producto seleccionado
            listProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null)
                    mostrarDatosProducto(newVal);
            });

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error de inicialización",
                    "Ocurrió un error al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =============================================================
    // MÉTODOS DE CONFIGURACIÓN
    // =============================================================

    /** Configurar la tabla y las columnas de la tabla */
    private void configurarTabla() {
        // Configurar columnas de la tabla
        colNameProducto.setCellValueFactory(data -> {
            long idProducto = data.getValue().getIdProducto();
            return new javafx.beans.property.SimpleStringProperty(
                productosEnDetalle.stream()
                    .filter(p -> p.getIdProducto() == idProducto)
                    .map(Producto::getNombre)
                    .findFirst()
                    .orElse(""));
        });
        colCantidad.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(
                data.getValue().getCantidadProductos()).asObject());
        colPrecioUnit.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(
                data.getValue().getPrecioUnitario()));
        colSubtotal.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(
                data.getValue().getSubtotal()));
        colTotal.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(
                data.getValue().getTotal()));

        // ✅ Mantener lista observable viva
        listDetalles.setItems(FXCollections.observableArrayList());
    }

    /** Configura el ComboBox de productos con los datos del servicio */
    private void configurarComboProd(ComboBox<Producto> combo) {
        List<Producto> productos = productoService.obtenerTodos();

        // Nueva lista filtrada
        List<Producto> productosFiltrados = new ArrayList<>();

        for (Producto p : productos) {
            if (p.getNombre() == null) continue;

            String nombre = p.getNombre().trim();

            // Verifica si está completamente en MAYÚSCULAS
            if (nombre.equals(nombre.toUpperCase())) {
                // Si está en mayúsculas completas, NO se agrega
                continue;
            }

            // Se agrega normalmente
            productosFiltrados.add(p);
        }

        combo.setItems(FXCollections.observableArrayList(productosFiltrados));
    }

    /** Configura el ComboBox de medios de Pago del servicio */
    private void configurarComboMedioPago(ComboBox<MedioPago> combo) {
        var tipos = medioPagoService.listarMedioPagos();
        combo.setItems(FXCollections.observableArrayList(tipos));
    }

    // =============================================================
    // EVENTOS DE INTERFAZ
    // =============================================================

    /** Muestra la información del producto seleccionado en los campos */
    private void mostrarDatosProducto(Producto producto) {
        prodPrecio.setText(String.valueOf(producto.getPrecio()));
        prodStock.setText(String.valueOf(producto.getStock()));
        prodUnidad.setText(producto.getUnidadMedida());

        // Buscar nombres de afectación y categoría
        String nombreAfectacion = listAfectacionProductos.stream()
                .filter(a -> a.getIdAfectacion().equals(producto.getIdTipoAfectacion()))
                .map(AfectacionProductos::getNombreAfectacion)
                .findFirst().orElse("No encontrado");

        String nombreCategoria = listCategoriaProductos.stream()
                .filter(c -> c.getIdCategoria().equals(producto.getIdCategoria()))
                .map(CategoriaProductos::getNombreCategoria)
                .findFirst().orElse("No encontrado");

        prodAfec.setText(nombreAfectacion);
        prodCat.setText(nombreCategoria);
        prodImp.setText(producto.getIdTipoAfectacion() == 1 ? "IGV" : "Ninguno");
    }

    /** Busca un cliente por número de documento */
    @FXML
    private void buscarCliente() {
        // Si ya hay un comprobante en proceso, no permitir buscar otro cliente
        if (!listDetalles.getItems().isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Operación no permitida", 
                "Ya hay un comprobante en proceso. Complete la generación del comprobante actual.");
            return;
        }

        String numDoc = numDocumento.getText();

        // Validación básica
        if (numDoc == null || numDoc.trim().isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Dato faltante", "Por favor ingrese un número de documento.");
            return;
        }

        if (listClientes == null || listClientes.isEmpty()) {
            mostrarAlerta(AlertType.ERROR, "Error de datos", "No hay clientes cargados en memoria.");
            return;
        }

        // Buscar cliente
        Cliente cliente = listClientes.stream()
                .filter(c -> c.getNumDocumento() != null && c.getNumDocumento().trim().equals(numDoc.trim()))
                .findFirst()
                .orElse(null);

        // Mostrar resultado
        if (cliente != null) {
            // Verificar si el cliente está desactivado (todo en mayúsculas)
            boolean nombreDesactivado = esTodoMayusculas(cliente.getNombres());
            boolean apellidoDesactivado = esTodoMayusculas(cliente.getApellidos());

            if(nombreDesactivado && apellidoDesactivado){
                mostrarAlerta(AlertType.ERROR , "Usuario Desactivado", "Este Cliente no es valido.");
                return;
            }
            
            // Mostrar los valores tal cual vienen del objeto Cliente
            nomCliente.setText(cliente.getNombres());
            apeCliente.setText(cliente.getApellidos());
            
            // Configurar tipos de comprobante según el tipo de documento
            if (cliente.getIdDocumento() == 1) { // DNI
                // Filtrar solo Boletas
                tipocomprobanteActual = listTipoComprobantes.get(1);
                txtComprobante.setText(tipocomprobanteActual.getNombreTipo());

            } else if (cliente.getIdDocumento() == 2) { // RUC
                // Filtrar solo Facturas
                tipocomprobanteActual = listTipoComprobantes.get(0); 
                txtComprobante.setText(tipocomprobanteActual.getNombreTipo());
            }

            // Deshabilitar los campos de búsqueda una vez encontrado el cliente
            numDocumento.setDisable(true);
            
            mostrarAlerta(AlertType.INFORMATION, "Cliente encontrado",
                    "Cliente: " + nomCliente.getText() + " " + apeCliente.getText());
        } else {
            nomCliente.setText("");
            apeCliente.setText("");
            mostrarAlerta(AlertType.WARNING, "No encontrado", "No se encontró un cliente con ese documento.");
        }
    }

    /** Verifica si un texto está completamente en mayúsculas */
    private boolean esTodoMayusculas(String texto) {
        return texto.equals(texto.toUpperCase());
    }

    // =============================================================
    // MÉTODOS AUXILIARES
    // =============================================================

    /** Devuelve el ID del producto seleccionado */
    public long obtenerIdProductoSeleccionado() {
        Producto selected = listProductos.getSelectionModel().getSelectedItem();
        return (selected != null) ? selected.getIdProducto() : -1;
    }

    /** Devuelve el producto seleccionado */
    public Producto obtenerProductoSeleccionado() {
        return listProductos.getSelectionModel().getSelectedItem();
    }

    public MedioPago obtenerMedioPagoSeleccionado() {
        return listMediosPago.getSelectionModel().getSelectedItem();
    }

    /** Muestra una alerta de tipo JavaFX */
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "0.00";
        }
        return value.setScale(2, RoundingMode.HALF_UP).toString();
    }

    // Metodos Generación de Comprobante

    /**
     * Maneja el evento del botón Agregar.
     * Valida los campos necesarios antes de proceder con la generación del
     * comprobante.
     */
    @FXML
    private void btnAgregar() {
        // Validar que haya un producto seleccionado
        if (obtenerProductoSeleccionado() == null) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Por favor seleccione un producto");
            return;
        }

        // Validar que se haya ingresado una cantidad
        if (txtCantidad.getText() == null || txtCantidad.getText().trim().isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Por favor ingrese una cantidad");
            return;
        }

        // Validar que la cantidad sea un número válido
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                mostrarAlerta(AlertType.WARNING, "Validación", "La cantidad debe ser mayor a 0");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Validación", "La cantidad debe ser un número válido");
            return;
        }

        // Validar que se haya seleccionado un cliente
        if (nomCliente.getText() == null || nomCliente.getText().trim().isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Validación", "Por favor seleccione un cliente");
            return;
        }

        // Proceder con la generación del comprobante
        generarSerie();
        agregarDetalle();
        // Actualizar tabla correctamente (sin perder el vínculo)
        listDetalles.refresh();
    }

    private void generarSerie() {
        String serie;

        // Validar si hay comprobantes existentes
        if (listComprobantes == null || listComprobantes.isEmpty()) {
            serie = "FG-1"; // Serie inicial
        } else {
            // Obtener el último comprobante
            Comprobante ultimoComprobante = listComprobantes.get(listComprobantes.size() - 1);
            String seriePrev = ultimoComprobante.getSerie();

            try {
                String[] parts = seriePrev.split("-");
                if (parts.length == 2) {
                    int numero = Integer.parseInt(parts[1]) + 1;
                    serie = parts[0] + "-" + numero;
                } else {
                    serie = "FG-1"; // Serie por defecto si el formato no es válido
                }
            } catch (NumberFormatException e) {
                serie = "FG-1"; // En caso de error al parsear el número
                e.printStackTrace();
            }
        }

        txtFechaEmision.setText(java.time.LocalDate.now().toString());
        txtNumSerie.setText(serie);
    }

    /**
     * Agrega un nuevo detalle al comprobante actual.
     * Realiza cálculos de subtotales y validaciones de stock.
     */
    private void agregarDetalle() {
        try {
            // Obtener y validar producto
            Producto producto = obtenerProductoSeleccionado();
            if (producto == null) {
                mostrarAlerta(AlertType.WARNING, "Error", "No se ha seleccionado ningún producto");
                return;
            }

            // Validar cantidad
            if (txtCantidad.getText() == null || txtCantidad.getText().trim().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Error", "Debe ingresar una cantidad");
                return;
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(txtCantidad.getText().trim());
                if (cantidad <= 0) {
                    mostrarAlerta(AlertType.WARNING, "Error", "La cantidad debe ser mayor a 0");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta(AlertType.WARNING, "Error", "La cantidad ingresada no es válida");
                return;
            }

            // Validar stock
            if (producto.getStock() < cantidad) {
                mostrarAlerta(AlertType.WARNING, "Stock insuficiente",
                        "Stock disponible: " + producto.getStock() + "\n" +
                                "Cantidad solicitada: " + cantidad);
                return;
            }

            // Crear y configurar el detalle
            DetalleComprobante detalle = new DetalleComprobante();
            detalle.setCantidadProductos(cantidad);
            detalle.setIdProducto(producto.getIdProducto());
            detalle.setPrecioUnitario(producto.getPrecio());

            // Calcular subtotal
            BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(cantidad));
            detalle.setSubtotal(subtotal);

            // Calcular total con impuestos
            double tasaImpuesto = (producto.getIdTipoAfectacion() == 2 ? 0.0 : 0.18);
            BigDecimal total = subtotal.multiply(BigDecimal.ONE.add(new BigDecimal(tasaImpuesto)));
            detalle.setTotal(total.setScale(2, RoundingMode.HALF_UP));

            // Inicializar lista si es necesario
            if (listaDetalle == null) {
                listaDetalle = new ArrayList<>();
            }


            // Agregar el producto a la lista temporal si no existe
            if (!productosEnDetalle.contains(producto)) {
                productosEnDetalle.add(producto);
            }

            listaDetalle.add(detalle);
            listDetalles.getItems().add(detalle);
            listDetalles.refresh();
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "Producto agregado al comprobante");

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error", "Error al agregar detalle: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGenerarComprobante() {

        if (!btnGuardarComprobante()){
            return;
        }

        mostrarComprobanteEnAlerta();

        configurarComboProd(listProductos);
        limpiarCampos();
    }

    /**
     * Limpia todos los campos del formulario y reinicia las listas
     */
    private void limpiarCampos() {
        // Limpiar campos de producto
        limpiarCombo(listProductos);
        prodPrecio.clear();
        prodStock.clear();
        prodCat.clear();
        prodAfec.clear();
        prodUnidad.clear();
        prodImp.clear();
        txtCantidad.clear();
        txtComprobante.clear();

        // Habilitar y limpiar campos de cliente
        numDocumento.setDisable(false);  // Habilitar nuevamente la búsqueda
        // Limpiar campos de cliente
        numDocumento.clear();
        nomCliente.clear();
        apeCliente.clear();

        // Limpiar campos de comprobante
        txtNumSerie.clear();
        txtFechaEmision.clear();
        limpiarCombo(listMediosPago);

        // Reiniciar listas
        listaDetalle = new ArrayList<>();
        productosEnDetalle.clear();

        // Reiniciar Tabla de detalles
        listDetalles.getItems().clear();
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
     * Guarda el comprobante actual con todos sus detalles en la base de datos.
     * Realiza validaciones necesarias antes de guardar.
     */
    
    private boolean btnGuardarComprobante() {
        try {
            // 1. Validar que haya detalles para guardar (primera validación para evitar trabajo innecesario)
            if (listaDetalle == null || listaDetalle.isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Validación", 
                    "No hay productos agregados al comprobante.\nDebe agregar al menos un producto.");
                return false;
            }

            // 2. Validaciones de campos obligatorios
            if (!validarCamposObligatorios()) {
                return false;
            }

            // 3. Validar selección de tipo de comprobante
            if (txtComprobante.getText() == null || txtComprobante.getText().trim().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Validación", 
                    "Debe seleccionar un tipo de comprobante.");
                return false;
            }

            // 4. Validar selección de medio de pago
            MedioPago medioPago = listMediosPago.getSelectionModel().getSelectedItem();
            if (medioPago == null) {
                mostrarAlerta(AlertType.WARNING, "Validación", 
                    "Debe seleccionar un medio de pago.\nSeleccione una opción de la lista.");
                listMediosPago.requestFocus();
                return false;
            }

            // 5. Validar datos del cliente
            Cliente cliente = validarCliente();
            if (cliente == null) {
                return false;
            }

            Comprobante comprobante = new Comprobante();

            // Generar nuevo ID de comprobante
            comprobante.setIdComprobante(listComprobantes.stream()
                    .mapToLong(Comprobante::getIdComprobante)
                    .max()
                    .orElse(0L) + 1);
            comprobante.setFechaEmision(java.time.LocalDate.now());
            comprobante.setSerie(txtNumSerie.getText());

            //Calcular la suma de los totales de los detalles
            BigDecimal sumaTotal = listaDetalle.stream()
                    .map(DetalleComprobante::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            comprobante.setTotalFinal(sumaTotal);

            //Calcular la suma de los subtotales de los detalles
            BigDecimal sumaDevengado = listaDetalle.stream()
                    .map(DetalleComprobante::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            comprobante.setDevengado(sumaDevengado);

            // Usamos el cliente ya validado
            comprobante.setDireccionEnvio(cliente.getDireccion());
            comprobante.setIdTipoComprobante(tipocomprobanteActual.getIdTipoComprobante());
            comprobante.setIdMedioPago(medioPago.getIdMedioPago());
            comprobante.setIdCliente(cliente.getIdCliente());


            
            comprobante.setIdUsuario(1L);

            try {
                // Primero guardamos el comprobante
                comprobanteService.subirComprobante(comprobante);
                
                // Luego guardamos cada detalle
                for (DetalleComprobante detalle : listaDetalle) {
                    detalle.setIdComprobante(comprobante.getIdComprobante());
                    detalleComprobanteService.registrarDetalleComprobante(detalle);
                }
                
                // Actualizamos la lista de comprobantes solo después de un guardado exitoso
                this.listComprobantes = comprobanteService.listarComprobante();
                return true;
            } catch (Exception e) {
                mostrarAlerta(AlertType.ERROR, "Error al guardar", 
                    "No se pudo guardar el comprobante: " + e.getMessage());
                throw e; // Re-lanzamos la excepción para que se maneje en el catch exterior
            }
        } catch (Exception e) {
            System.err.println("Error en btnGuardarComprobante: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    

    // Mostrar Comprobantes en una alerta
    /**
     * Valida que todos los campos obligatorios estén completos
     */
    private boolean validarCamposObligatorios() {
        StringBuilder errores = new StringBuilder();

        if (txtNumSerie.getText().trim().isEmpty()) {
            errores.append("- El número de serie es obligatorio\n");
            txtNumSerie.requestFocus();
        }

        if (numDocumento.getText().trim().isEmpty()) {
            errores.append("- El número de documento del cliente es obligatorio\n");
            numDocumento.requestFocus();
        }

        if (errores.length() > 0) {
            mostrarAlerta(AlertType.WARNING, "Campos obligatorios",
                    "Por favor complete los siguientes campos:\n" + errores.toString());
            return false;
        }

        return true;
    }

    /**
     * Valida que el cliente exista y tenga los datos necesarios
     */
    private Cliente validarCliente() {
        String numeroDocumento = numDocumento.getText().trim();
        Cliente cliente = listClientes.stream()
                .filter(c -> c.getNumDocumento() != null &&
                        c.getNumDocumento().trim().equals(numeroDocumento))
                .findFirst()
                .orElse(null);

        if (cliente == null) {
            mostrarAlerta(AlertType.WARNING, "Cliente no encontrado",
                    "No se encontró un cliente con el número de documento: " + numeroDocumento);
            numDocumento.requestFocus();
            return null;
        }

        if (cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Datos incompletos",
                    "El cliente no tiene una dirección registrada.\n" +
                            "Por favor, actualice los datos del cliente antes de continuar.");
            numDocumento.requestFocus();
            return null;
        }

        return cliente;
    }

    private void mostrarComprobanteEnAlerta() {
        if (listComprobantes != null && !listComprobantes.isEmpty()) {
            Comprobante c = listComprobantes.get(listComprobantes.size() - 1);
            StringBuilder comprobanteStr = new StringBuilder("Comprobante Generado:\n");
            comprobanteStr.append("ID Comprobante: ").append(c.getIdComprobante())
                    .append("\n")
                    .append("Fecha Emisión: ").append(c.getFechaEmision())
                    .append("\n")
                    .append("Serie: ").append(c.getSerie())
                    .append("\n")
                    .append("Total Final: ").append(formatBigDecimal(c.getTotalFinal()))
                    .append("\n")
                    .append("Devengado: ").append(formatBigDecimal(c.getDevengado()))
                    .append("\n")
                    .append("Dirección Envío: ").append(c.getDireccionEnvio())
                    .append("\n");
            mostrarAlerta(AlertType.INFORMATION, "Comprobante " + c.getIdComprobante(), comprobanteStr.toString());
        }
    }

}

