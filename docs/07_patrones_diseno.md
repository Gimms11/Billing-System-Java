# SIFAC-SW — Patrones de Diseño (Perspectiva Académica IS)

## 7. Patrones de Diseño Implementados

> Los patrones de diseño son soluciones documentadas a problemas recurrentes en el diseño de software. Su estudio y aplicación es parte central de la Ingeniería del Software como disciplina.
> — Gang of Four (GoF), *"Design Patterns: Elements of Reusable Object-Oriented Software"*, 1994

---

### 7.1 ABSTRACT FACTORY — Familia de Objetos DAO

**Ubicación**: `persistence/dao/DAOFactory.java` + `persistence/impl/PostgresDAOFactory.java`

**Clasificación GoF**: Patrón Creacional

**Relación con IS**: Arquitectura de Aplicación (Unidad III) — desacoplamiento entre capas.

**Problema Resuelto**:
El código cliente (servicios) necesita obtener objetos de acceso a datos (DAOs) sin conocer el tipo de base de datos subyacente. Si mañana se cambia de PostgreSQL a MySQL, solo se crea una nueva fábrica; no se toca nada en las capas superiores.

**Implementación**:
```java
// DAOFactory.java — Clase abstracta (interfaz de la fábrica)
public abstract class DAOFactory {
    public static final int POSTGRES = 1;

    public abstract DAOCliente getClienteDAO();
    public abstract DAOProducto getProductoDAO();
    public abstract DAOComprobante getComprobanteDAO();
    // ... 11 métodos factory más

    // Método de selección de implementación
    public static DAOFactory getDAOFactory(int tipo) {
        return switch (tipo) {
            case POSTGRES -> new PostgresDAOFactory();
            default -> throw new IllegalArgumentException("BD no soportada");
        };
    }
}

// PostgresDAOFactory.java — Implementación concreta
public class PostgresDAOFactory extends DAOFactory {
    @Override
    public DAOCliente getClienteDAO() {
        return new DAOClienteImpl();  // Implementación PostgreSQL
    }
}
```

**Diagrama**:
```
DAOFactory (abstracta)
    ├── getClienteDAO() : DAOCliente      ← retorna interfaz
    ├── getProductoDAO() : DAOProducto
    └── ...
         ↑ extiende
PostgresDAOFactory
    ├── getClienteDAO() → new DAOClienteImpl()
    └── ...
```

**Beneficio Académico**: Demuestra el **Principio Abierto/Cerrado (OCP)** — el sistema está abierto para extensión (nueva fábrica MySQL) pero cerrado para modificación (no se toca código existente).

---

### 7.2 BUILDER — Construcción de Documentos PDF

**Ubicación**: `patterns/builder/` (5 clases)

**Clasificación GoF**: Patrón Creacional

**Relación con IS**: Arquitectura de Información (Unidad III) — producción de artefactos digitales.

**Problema Resuelto**:
Un documento PDF de comprobante tiene múltiples secciones ordenadas (encabezado, datos del cliente, tabla de items, pie con totales). Construirlo en un único método gigante viola SRP. El Builder divide la construcción en pasos, cada uno con una responsabilidad clara.

**Implementación**:
```java
// GeneradorPDFBuilder.java — Interfaz del builder
public interface GeneradorPDFBuilder {
    void iniciarDocumento(String ruta) throws Exception;
    void agregarEncabezado(Empresa empresa) throws Exception;
    void agregarDatosCliente(Cliente cliente, Comprobante c) throws Exception;
    void agregarCuerpo(List<DetalleComprobante> detalles) throws Exception;
    void agregarPie(Comprobante comprobante) throws Exception;
    void finalizarDocumento() throws Exception;
}

// GeneradorPDFDirector.java — Director que orquesta la construcción
public class GeneradorPDFDirector {
    public void construir(GeneradorPDFBuilder builder, ...) {
        builder.iniciarDocumento(ruta);
        builder.agregarEncabezado(empresa);
        builder.agregarDatosCliente(cliente, comprobante);
        builder.agregarCuerpo(detalles);
        builder.agregarPie(comprobante);
        builder.finalizarDocumento();
    }
}
```

**Diagrama de Clases**:
```
GeneradorPDFBuilder (interfaz)
    │
    ├── AbstractGeneradorPDF (implementa métodos comunes)
    │       │
    │       ├── GeneradorBoletaPDF  (formato boleta específico)
    │       └── GeneradorFacturaPDF (formato factura específico)
    │
GeneradorPDFDirector (orquestador)
    └── usa → GeneradorPDFBuilder
```

**Beneficio Académico**: Demuestra **Separación de Responsabilidades (SRP)** y el principio de **construcción en pasos**. El Director puede producir diferentes tipos de documentos usando el mismo protocolo de construcción.

---

### 7.3 STRATEGY — Navegación con Control de Permisos

**Ubicación**: `patterns/strategy/NavegacionStrategy.java` + 6 implementaciones

**Clasificación GoF**: Patrón de Comportamiento

**Relación con IS**: Arquitectura de Aplicación + Beneficios OO — Polimorfismo (Unidades II y III).

**Problema Resuelto**:
Cada módulo del sistema tiene su propia lógica de validación de permisos antes de abrir la vista. Sin Strategy, el código del menú tendría un `if-else` o `switch` gigante. Con Strategy, cada módulo encapsula su lógica de acceso y el menú solo llama `strategy.navegar()`.

**Implementación**:
```java
// NavegacionStrategy.java — Interfaz de estrategia
public interface NavegacionStrategy {
    void navegar() throws IOException;
    boolean tienePermiso();
    String getNombrePantalla();
}

// NavFacturacion.java — Estrategia concreta
public class NavFacturacion implements NavegacionStrategy {
    @Override
    public boolean tienePermiso() {
        return PermisosNavegacion.puedeFacturar();
    }
    @Override
    public void navegar() throws IOException {
        App.setRoot("GenFactures");
    }
    @Override
    public String getNombrePantalla() { return "Facturación"; }
}

// ControllerMenu.java — Contexto que usa la estrategia
NavegacionStrategy strategy = new NavFacturacion();
if (strategy.tienePermiso()) {
    strategy.navegar();
}
```

**Estrategias Implementadas**:
| Clase | Módulo que Protege | Permiso Verificado |
|-------|-------------------|-------------------|
| `NavDashboard` | Dashboard principal | Siempre permitido |
| `NavClientes` | Gestión de clientes | `puedeVerClientes()` |
| `NavProductos` | Gestión de productos | `puedeVerProductos()` |
| `NavFacturacion` | Emisión de comprobantes | `puedeFacturar()` |
| `NavHistorial` | Historial de transacciones | `puedeVerHistorial()` |
| `NavConfiguracion` | Configuración del sistema | `esAdministrador()` |

**Beneficio Académico**: Ejemplo canónico de **polimorfismo** — el menú trata todas las estrategias de forma uniforme. Cada nueva estrategia (nuevo módulo) se agrega sin modificar el menú.

---

### 7.4 ADAPTER — Unificación de Generadores XML y Email

**Ubicación**: `patterns/adapter/` (6 clases)

**Clasificación GoF**: Patrón Estructural

**Relación con IS**: Arquitectura Tecnológica (Unidad III) — integración de servicios externos.

**Problema Resuelto**:
Boletas y facturas generan XML con estructuras distintas (diferentes nodos, atributos, campos obligatorios). Sin Adapter, el código cliente necesita saber con qué tipo de comprobante trabaja. El Adapter unifica ambos generadores bajo una sola interfaz.

**Implementación**:
```java
// XMLGenerator.java — Interfaz unificada (el "target")
public interface XMLGenerator {
    String generar(Comprobante comprobante);
}

// BoletaXMLGenerator.java — Adaptador para boletas
public class BoletaXMLGenerator implements XMLGenerator {
    @Override
    public String generar(Comprobante comprobante) {
        // Genera XML con estructura de Boleta de Venta
        // Nodos específicos: BL-, UBL 2.1 boleta
    }
}

// FacturaXMLGenerator.java — Adaptador para facturas
public class FacturaXMLGenerator implements XMLGenerator {
    @Override
    public String generar(Comprobante comprobante) {
        // Genera XML con estructura de Factura
        // Nodos específicos: FG-, UBL 2.1 factura
    }
}

// XMLGeneratorFactory.java — Fábrica de adaptadores
public class XMLGeneratorFactory {
    public static XMLGenerator getXMLGenerator(TipoComprobante tipo) {
        return switch (tipo.getCodigo()) {
            case "BL" -> new BoletaXMLGenerator();
            case "FG" -> new FacturaXMLGenerator();
            default   -> throw new IllegalArgumentException("Tipo desconocido");
        };
    }
}
```

**Beneficio Académico**: Demuestra **inversión de dependencias (DIP)** — el código de alto nivel (`XMLService`) depende de la abstracción (`XMLGenerator`), no de implementaciones concretas.

---

### 7.5 SINGLETON — Gestión de Conexión a Base de Datos

**Ubicación**: `persistence/ConexionBD.java`

**Clasificación GoF**: Patrón Creacional

**Relación con IS**: Datos Centralizados (Unidad IV).

**Problema Resuelto**:
Abrir una nueva conexión JDBC en cada operación es costoso en recursos y puede causar agotamiento del pool de conexiones de NeonDB. El Singleton garantiza que toda la aplicación comparta una sola instancia de conexión activa.

**Implementación**:
```java
public class ConexionBD {
    private static ConexionBD instance;
    private Connection connection;

    private ConexionBD() throws SQLException {
        // Crea conexión a NeonDB con sslmode=require
        this.connection = DriverManager.getConnection(URL, USER, PASS);
    }

    public static ConexionBD getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() { return connection; }
}
```

**Beneficio Académico**: Demuestra **encapsulamiento** (constructor privado), **lazy initialization** y gestión de recursos críticos. Directamente relacionado con el concepto de **Datos Centralizados** de la Unidad IV.

---

### 7.6 DAO PATTERN — Abstracción de Persistencia

**Ubicación**: `persistence/dao/` + `persistence/impl/`

**Clasificación**: Patrón Arquitectónico (no GoF puro, pero catalogado en J2EE Patterns)

**Relación con IS**: Diseño Lógico y Diseño Físico (Unidad IV).

**Problema Resuelto**:
La lógica de negocio (servicios) no debe conocer cómo se guardan los datos. Si el servicio llama directamente a `Connection.prepareStatement(...)`, queda acoplado a JDBC y a PostgreSQL. El DAO aísla ese detalle.

**Implementación**:
```java
// DAOCliente.java — Interfaz (Diseño Lógico)
public interface DAOCliente {
    List<Cliente> listarTodos() throws SQLException;
    Cliente obtenerPorId(Long id) throws SQLException;
    void insertar(Cliente cliente) throws SQLException;
    void actualizar(Cliente cliente) throws SQLException;
    void eliminar(Long id) throws SQLException;
}

// DAOClienteImpl.java — Implementación PostgreSQL (Diseño Físico)
public class DAOClienteImpl implements DAOCliente {
    @Override
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY nombres";
        // ... PreparedStatement, ResultSet, mapeo a objetos Cliente
    }
}
```

**Beneficio Académico**: El DAO es el ejemplo más directo de la transición **Diseño Lógico → Diseño Físico** de la Unidad IV. La interfaz = diseño lógico abstracto; la implementación = diseño físico concreto en SQL.

---

### 7.7 Resumen de Patrones por Categoría GoF

| Categoría | Patrón | Archivo Principal | Unidad IS |
|-----------|--------|-------------------|-----------|
| **Creacional** | Abstract Factory | `DAOFactory.java` | III |
| **Creacional** | Builder | `GeneradorPDFDirector.java` | III-IV |
| **Creacional** | Singleton | `ConexionBD.java` | IV |
| **Estructural** | Adapter | `XMLGenerator.java` | III |
| **Comportamiento** | Strategy | `NavegacionStrategy.java` | II-III |
| **Arquitectónico** | DAO | `persistence/dao/*.java` | IV |

---

*Documento: `docs/07_patrones_diseño.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
