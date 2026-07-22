package pe.utp.sifacsw.dominio.modelos;

import java.math.BigDecimal;

public class DetalleComprobante {
    private Long idDetalle;
    private Integer cantidadProductos;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;        // = cantidad * precioUnitario (ya calculado)
    private Long idProducto;            // FK → Producto
    private Long idImpuesto;            // FK → Impuestos
    private BigDecimal total;           // = subtotal + montoImpuesto (ya calculado)
    private Long idComprobante;         // FK → Comprobante

    private String nombreProducto;      // Transient: for display purposes

    // Constructor vacío
    public DetalleComprobante() {}

    // Constructor completo
    public DetalleComprobante(Long idDetalle, Integer cantidadProductos, BigDecimal precioUnitario,
                              BigDecimal subtotal, Long idProducto, Long idImpuesto,
                              BigDecimal total, Long idComprobante) {
        this.idDetalle = idDetalle;
        this.cantidadProductos = cantidadProductos;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.idProducto = idProducto;
        this.idImpuesto = idImpuesto;
        this.total = total;
        this.idComprobante = idComprobante;
    }

    // Getters
    public Long getIdDetalle() { return idDetalle; }
    public Integer getCantidadProductos() { return cantidadProductos; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public Long getIdProducto() { return idProducto; }
    public Long getIdImpuesto() { return idImpuesto; }
    public BigDecimal getTotal() { return total; }
    public Long getIdComprobante() { return idComprobante; }
    public String getNombreProducto() { return nombreProducto; }

    // Setters
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }
    public void setCantidadProductos(Integer cantidadProductos) { this.cantidadProductos = cantidadProductos; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
    public void setIdImpuesto(Long idImpuesto) { this.idImpuesto = idImpuesto; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setIdComprobante(Long idComprobante) { this.idComprobante = idComprobante; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}

