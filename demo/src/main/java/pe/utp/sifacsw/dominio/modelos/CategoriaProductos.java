package pe.utp.sifacsw.dominio.modelos;

public class CategoriaProductos {
    private Long idCategoria;
    private String nombreCategoria;
    private String descripcion;

    public CategoriaProductos() {}

    public CategoriaProductos(Long idAfectacion, String nombreAfectacion, String descripcion) {
        this.idCategoria = idAfectacion;
        this.nombreCategoria = nombreAfectacion;
        this.descripcion = descripcion;
    }

    // Getters
    public Long getIdCategoria() { return idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public String getDescripcion() { return descripcion; }

    // Setters
    public void setIdCategoria(Long idAfectacion) { this.idCategoria = idAfectacion; }
    public void setNombreCategoria(String nombreAfectacion) { this.nombreCategoria = nombreAfectacion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreCategoria;
    }
}

