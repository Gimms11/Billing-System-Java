package pe.utp.sifacsw.presentacion.controladores;

import java.util.ArrayList;
import java.math.BigDecimal;

import pe.utp.sifacsw.dominio.modelos.AfectacionProductos;
import pe.utp.sifacsw.dominio.modelos.CategoriaProductos;
import pe.utp.sifacsw.dominio.modelos.Cliente;
import pe.utp.sifacsw.dominio.modelos.Producto;
import javafx.collections.FXCollections;
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
import pe.utp.sifacsw.aplicacion.servicios.AfectacionService;
import pe.utp.sifacsw.aplicacion.servicios.CategoriaService;
import pe.utp.sifacsw.aplicacion.servicios.ProductoService;

/**
 * Controlador para la gestión de productos.
 * Administra los ComboBox de tipo de afectación y categoría de productos
 * para dos formularios distintos dentro de la aplicación.
 */
public class ControllerProducts {

    // === COMBOBOX DE AFECTACIÓN ===
    @FXML private ComboBox<AfectacionProductos> listAfectacion;
    @FXML private ComboBox<AfectacionProductos> listAfectacion1;

    // === COMBOBOX DE CATEGORÍA ===
    @FXML private ComboBox<CategoriaProductos> listCategoria;
    @FXML private ComboBox<CategoriaProductos> listCategoria1;

    // === TABLA DE PRODUCTOS ===
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colStock;
    @FXML private TableColumn<Producto, String> colCat;
    @FXML private TableColumn<Producto, String> colAfec;
    @FXML private TableColumn<Producto, String> colMedida;
    @FXML private TableColumn<Producto, Hyperlink> colAct;

    // --- Datos de insercción del producto ---

    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private TextField txtUnidad;
    @FXML private TextArea txtDesc;

    // --- Datos de Filtro de productos ---

    @FXML private TextField txtFnombre;
    @FXML private TextField txtFprecio;
    @FXML private TextField txtFstock;
    @FXML private TextField txtFunidad;

    // === SERVICIOS ===
    private AfectacionService afectacionService;
    private CategoriaService categoriaService;
    private ProductoService productoService;

    // =============================================================
    // MÉTODO PRINCIPAL DE INICIALIZACIÓN
    // =============================================================
    @FXML
    public void initialize() {
        try {
            // Inicialización de servicios
            this.afectacionService = new AfectacionService();
            this.categoriaService = new CategoriaService();
            this.productoService = new ProductoService();

            // Configuración de la tabla
            configurarTabla();

            // Configuración de ambos grupos de ComboBox
            configurarComboAfectacion(listAfectacion);
            configurarComboAfectacion(listAfectacion1);

            configurarComboCategoria(listCategoria);
            configurarComboCategoria(listCategoria1);

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error de inicialización", 
                    "Ocurrió un error al cargar los datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =============================================================
    // CONFIGURACIÓN DE COMBOBOX
    // =============================================================

    /**
     * Carga las afectaciones de productos y las asigna al ComboBox.
     * @param combo ComboBox de afectaciones.
     */
    private void configurarComboAfectacion(ComboBox<AfectacionProductos> combo) {
        var afectaciones = afectacionService.listarAfectaciones();

        if (afectaciones == null) {
            afectaciones = new ArrayList<>();
        }

        // Si es el ComboBox de filtrado (el que termina en 1), agregar opción "Ninguno"
        if (combo == listAfectacion1) {
            AfectacionProductos ninguno = new AfectacionProductos();
            ninguno.setIdAfectacion(1L);
            ninguno.setNombreAfectacion("Ninguno");
            afectaciones.add(0, ninguno);
        }

        combo.setItems(FXCollections.observableArrayList(afectaciones));
        combo.setPromptText("Seleccione tipo de afectación");

        // Configurar cómo se muestran los items en el ComboBox
        combo.setCellFactory(param -> new ListCell<AfectacionProductos>() {
            @Override
            protected void updateItem(AfectacionProductos item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(combo.getPromptText());
                } else {
                    setText(item.getNombreAfectacion());
                }
            }
        });

        // Configurar cómo se muestra el item seleccionado
        combo.setButtonCell(new ListCell<AfectacionProductos>() {
            @Override
            protected void updateItem(AfectacionProductos item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(combo.getPromptText());
                } else {
                    setText(item.getNombreAfectacion());
                }
            }
        });
    }

    /**
     * Carga las categorías de productos y las asigna al ComboBox.
     * @param combo ComboBox de categorías.
     */
    private void configurarComboCategoria(ComboBox<CategoriaProductos> combo) {
        var categorias = categoriaService.listarCategorias();

        if (categorias == null) {
            categorias = new ArrayList<>();
        }

        // Si es el ComboBox de filtrado (el que termina en 1), agregar opción "Ninguno"
        if (combo == listCategoria1) {
            CategoriaProductos ninguno = new CategoriaProductos();
            ninguno.setIdCategoria(1L);
            ninguno.setNombreCategoria("Ninguno");
            categorias.add(0, ninguno);
        }

        combo.setItems(FXCollections.observableArrayList(categorias));
        combo.setPromptText("Seleccione categoría");

        // Configurar cómo se muestran los items en el ComboBox
        combo.setCellFactory(param -> new ListCell<CategoriaProductos>() {
            @Override
            protected void updateItem(CategoriaProductos item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(combo.getPromptText());
                } else {
                    setText(item.getNombreCategoria());
                }
            }
        });

        // Configurar cómo se muestra el item seleccionado
        combo.setButtonCell(new ListCell<CategoriaProductos>() {
            @Override
            protected void updateItem(CategoriaProductos item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(combo.getPromptText());
                } else {
                    setText(item.getNombreCategoria());
                }
            }
        });
    }

    // =============================================================
    // MÉTODOS PARA OBTENER VALORES SELECCIONADOS
    // =============================================================

    /** Devuelve el ID de la afectación seleccionada del primer ComboBox. */
    public Long obtenerIdAfectacionSeleccionada() {
        AfectacionProductos af = listAfectacion.getValue();
        return (af != null) ? af.getIdAfectacion() : null;
    }

    /** Devuelve el ID de la afectación seleccionada del segundo ComboBox. */
    public Long obtenerIdAfectacionSeleccionada1() {
        AfectacionProductos af = listAfectacion1.getValue();
        // Retorna null si no hay selección o si se seleccionó "Ninguno"
        if (af == null || af.getNombreAfectacion().equals("Ninguno")) {
            return null;
        }
        return af.getIdAfectacion();
    }

    /** Devuelve el ID de la categoría seleccionada del primer ComboBox. */
    public Long obtenerIdCategoriaSeleccionada() {
        CategoriaProductos cat = listCategoria.getValue();
        return (cat != null) ? cat.getIdCategoria() : null;
    }

    /** Devuelve el ID de la categoría seleccionada del segundo ComboBox. */
    public Long obtenerIdCategoriaSeleccionada1() {
        CategoriaProductos cat = listCategoria1.getValue();
        // Retorna null si no hay selección o si se seleccionó "Ninguno"
        if (cat == null || cat.getNombreCategoria().equals("Ninguno")) {
            return null;
        }
        return cat.getIdCategoria();
    }

    // =============================================================
    // MÉTODO AUXILIAR: ALERTAS
    // =============================================================

    /**
     * Muestra una alerta de tipo JavaFX.
     * @param tipo Tipo de alerta (INFORMATION, WARNING, ERROR)
     * @param titulo Título de la ventana de alerta
     * @param mensaje Contenido del mensaje
     */
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void configurarTabla() {

        // Configurar columna Nombre
        colNombre.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getNombre() != null ? 
                cellData.getValue().getNombre() : ""
            );
        });

        // Configurar columna Stock
        colStock.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            Integer stock = cellData.getValue().getStock();
            return new SimpleStringProperty(stock != null ? stock.toString() : "");
        });

        // Configurar columna Categoría
        colCat.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getNombreCategoria() != null ? 
                cellData.getValue().getNombreCategoria() : ""
            );
        });


        // Configurar columna Afectación
        colAfec.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getNombreAfectacion() != null ? 
                cellData.getValue().getNombreAfectacion() : ""
            );
        });

        // Configurar columna Unidad de Medida
        colMedida.setCellValueFactory(cellData -> {
            if (cellData.getValue() == null) return new SimpleStringProperty("");
            return new SimpleStringProperty(
                cellData.getValue().getUnidadMedida() != null ? 
                cellData.getValue().getUnidadMedida() : ""
            );
        });

        // Configurar columna de Acciones
        colAct.setCellFactory(param -> new TableCell<>() {
            private final Hyperlink link = new Hyperlink("Modificar");
            {
                link.setStyle("-fx-text-fill: #b60003;\n-fx-underline: true;");
            }

            @Override
            protected void updateItem(Hyperlink item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(link);
                    link.setOnAction(event -> {
                        Producto producto = getTableRow().getItem();
                        if (producto != null) {
                            abrirVentanaModificar(producto);
                        }
                    });
                }
            }
        });

        // Cargar datos iniciales
        cargarDatosTabla();
    }

    @FXML
    public void insertarProducto() {
        try {
            // Validaciones
            if (txtNombre.getText().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El nombre del producto es obligatorio.");
                return;
            }
            // Validaciones
            if (txtStock.getText().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El nombre del producto es obligatorio.");
                return;
            }
            // Validaciones
            if (txtUnidad.getText().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El nombre del producto es obligatorio.");
                return;
            }
            // Validaciones
            if (txtDesc.getText().isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El nombre del producto es obligatorio.");
                return;
            }
            // Validaciones
            String precioText = txtPrecio.getText();
            if (precioText.isEmpty()) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El precio es obligatorio.");
                return;
            }
            // Validaciones
            BigDecimal precio;
            try {
                precio = new BigDecimal(precioText);
                if (precio.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El precio debe ser mayor a 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El precio debe ser un número válido.");
                return;
            }

            String stockText = txtStock.getText();
            Integer stock = null;
            if (!stockText.isEmpty()) {
                try {
                    stock = Integer.parseInt(stockText);
                    if (stock < 0) {
                        mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El stock no puede ser negativo.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "El stock debe ser un número entero válido.");
                    return;
                }
            }

            Long idAfectacion = obtenerIdAfectacionSeleccionada();
            if (idAfectacion == null) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "Debe seleccionar un tipo de afectación.");
                return;
            }

            Long idCategoria = obtenerIdCategoriaSeleccionada();
            if (idCategoria == null) {
                mostrarAlerta(AlertType.WARNING, "Datos incorrectos", "Debe seleccionar una categoría.");
                return;
            }

            // Crear objeto Producto
            Producto producto = new Producto(
                null, // idProducto (será generado por la BD)
                txtNombre.getText(),
                precio,
                stock,
                txtDesc.getText(),
                txtUnidad.getText(),
                idAfectacion,
                idCategoria
            );

            // Registrar el producto
            productoService.registrarProducto(producto);
            mostrarAlerta(AlertType.INFORMATION, "Registro exitoso", "El producto se ha registrado correctamente.");
            
            // Limpiar campos
            limpiarCampos();
            
            // Recargar tabla
            cargarDatosTabla();

        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error al registrar", 
                "No se pudo registrar el producto.\nError: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        // Limpiar campos de texto
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtUnidad.clear();
        txtDesc.clear();
        
        // Limpiar selecciones de ComboBox manteniendo los promptText      
        limpiarCombo(listCategoria);
        limpiarCombo(listAfectacion);
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

    @FXML
    private void limpiarFiltros() {
        // Limpiar campos de texto de filtro
        txtFnombre.clear();
        txtFprecio.clear();
        txtFstock.clear();
        txtFunidad.clear();
        
        // Resetear ComboBox de filtrado
        listCategoria1.setValue(null);
        listAfectacion1.setValue(null);
        
        // Recargar datos originales
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        try {
            var productos = productoService.obtenerTodos();
            if (productos == null) {
                productos = new ArrayList<>();
            }
            tablaProductos.setItems(FXCollections.observableArrayList(productos));
            tablaProductos.refresh();
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error al cargar datos", 
                "No se pudieron cargar los datos de los productos.\n" + e.getMessage());
        }
    }

    /**
     * Abre la ventana de modificación de cliente
    */
     private void abrirVentanaModificar(Producto producto) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pe/utp/sifacsw/vistas/ModProducto.fxml"));
            Parent root = loader.load();
            
            // Obtener el controller de la ventana emergente
            ControllerModProductos controller = loader.getController();

            // Enviar el cliente seleccionado al controller
            controller.setProducto(producto);

            // Crear ventana modal
            Stage stage = new Stage();
            stage.setTitle("Modificar Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Modal: bloquea la ventana principal
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.showAndWait();  // Espera a que se cierre

            // Luego de cerrarse, recargar la tabla
            cargarDatosTabla();

        } catch (Exception e) {
            mostrarError("Error al abrir ventana",
                    "No se pudo abrir la ventana de modificación.",
                    e.getMessage());
        }
    }

    @FXML
    private void filtroProductos(){
        try {
            // Obtener valores de los campos de texto
            String nombre = txtFnombre.getText().isEmpty() ? null : txtFnombre.getText();
            Double precio = null;
            if (!txtFprecio.getText().isEmpty()) {
                try {
                    precio = Double.parseDouble(txtFprecio.getText());
                } catch (NumberFormatException e) {
                    mostrarAlerta(AlertType.WARNING, "Dato inválido", "El precio debe ser un número válido");
                    return;
                }
            }
            
            Integer stock = null;
            if (!txtFstock.getText().isEmpty()) {
                try {
                    stock = Integer.parseInt(txtFstock.getText());
                } catch (NumberFormatException e) {
                    mostrarAlerta(AlertType.WARNING, "Dato inválido", "El stock debe ser un número entero");
                    return;
                }
            }
            
            String unidad = txtFunidad.getText().isEmpty() ? null : txtFunidad.getText();
            
            // Obtener IDs de los ComboBox
            Long catId = obtenerIdCategoriaSeleccionada1();
            Long afecId = obtenerIdAfectacionSeleccionada1();
            
            // Convertir a Integer solo si no son null
            Integer idCategoria = catId != null ? catId.intValue() : null;
            Integer idAfectacion = afecId != null ? afecId.intValue() : null;

            var productos = productoService.filtrarProductos(
                nombre, precio, stock, unidad, idAfectacion, idCategoria
            );

            if (productos == null) {
                productos = new ArrayList<>();
            }
            tablaProductos.setItems(FXCollections.observableArrayList(productos));
            tablaProductos.refresh();
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error al cargar datos", 
                "No se pudieron cargar los datos de los productos.\n" + e.getMessage());
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
}

