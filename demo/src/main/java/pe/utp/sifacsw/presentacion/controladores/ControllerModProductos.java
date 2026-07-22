package pe.utp.sifacsw.presentacion.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;

import pe.utp.sifacsw.dominio.modelos.AfectacionProductos;
import pe.utp.sifacsw.dominio.modelos.CategoriaProductos;
import pe.utp.sifacsw.dominio.modelos.Producto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import pe.utp.sifacsw.aplicacion.servicios.AfectacionService;
import pe.utp.sifacsw.aplicacion.servicios.CategoriaService;
import pe.utp.sifacsw.aplicacion.servicios.ProductoService;

public class ControllerModProductos {

    // --- Inputs ---
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtUnidad;
    @FXML
    private TextArea txtDesc;

    // --- ComboBox ---
    @FXML
    private ComboBox<CategoriaProductos> listCategoria;
    @FXML
    private ComboBox<AfectacionProductos> listAfectacion;

    @FXML
    private Button btnEliminActivar;

    // --- Servicios ---
    private AfectacionService afectacionService;
    private CategoriaService categoriaService;
    private ProductoService productoService;

    // --- Producto actual ---
    private Producto productoActual;

    /*
     * ============================================================
     * Inicialización
     * ============================================================
     */
    @FXML
    public void initialize() {
        try {
            afectacionService = new AfectacionService();
            categoriaService = new CategoriaService();
            productoService = new ProductoService();

            productoActual = productoService.obteneProducto(1L);

            cargarDatosProducto();
            configurarComboAfectacion();
            configurarComboCategoria();
            configurarEstadoBoton();
            bloquearCampos(!productoEstaActivo());

        } catch (Exception e) {
            mostrarError("Error al iniciar", "No se pudo cargar el producto.", e.getMessage());
        }
    }

    /*
     * ============================================================
     * SETTER dinámico
     * ============================================================
     */
    public void setProducto(Producto producto) {
        this.productoActual = producto;

        cargarDatosProducto();
        configurarComboAfectacion();
        configurarComboCategoria();
        configurarEstadoBoton();
        bloquearCampos(!productoEstaActivo());
    }

    /*
     * ============================================================
     * Cargar datos del producto
     * ============================================================
     */
    private void cargarDatosProducto() {
        txtNombre.setText(productoActual.getNombre());
        txtDesc.setText(productoActual.getDescripcion());
        txtPrecio.setText(String.valueOf(productoActual.getPrecio()));
        txtStock.setText(String.valueOf(productoActual.getStock()));
        txtUnidad.setText(productoActual.getUnidadMedida());
    }

    /*
     * ============================================================
     * BLOQUEAR / DESBLOQUEAR CAMPOS
     * ============================================================
     */
    private void bloquearCampos(boolean bloquear) {
        txtNombre.setDisable(bloquear);
        txtPrecio.setDisable(bloquear);
        txtStock.setDisable(bloquear);
        txtUnidad.setDisable(bloquear);
        txtDesc.setDisable(bloquear);
        listCategoria.setDisable(bloquear);
        listAfectacion.setDisable(bloquear);
    }

    /*
     * ============================================================
     * Estado ACTIVO / DESACTIVADO
     * ============================================================
     */
    private boolean estaDesactivado(String texto) {
        return texto.equals(texto.toUpperCase());
    }

    private boolean productoEstaActivo() {
        return !(estaDesactivado(txtNombre.getText()) &&
                estaDesactivado(txtDesc.getText()));
    }

    private void configurarEstadoBoton() {
        if (productoEstaActivo())
            btnEliminActivar.setText("Desactivar");
        else
            btnEliminActivar.setText("Activar");
    }

    /*
     * ============================================================
     * Activar / Desactivar producto (LÓGICA MAYÚSCULAS)
     * ============================================================
     */
    @FXML
    private void onClickEliminarActivar() {

        boolean activo = productoEstaActivo();

        try {
            if (activo) {
                // DESACTIVAR — convertir a mayúsculas
                productoActual.setNombre(txtNombre.getText().toUpperCase());
                productoActual.setDescripcion(txtDesc.getText().toUpperCase());
                bloquearCampos(true);
                mostrarInfo("Producto desactivado",
                        "Ahora el producto se encuentra DESACTIVADO.");

            } else {
                // ACTIVAR — capitalizar
                productoActual.setNombre(capitalizar(txtNombre.getText()));
                productoActual.setDescripcion(capitalizar(txtDesc.getText()));
                bloquearCampos(false);
                mostrarInfo("Producto activado",
                        "El producto ahora está ACTIVO.");
            }

            productoService.actualizarProducto(productoActual);

        } catch (Exception e) {
            mostrarError("Error al cambiar estado",
                    "No se pudo actualizar el estado del producto.",
                    e.getMessage());
        }

        cerrarVentana();
    }

    /*
     * ============================================================
     * Combos
     * ============================================================
     */
    private void configurarComboAfectacion() {
        try {
            var afectaciones = afectacionService.listarAfectaciones();
            if (afectaciones == null)
                afectaciones = new ArrayList<>();

            listAfectacion.setItems(FXCollections.observableArrayList(afectaciones));
            listAfectacion.setPromptText("Seleccione tipo de afectación");

            afectaciones.forEach(a -> {
                if (a.getIdAfectacion() == productoActual.getIdTipoAfectacion())
                    listAfectacion.getSelectionModel().select(a);
            });

            listAfectacion.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(AfectacionProductos item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombreAfectacion());
                }
            });

            listAfectacion.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(AfectacionProductos item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "Seleccione tipo de afectación"
                            : item.getNombreAfectacion());
                }
            });

        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar las afectaciones.", e.getMessage());
        }
    }

    private void configurarComboCategoria() {
        try {
            var categorias = categoriaService.listarCategorias();
            if (categorias == null)
                categorias = new ArrayList<>();

            listCategoria.setItems(FXCollections.observableArrayList(categorias));
            listCategoria.setPromptText("Seleccione categoría");

            categorias.forEach(c -> {
                if (c.getIdCategoria() == productoActual.getIdCategoria())
                    listCategoria.getSelectionModel().select(c);
            });

            listCategoria.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(CategoriaProductos item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombreCategoria());
                }
            });

            listCategoria.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(CategoriaProductos item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "Seleccione categoría"
                            : item.getNombreCategoria());
                }
            });

        } catch (Exception e) {
            mostrarError("Error", "No se pudo cargar las categorías.", e.getMessage());
        }
    }

    /*
     * ============================================================
     * Validaciones
     * ============================================================
     */
    private boolean validar() {

        if (txtNombre.getText().isBlank() ||
                txtPrecio.getText().isBlank() ||
                txtStock.getText().isBlank() ||
                txtUnidad.getText().isBlank() ||
                txtDesc.getText().isBlank()) {

            mostrarAdvertencia("Campos vacíos", "Todos los campos son obligatorios.");
            return false;
        }

        if (!txtPrecio.getText().matches("\\d+(\\.\\d+)?")) {
            mostrarAdvertencia("Precio inválido", "Ingrese un valor numérico.");
            return false;
        }

        if (!txtStock.getText().matches("\\d+")) {
            mostrarAdvertencia("Stock inválido", "Debe ser un número entero.");
            return false;
        }

        if (listCategoria.getValue() == null) {
            mostrarAdvertencia("Categoría", "Seleccione una categoría.");
            return false;
        }

        if (listAfectacion.getValue() == null) {
            mostrarAdvertencia("Afectación", "Seleccione un tipo de afectación.");
            return false;
        }

        return true;
    }

    /*
     * ============================================================
     * Actualizar Producto
     * ============================================================
     */
    @FXML
    private void actualizarProducto() {

        if (!validar())
            return;

        try {
            productoActual.setNombre(capitalizar(txtNombre.getText()));
            productoActual.setDescripcion(txtDesc.getText());
            productoActual.setPrecio(BigDecimal.valueOf(Double.parseDouble(txtPrecio.getText())));
            productoActual.setStock(Integer.valueOf(txtStock.getText()));
            productoActual.setUnidadMedida(txtUnidad.getText());

            productoActual.setIdCategoria(listCategoria.getValue().getIdCategoria());
            productoActual.setIdTipoAfectacion(listAfectacion.getValue().getIdAfectacion());

            productoService.actualizarProducto(productoActual);

            mostrarInfo("Actualización exitosa", "El producto fue actualizado correctamente.");

        } catch (Exception e) {
            mostrarError("Error al actualizar",
                    "Ocurrió un problema al guardar los cambios.",
                    e.getMessage());
        }

        cerrarVentana();
    }

    /*
     * ============================================================
     * CANCELAR (restaurar datos sin guardar)
     * ============================================================
     */
    @FXML
    private void cancelar() {
        cargarDatosProducto();
        configurarComboAfectacion();
        configurarComboCategoria();
        configurarEstadoBoton();
        bloquearCampos(!productoEstaActivo());
        mostrarInfo("Cancelado", "Se descartaron los cambios.");
        cerrarVentana();
    }

    /*
     * ============================================================
     * CERRAR VENTANA SIN HACER NADA
     * ============================================================
     */
    @FXML
    private void cerrarSinCambios() {
        cerrarVentana();
    }

    /*
     * ============================================================
     * Utilidades
     * ============================================================
     */
    private String capitalizar(String texto) {
        if (texto == null || texto.isBlank())
            return texto;
        texto = texto.toLowerCase();
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private void mostrarInfo(String t, String m) {
        new Alert(Alert.AlertType.INFORMATION, m) {
            {
                setHeaderText(t);
            }
        }.showAndWait();
    }

    private void mostrarAdvertencia(String t, String m) {
        new Alert(Alert.AlertType.WARNING, m) {
            {
                setHeaderText(t);
            }
        }.showAndWait();
    }

    private void mostrarError(String t, String m, String d) {
        new Alert(Alert.AlertType.ERROR, m + "\n\nDETALLES:\n" + d) {
            {
                setHeaderText(t);
            }
        }.showAndWait();
    }

    @FXML
    private void cerrarVentana() {
        btnEliminActivar.getScene().getWindow().hide();
    }
}

