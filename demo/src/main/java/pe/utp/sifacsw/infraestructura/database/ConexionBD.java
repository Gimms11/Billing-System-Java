package pe.utp.sifacsw.infraestructura.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Gestión de conexiones a PostgreSQL (NeonDB) mediante HikariCP.
 * <p>
 * Implementa un pool de conexiones persistente que elimina el overhead del
 * handshake SSL/TLS en cada operación. Una sola instancia del pool se
 * comparte durante toda la sesión del usuario (Singleton sobre el DataSource).
 * </p>
 * <p>
 * Arquitectura de Información — Unidad III IS: Capa de Infraestructura.
 * </p>
 */
public class ConexionBD {

    // ─────────────────────────────────────────────────────────
    // Configuración de conexión (NeonDB / PostgreSQL cloud)
    // ─────────────────────────────────────────────────────────
    private static final String URL =
        "jdbc:postgresql://ep-lingering-tooth-ace9356b-pooler.sa-east-1.aws.neon.tech/neondb" +
        "?sslmode=require";
    private static final String USER     = "neondb_owner";
    private static final String PASSWORD = "npg_vODo1sA9HjnS";

    // ─────────────────────────────────────────────────────────
    // Singleton del DataSource (pool de conexiones)
    // ─────────────────────────────────────────────────────────
    private static HikariDataSource dataSource;

    // Constructor privado — no instanciable
    private ConexionBD() {}

    /**
     * Inicializa el pool HikariCP con la configuración óptima para NeonDB.
     * Se llama una sola vez al inicio de la aplicación.
     */
    private static synchronized void inicializarPool() {
        if (dataSource != null && !dataSource.isClosed()) return;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setDriverClassName("org.postgresql.Driver");

        // ── Tamaño del pool ──────────────────────────────────
        // NeonDB free tier permite ~5 conexiones concurrentes
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);          // conexiones listas en espera

        // ── Tiempos de espera ────────────────────────────────
        config.setConnectionTimeout(8_000);   // máx 8s para obtener conexión del pool
        config.setIdleTimeout(300_000);       // liberar conexión idle tras 5 min
        config.setMaxLifetime(600_000);       // reciclar conexión tras 10 min
        config.setKeepaliveTime(60_000);      // keepalive cada 1 min (evita timeout NeonDB)

        // ── Validación de conexión ───────────────────────────
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(3_000);

        // ── Performance ──────────────────────────────────────
        config.addDataSourceProperty("prepareThreshold", "5");  // PreparedStatement server-side cache
        config.addDataSourceProperty("preparedStatementCacheQueries", "256");
        config.addDataSourceProperty("preparedStatementCacheSizeMiB", "5");

        // ── Nombre del pool (visible en logs) ────────────────
        config.setPoolName("SIFACSW-Pool");

        dataSource = new HikariDataSource(config);
    }

    /**
     * Devuelve una conexión del pool. Al terminar de usarla, llama a
     * {@code connection.close()} — esto la devuelve al pool, NO la cierra físicamente.
     *
     * @return Conexión JDBC activa y válida
     * @throws SQLException si no hay conexiones disponibles
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            inicializarPool();
        }
        return dataSource.getConnection();
    }

    /**
     * Cierra completamente el pool de conexiones.
     * Llamar solo al cerrar la aplicación.
     */
    public static void cerrarPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
