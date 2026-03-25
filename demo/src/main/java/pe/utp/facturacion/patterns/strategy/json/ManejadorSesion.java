package pe.utp.facturacion.patterns.strategy.json;

import pe.utp.facturacion.model.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManejadorSesion implements ManejadorJsonI<Usuario> {

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
        return new File(directorioApp, "sesionActual.json");
    }

    @Override
    public Usuario leerArchivoJson(String rutaArchivo) {
        System.out.println("[PATRÓN STRATEGY] Ejecutando estrategia de lectura JSON: ManejadorSesion");
        System.out.println("[GRASP: Polymorphism] Implementación polimórfica de ManejadorJsonI para Usuario");
        System.out.println("[GRASP: Information Expert] ManejadorSesion conoce cómo leer datos de sesión desde JSON");

        File archivo = obtenerArchivoSeguro(rutaArchivo);

        // Validación CRUCIAL: Si el archivo no existe (primera vez que abren el .exe o
        // cerraron sesión), retorna null
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

            Usuario usuario = new Usuario();

            // Separar los campos por comas y procesar cada uno
            for (String campo : json.split(",")) {
                String[] partes = campo.split(":");
                if (partes.length >= 2) {
                    String clave = partes[0].trim().replace("\"", "");
                    // Unimos el valor por si la contraseña u otro campo tenía ":" adentro
                    String valor = partes[1].trim().replace("\"", "");

                    switch (clave) {
                        case "idUsuario":
                            usuario.setIdUsuario(Long.parseLong(valor));
                            break;
                        case "username":
                            usuario.setUsername(valor);
                            break;
                        case "password":
                            usuario.setPassword(valor);
                            break;
                        case "rol":
                            usuario.setRol(valor);
                            break;
                    }
                }
            }

            // Validar campos obligatorios adaptado al objeto Usuario
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de usuario es obligatorio");
            }

            return usuario;

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
    public void escribirArchivoJson(Usuario usuario, String rutaArchivo) {
        File archivo = obtenerArchivoSeguro(rutaArchivo);

        // Se usa tu formato exacto de JSON
        StringBuilder json = new StringBuilder();
        json.append("{\n")
                .append("  \"idUsuario\": ").append(usuario.getIdUsuario()).append(",\n")
                .append("  \"username\": \"").append(escapeJson(usuario.getUsername())).append("\",\n")
                .append("  \"password\": \"").append(escapeJson(usuario.getPassword())).append("\",\n")
                .append("  \"rol\": \"").append(escapeJson(usuario.getRol())).append("\"\n")
                .append("}");

        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(json.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir el archivo JSON de usuario en: " + archivo.getAbsolutePath(),
                    e);
        }
    }

    private String escapeJson(String value) {
        if (value == null)
            return "";
        return value.replace("\"", "\\\"");
    }
}