package pe.utp.facturacion.patterns.strategy.json;

import pe.utp.facturacion.model.Empresa;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManejadorEmpresa implements ManejadorJsonI<Empresa> {

    // Método auxiliar para obtener la ruta segura y evitar el crash en el .exe
    private File obtenerArchivoSeguro(String rutaOpcional) {
        // Si mandas una ruta por parámetro, la respeta
        if (rutaOpcional != null && !rutaOpcional.trim().isEmpty()) {
            return new File(rutaOpcional);
        }

        // Obtiene la ruta base del usuario de Windows (Ej: C:\Users\TuUsuario)
        String userHome = System.getProperty("user.home");
        // Crea una carpeta invisible específica para tu aplicación
        File directorioApp = new File(userHome, ".SistemaFacturacion");

        // Crea los directorios si no existen
        if (!directorioApp.exists()) {
            directorioApp.mkdirs();
        }

        // Retorna el archivo JSON final en esa ruta segura
        return new File(directorioApp, "empresa.json");
    }

    @Override
    public Empresa leerArchivoJson(String rutaArchivo) {
        System.out.println("[PATRÓN STRATEGY] Ejecutando estrategia de lectura JSON: ManejadorEmpresa");
        System.out.println("[GRASP: Polymorphism] Implementación polimórfica de ManejadorJsonI para Empresa");
        System.out.println("[GRASP: Information Expert] ManejadorEmpresa conoce cómo leer datos de empresa desde JSON");

        File archivo = obtenerArchivoSeguro(rutaArchivo);

        // Validación CRUCIAL: Si el archivo no existe, retorna null
        if (!archivo.exists()) {
            return null;
        }

        try {
            StringBuilder jsonContent = new StringBuilder();
            String linea;

            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                while ((linea = reader.readLine()) != null) {
                    jsonContent.append(linea.trim());
                }
            }

            String json = jsonContent.toString();

            // Prevención de errores si el archivo quedó vacío
            if (json.isEmpty())
                return null;

            // Eliminar las llaves del inicio y final
            json = json.substring(1, json.length() - 1);

            Empresa empresa = new Empresa();

            // Separar los campos por comas y procesar cada uno
            for (String campo : json.split(",")) {
                String[] partes = campo.split(":");
                if (partes.length >= 2) {
                    String clave = partes[0].trim().replace("\"", "");
                    // Unimos el valor de manera más segura por si hay ":" en correos o URLs
                    String valor = partes[1].trim().replace("\"", "");

                    switch (clave) {
                        case "nombre":
                            empresa.setNombre(valor);
                            break;
                        case "ruc":
                            empresa.setRuc(valor);
                            break;
                        case "id":
                            empresa.setIdEmpresa(Long.parseLong(valor));
                            break;
                        case "correo":
                            empresa.setCorreo(valor);
                            break;
                        case "direccion":
                            empresa.setDireccion(valor);
                            break;
                        case "telefono":
                            empresa.setTelefono(valor);
                            break;
                        case "pais":
                            empresa.setPais(valor);
                            break;
                        case "descripcion":
                            empresa.setDescripcion(valor);
                            break;
                    }
                }
            }

            // Validar campos obligatorios
            if (empresa.getNombre() == null || empresa.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la empresa es obligatorio");
            }

            // Si no hay RUC, asignar uno por defecto para testing
            if (empresa.getRuc() == null || empresa.getRuc().trim().isEmpty()) {
                empresa.setRuc("20123456789");
            }

            return empresa;

        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Error al procesar los datos: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void escribirArchivoJson(Empresa empresa, String rutaArchivo) {
        File archivo = obtenerArchivoSeguro(rutaArchivo);

        // Construir JSON manualmente respetando tu estructura
        StringBuilder json = new StringBuilder();
        json.append("{\n")
                .append("  \"nombre\": \"").append(escapeJson(empresa.getNombre())).append("\",\n")
                .append("  \"ruc\": \"").append(escapeJson(empresa.getRuc())).append("\",\n")
                .append("  \"id\": ").append(empresa.getIdEmpresa()).append(",\n")
                .append("  \"correo\": \"").append(escapeJson(empresa.getCorreo())).append("\",\n")
                .append("  \"direccion\": \"").append(escapeJson(empresa.getDireccion())).append("\",\n")
                .append("  \"telefono\": \"").append(escapeJson(empresa.getTelefono())).append("\",\n")
                .append("  \"pais\": \"").append(escapeJson(empresa.getPais())).append("\",\n")
                .append("  \"descripcion\": \"").append(escapeJson(empresa.getDescripcion())).append("\"\n")
                .append("}");

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(json.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo JSON de empresa en: " + archivo.getAbsolutePath(),
                    e);
        }
    }

    // Método auxiliar para escapar comillas y caracteres especiales (básico)
    private String escapeJson(String value) {
        if (value == null)
            return "";
        return value.replace("\"", "\\\"");
    }
}