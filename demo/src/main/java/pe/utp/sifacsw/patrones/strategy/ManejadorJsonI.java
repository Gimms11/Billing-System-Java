package pe.utp.sifacsw.patrones.strategy;

public interface ManejadorJsonI<T> {
    T leerArchivoJson(String rutaArchivo);
    void escribirArchivoJson(T objeto, String rutaArchivo);
}
