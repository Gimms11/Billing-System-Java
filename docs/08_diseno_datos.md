# SIFAC-SW — Diseño de Datos

## 8. Diseño de Datos: Conceptual, Lógico y Físico

> La Unidad IV del curso aborda el diseño de bases de datos en tres niveles de abstracción progresiva: desde las entidades del mundo real (conceptual) hasta las instrucciones SQL concretas (físico).
> SIFAC-SW implementa los tres niveles de forma explícita y verificable.

---

### 8.1 Diseño Conceptual

El diseño conceptual identifica las **entidades del negocio**, sus **atributos** y las **relaciones** entre ellas, sin considerar aún el SGBD o la tecnología de implementación.

#### Entidades Principales del Dominio

| Entidad | Descripción | Clase Java |
|---------|-------------|-----------|
| **Empresa** | La PyME propietaria del sistema | `model/Empresa.java` |
| **Usuario** | Persona que opera el sistema | `model/Usuario.java` |
| **Cliente** | Persona natural o jurídica que recibe el comprobante | `model/Cliente.java` |
| **Producto** | Bien o servicio vendido | `model/Producto.java` |
| **Comprobante** | Documento de pago emitido (Factura o Boleta) | `model/Comprobante.java` |
| **DetalleComprobante** | Línea de item dentro de un comprobante | `model/DetalleComprobante.java` |
| **TipoComprobante** | Catálogo: Factura, Boleta | `model/TipoComprobante.java` |
| **TipoDocumento** | DNI, RUC, CE, Pasaporte | `model/TipoDocumento.java` |
| **MedioPago** | Efectivo, Tarjeta, Transferencia | `model/MedioPago.java` |
| **CategoriaProductos** | Categorías del catálogo de productos | `model/CategoriaProductos.java` |
| **AfectacionProductos** | Tipo de afectación tributaria (IGV) | `model/AfectacionProductos.java` |
| **TipoImpuestos** | IGV, Exonerado, Inafecto | `model/TipoImpuestos.java` |
| **Departamento** | División geográfica nivel 1 | `model/Departamento.java` |
| **Provincia** | División geográfica nivel 2 | `model/Provincia.java` |
| **Distrito** | División geográfica nivel 3 | `model/Distrito.java` |

#### Relaciones del Modelo Conceptual

```
Empresa      ──── emite ────→  Comprobante  (1 empresa emite N comprobantes)
Usuario      ──── registra →   Comprobante  (1 usuario registra N comprobantes)
Cliente      ──── recibe ──→   Comprobante  (1 cliente tiene N comprobantes)
Comprobante  ──── contiene →   DetalleComprobante (1..N detalles por comprobante)
Producto     ──── aparece en → DetalleComprobante (1 producto en N detalles)
Cliente      ──── reside en →  Distrito     (N:1 — muchos clientes, un distrito)
Distrito     ──── pertenece →  Provincia    (N:1)
Provincia    ──── pertenece →  Departamento (N:1)

Comprobante  ──── es de tipo → TipoComprobante (N:1)
Comprobante  ──── paga con  →  MedioPago    (N:1)
Cliente      ──── tiene tipo → TipoDocumento (N:1)
Producto     ──── categoría →  CategoriaProductos (N:1)
Producto     ──── afectación → AfectacionProductos (N:1)
```

#### Diagrama E-R Conceptual (simplificado)

```
[Empresa]──emite──[Comprobante]──recibe──[Cliente]
                       │                    │
                   contiene             reside en
                       │                    │
              [DetalleComprobante]      [Distrito]
                       │                    │
                  aparece en           pertenece a
                       │                    │
                  [Producto]           [Provincia]
                                           │
                                      pertenece a
                                           │
                                      [Departamento]
```

---

### 8.2 Diseño Lógico

El diseño lógico transforma el modelo conceptual en un **esquema relacional**, definiendo tablas, columnas, tipos de datos abstractos y relaciones mediante claves foráneas. En SIFAC-SW, este nivel corresponde a las **interfaces DAO**.

#### Esquema Lógico — Tabla Principal: Comprobantes

```
COMPROBANTE
───────────────────────────────────────────────────
id_comprobante      : LONG (PK)
serie               : STRING
numero              : STRING
fecha_emision       : DATE
subtotal            : DECIMAL
igv                 : DECIMAL
total               : DECIMAL
id_cliente          : LONG (FK → CLIENTE)
id_tipo_comprobante : INT  (FK → TIPO_COMPROBANTE)
id_medio_pago       : INT  (FK → MEDIO_PAGO)
id_usuario          : INT  (FK → USUARIO)

DETALLE_COMPROBANTE
───────────────────────────────────────────────────
id_detalle          : LONG (PK)
id_comprobante      : LONG (FK → COMPROBANTE)
id_producto         : LONG (FK → PRODUCTO)
cantidad            : INT
precio_unitario     : DECIMAL
subtotal            : DECIMAL

CLIENTE
───────────────────────────────────────────────────
id_cliente          : LONG (PK)
nombres             : STRING
apellidos           : STRING
correo              : STRING
telefono            : STRING
direccion           : STRING
id_tipo_documento   : INT  (FK → TIPO_DOCUMENTO)
num_documento       : STRING
id_distrito         : INT  (FK → DISTRITO)

PRODUCTO
───────────────────────────────────────────────────
id_producto         : LONG (PK)
nombre              : STRING
descripcion         : STRING
precio              : DECIMAL
stock               : INT
id_categoria        : INT  (FK → CATEGORIA_PRODUCTOS)
id_afectacion       : INT  (FK → AFECTACION_PRODUCTOS)
```

#### Interfaces DAO como Representación del Diseño Lógico

```java
// DAOComprobante.java — define el contrato lógico de acceso a datos
public interface DAOComprobante {
    void insertar(Comprobante c) throws SQLException;
    Comprobante obtenerPorId(Long id) throws SQLException;
    List<Comprobante> listarPorRangoFechas(Date inicio, Date fin) throws SQLException;
    String obtenerSiguienteNumero(String serie) throws SQLException;
    // ...
}
```

---

### 8.3 Diseño Físico

El diseño físico implementa el esquema lógico en el **SGBD concreto**: PostgreSQL 15 en NeonDB. En SIFAC-SW, este nivel corresponde a las **implementaciones DAO** en `persistence/impl/`.

#### Tecnología de Implementación Física

| Componente | Valor |
|-----------|-------|
| SGBD | PostgreSQL 15 |
| Hosting | NeonDB (serverless, AWS us-east-1) |
| Driver | `postgresql-42.7.3.jar` (JDBC 4.2) |
| Protocolo | TCP/IP + SSL (`sslmode=require`) |
| Pool | Singleton (`ConexionBD`) |
| Acceso | `PreparedStatement` (prevención SQL Injection) |

#### Ejemplo de Diseño Físico: DAOComprobanteImpl

```java
// DAOComprobanteImpl.java — Implementación física en PostgreSQL
public class DAOComprobanteImpl implements DAOComprobante {

    @Override
    public void insertar(Comprobante c) throws SQLException {
        String sql = """
            INSERT INTO comprobantes
            (serie, numero, fecha_emision, subtotal, igv, total,
             id_cliente, id_tipo_comprobante, id_medio_pago, id_usuario)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        Connection conn = ConexionBD.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getSerie());
            ps.setString(2, c.getNumero());
            ps.setDate(3, Date.valueOf(c.getFechaEmision()));
            ps.setBigDecimal(4, c.getSubtotal());
            ps.setBigDecimal(5, c.getIgv());
            ps.setBigDecimal(6, c.getTotal());
            ps.setLong(7, c.getCliente().getIdCliente());
            // ... resto de parámetros
            ps.executeUpdate();
        }
    }
}
```

#### Transición Diseño Lógico → Diseño Físico

```
DISEÑO LÓGICO                    DISEÑO FÍSICO (PostgreSQL)
────────────────────             ─────────────────────────────
DAOComprobante.insertar()   →    INSERT INTO comprobantes ...
DAOComprobante.listar()     →    SELECT * FROM comprobantes ORDER BY ...
DAOCliente.obtenerPorId()   →    SELECT * FROM clientes WHERE id_cliente = ?
DAOProducto.actualizar()    →    UPDATE productos SET nombre=?, precio=? WHERE ...
DAOUbigeo.listarDistritos() →    SELECT d.*, p.nombre AS prov, dep.nombre AS dpto
                                  FROM distritos d JOIN provincias p ON ... JOIN ...
```

---

### 8.4 Datos Centralizados — Singleton `ConexionBD`

El patrón Singleton en `ConexionBD` implementa el concepto de **datos centralizados** de la Unidad IV:

- **Una sola fuente de verdad**: toda la aplicación accede a los datos a través de la misma conexión centralizada.
- **Consistencia garantizada**: no hay múltiples transacciones concurrentes descontroladas.
- **Gestión de recursos**: la conexión se reutiliza en lugar de crear y destruir en cada operación.

```
[ClienteService]     ─┐
[ProductoService]    ─┤
[ComprobanteService] ─┤─── ConexionBD (Singleton) ───→ PostgreSQL NeonDB
[AutenticacioService]─┤
[DAOClienteImpl]     ─┤
[DAOComprobanteImpl] ─┘
         ▲
    Todos los DAOs usan la misma instancia de conexión
```

---

### 8.5 Resumen: Los Tres Niveles en SIFAC-SW

| Nivel de Diseño | Artefacto en SIFAC-SW | Tecnología |
|----------------|----------------------|-----------|
| **Conceptual** | Clases en `model/` → Diagrama E-R | Java POJO |
| **Lógico** | Interfaces en `persistence/dao/` | Java Interfaces |
| **Físico** | Implementaciones en `persistence/impl/` | JDBC + SQL + PostgreSQL |

---

*Documento: `docs/08_diseño_datos.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
