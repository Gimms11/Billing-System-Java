# INFORME ACADÉMICO
## SIFAC-SW — Sistema de Facturación Académica para la Gestión del Ciclo de Vida del Software

---

**Universidad**: Universidad Tecnológica del Perú (UTP)
**Curso**: Ingeniería del Software
**Período**: 2025-2026

---

## 1. INTRODUCCIÓN

### 1.1 Presentación del Proyecto

**SIFAC-SW** (*Sistema de Facturación Académica para la Gestión del Ciclo de Vida del Software*) es una aplicación de escritorio desarrollada en **Java 24** con interfaz gráfica **JavaFX** y persistencia en una base de datos **PostgreSQL** alojada en la nube (NeonDB). Su dominio funcional es la facturación electrónica para pequeñas y medianas empresas (PyME): gestión de clientes, productos, emisión de comprobantes (facturas y boletas), generación de documentos PDF y XML, y control de acceso por roles.

Más allá de su función operativa, SIFAC-SW ha sido concebido y documentado como un **caso de estudio integral de Ingeniería del Software**: cada módulo, cada patrón de diseño, cada decisión arquitectónica y cada capa del sistema materializa y demuestra los conceptos teóricos estudiados a lo largo de las cuatro unidades del curso.

### 1.2 Contexto Académico

El curso de **Ingeniería del Software** aborda el desarrollo de sistemas desde una perspectiva disciplinada: no se trata únicamente de "escribir código", sino de aplicar un proceso sistemático, medible y controlable para producir software de calidad. SIFAC-SW encarna esa perspectiva:

- **No es un prototipo**: es un sistema funcional con arquitectura definida, capas separadas, patrones formales y persistencia real en la nube.
- **No es código suelto**: cada clase tiene una responsabilidad única (SRP), cada interfaz define un contrato (ISP), cada módulo es extensible sin modificación (OCP).
- **No es académico artificial**: el dominio de facturación es un negocio real, las tecnologías son estándares de la industria, y los patrones implementados son del catálogo GoF.

### 1.3 Por qué la Facturación como Dominio de Estudio

La facturación electrónica es un dominio idóneo para un caso de estudio de IS por las siguientes razones:

1. **Entidades bien definidas**: Clientes, Productos, Comprobantes, Detalles, Usuarios, Empresa — todas modelables como clases OO con atributos, métodos y relaciones claras.
2. **Reglas de negocio complejas**: Cálculo de IGV (18%), tipos de comprobante (factura vs. boleta), series correlativas, tipos de afectación tributaria.
3. **Múltiples formatos de salida**: Documentos PDF (patrón Builder con iText), documentos XML-UBL (patrón Adapter con DOM API), correo electrónico (patrón Adapter con JavaMail).
4. **Seguridad y roles**: Control de acceso basado en roles (RBAC), sesiones de usuario activas.
5. **Escalabilidad arquitectónica**: La arquitectura DAO permite cambiar el SGBD sin tocar la lógica de negocio.

---

## 2. JUSTIFICACIÓN

### 2.1 Justificación Académica

El desarrollo de SIFAC-SW responde directamente a las cuatro unidades del sílabo del curso de Ingeniería del Software:

| Eje del Curso | Cómo lo Demuestra SIFAC-SW |
|--------------|---------------------------|
| **Ciclo de Vida (Unidad I)** | El sistema atravesó fases formales: concepción → elaboración → construcción → transición. Cada fase produjo artefactos verificables (documentos, código, base de datos, sistema ejecutable). |
| **Paradigma OO (Unidad II)** | 17 clases de dominio con encapsulamiento, 2 jerarquías de herencia, 6+ interfaces polimórficas, 4 pilares OO demostrados con código real. |
| **Proceso Unificado (Unidad III)** | Arquitectura en 4 capas alineada con las arquitecturas del PU: negocios, aplicación, información y tecnológica. 6 patrones GoF implementados. |
| **Diseño de Datos (Unidad IV)** | Modelo E-R → esquema relacional normalizado → tablas físicas en PostgreSQL. Tres niveles de diseño verificables en el código fuente. |

### 2.2 Justificación Técnica

| Decisión Tecnológica | Justificación de IS |
|---------------------|-------------------|
| **Java 24** | Tipado estático, soporte nativo de interfaces y herencia, JVM como plataforma estable. Lenguaje de referencia empresarial. |
| **JavaFX** | Implementa el patrón MVC de forma nativa: FXML (Vista) + Controller + Model. Permite enseñar MVC de forma tangible. |
| **DAO + Abstract Factory** | Separación de concerns — la lógica de negocio no conoce el SGBD. Principio fundamental de IS. |
| **PostgreSQL en NeonDB** | Infraestructura real de producción (cloud serverless, SSL), cierra la brecha entre academia e industria. |
| **6 Patrones GoF** | Los patrones de diseño son el vocabulario compartido de los ingenieros de software. Aplicarlos en un dominio real los internaliza con efectividad. |

### 2.3 Justificación de Patrones Aplicados

| Patrón GoF | Problema Resuelto | Principio IS |
|-----------|------------------|--------------|
| **Abstract Factory** | Crear DAOs sin acoplarse al SGBD | Principio Abierto/Cerrado (OCP) |
| **Builder** | Construir PDFs en pasos ordenados | Responsabilidad Única (SRP) |
| **Strategy** | Navegación polimórfica con permisos | Polimorfismo / DIP |
| **Adapter** | Unificar generadores XML heterogéneos | Inversión de Dependencias |
| **Singleton** | Una sola instancia de conexión a BD | Gestión de recursos críticos |
| **DAO** | Aislar acceso a datos de lógica de negocio | Separación de capas (IS) |

---

## 3. OBJETIVOS

### 3.1 Objetivo General

Desarrollar e implementar **SIFAC-SW**, aplicando de forma integral los fundamentos teórico-prácticos de la **Ingeniería del Software**: ciclo de vida, paradigma orientado a objetos, proceso unificado, arquitecturas empresariales y diseño de datos en sus niveles conceptual, lógico y físico; tomando como dominio de negocio la facturación electrónica de una PyME.

### 3.2 Objetivos Específicos

**OE-01 [Unidad I]**: Documentar el ciclo de vida del software, identificando fases de concepción, elaboración, construcción y transición del Proceso Unificado, y los artefactos producidos en cada fase.

**OE-02 [Unidad II]**: Implementar el paradigma OO mediante clases, objetos, herencia, polimorfismo por interfaz, encapsulamiento y abstracción en el modelo de dominio de facturación (17 clases en `model/`).

**OE-03 [Unidad III]**: Diseñar una arquitectura multicapa (presentación → lógica de negocio → acceso a datos → datos) alineada con las 4 arquitecturas del Proceso Unificado.

**OE-04 [Unidad III]**: Implementar 6 patrones GoF (Abstract Factory, Builder, Strategy, Adapter, Singleton, DAO) en código funcional, documentando problema, solución y beneficio de cada uno.

**OE-05 [Unidad IV]**: Construir el modelo de datos en 3 niveles: conceptual (`model/`), lógico (`persistence/dao/`) y físico (`persistence/impl/` + PostgreSQL).

**OE-06 [Unidades I, IV]**: Generar artefactos digitales tangibles (PDF, XML, email) como evidencia de la operatividad completa del sistema y del ciclo de vida del producto de software.

**OE-07 [Unidad III]**: Implementar control de acceso basado en roles (RBAC) con patrón Strategy para validación de permisos en tiempo de ejecución.

---

## 4. ALCANCE

### 4.1 Módulos Implementados

| Módulo | Funcionalidades | Estado |
|--------|----------------|--------|
| **Autenticación** | Login, roles, sesión, logout | ✅ Completo |
| **Clientes** | CRUD completo + ubigeo peruano | ✅ Completo |
| **Productos** | CRUD + categorías + afectación tributaria | ✅ Completo |
| **Comprobantes** | Factura y boleta + PDF + XML + email | ✅ Completo |
| **Historial** | Listado filtrado + re-descarga PDF | ✅ Completo |
| **Dashboard** | Métricas de facturación | ✅ Completo |

### 4.2 Fuera del Alcance

- Integración SUNAT/SRI para validación tributaria real
- Versión web o móvil
- Pruebas automatizadas JUnit
- Módulo de contabilidad avanzada

---

## 5. ARQUITECTURA DEL SISTEMA (UNIDAD III)

### 5.1 Arquitectura de Negocios

Los procesos de negocio principales automatizados por SIFAC-SW:

1. **Autenticación y autorización**: validación de credenciales + carga de permisos por rol.
2. **Gestión de catálogos**: CRUD de clientes y productos.
3. **Emisión de comprobantes**: proceso central del negocio (selección → cálculo → generación → distribución).
4. **Consulta de historial**: reporting y trazabilidad de transacciones.

**Reglas de negocio implementadas**:
- IGV = 18% sobre base imponible (solo productos gravados)
- Numeración correlativa automática por serie (BL-001, FG-001...)
- Solo usuarios con permiso pueden acceder a cada módulo
- Boleta para persona natural / Factura para persona jurídica (RUC)

### 5.2 Arquitectura de Aplicación — MVC en 4 Capas

```
CAPA 1: PRESENTACIÓN    → controller/ + resources/*.fxml (JavaFX MVC)
CAPA 2: LÓGICA NEGOCIO  → service/ (15 servicios, reglas de negocio)
CAPA 3: ACCESO A DATOS  → persistence/dao/ + persistence/impl/ (DAO)
CAPA 4: DATOS           → PostgreSQL 15 en NeonDB (cloud)
```

### 5.3 Arquitectura de Información

Flujo de transformación de la información:

```
ENTRADA (datos del usuario en UI)
    ↓ validación en controller
PROCESAMIENTO (reglas de negocio en service)
    ↓ persistencia en dao
ALMACENAMIENTO (PostgreSQL en NeonDB)
    ↓ generación de artefactos
SALIDA: PDF (Builder) + XML (Adapter) + Email (Adapter)
```

### 5.4 Arquitectura Tecnológica

| Capa | Tecnología | Versión |
|------|-----------|--------|
| UI Desktop | JavaFX + FXML | 24.0.1 |
| Lógica | Java | 24 |
| Build | Apache Maven | 3.8.1+ |
| SGBD | PostgreSQL | 15 |
| Cloud | NeonDB | Serverless |
| PDF | iText | 5.5.13 |
| XML | Java DOM API | JDK 24 |
| Email | JavaMail | 1.6.2 |
| Iconos | iKonli | 12.4.0 |

---

## 6. PARADIGMA ORIENTADO A OBJETOS (UNIDAD II)

### 6.1 Los 4 Pilares OO en SIFAC-SW

#### Encapsulamiento
```java
public class Cliente {
    private Long idCliente;       // atributo privado
    private String nombres;
    public String getNombres() { return nombres; }           // acceso controlado
    public void setNombres(String n) { if (n != null) this.nombres = n; }
}
```

#### Herencia
```java
AbstractGeneradorPDF (clase base abstracta — métodos comunes de generación PDF)
    ├── GeneradorBoletaPDF  extends AbstractGeneradorPDF
    └── GeneradorFacturaPDF extends AbstractGeneradorPDF
```

#### Polimorfismo
```java
NavegacionStrategy estrategia = new NavFacturacion();
// o new NavClientes(), new NavProductos(), new NavHistorial()...
estrategia.navegar();        // comportamiento diferente según instancia concreta
estrategia.tienePermiso();   // validación específica por módulo
```

#### Abstracción
```java
public abstract class DAOFactory {           // oculta CUÁL base de datos
    public abstract DAOCliente getClienteDAO();
    public abstract DAOProducto getProductoDAO();
}
// El código cliente no sabe si es PostgreSQL, MySQL u Oracle
```

### 6.2 Clases e Interfaces del Sistema

| Categoría | Cantidad | Ejemplo |
|-----------|---------|---------|
| Clases de dominio (model) | 17 | `Cliente`, `Comprobante`, `Producto` |
| Controladores JavaFX | 10 | `ControllerFacturas`, `ControllerLogin` |
| Servicios de negocio | 15 | `ComprobanteService`, `genPDFService` |
| Interfaces DAO | 14 | `DAOCliente`, `DAOComprobante` |
| Implementaciones DAO | 14 | `DAOClienteImpl`, `DAOComprobanteImpl` |
| Clases de patrones | 17 | Builders, Adapters, Strategies |
| **Total** | **~87** | **clases e interfaces Java** |

---

## 7. DISEÑO DE DATOS (UNIDAD IV)

### 7.1 Diseño Conceptual — Entidades del Negocio

Entidades identificadas (representadas como clases Java en `model/`):

| Entidad | Clase Java | Relaciones Principales |
|---------|-----------|----------------------|
| **Comprobante** | `Comprobante.java` | 1 Cliente, 1 TipoComprobante, 1 MedioPago, N Detalles |
| **DetalleComprobante** | `DetalleComprobante.java` | 1 Comprobante, 1 Producto |
| **Cliente** | `Cliente.java` | 1 TipoDocumento, 1 Distrito |
| **Producto** | `Producto.java` | 1 Categoria, 1 Afectacion |
| **Usuario** | `Usuario.java` | Rol de acceso |
| **Empresa** | `Empresa.java` | Emisor de comprobantes |
| + 11 catálogos | `TipoComprobante`, `MedioPago`, `Ubigeo`... | — |

### 7.2 Diseño Lógico — Interfaces DAO

Las interfaces en `persistence/dao/` representan el esquema lógico — operaciones CRUD abstractas independientes del SGBD:

```java
public interface DAOComprobante {
    void insertar(Comprobante c) throws SQLException;
    Comprobante obtenerPorId(Long id) throws SQLException;
    List<Comprobante> listarPorRangoFechas(Date inicio, Date fin) throws SQLException;
    String obtenerSiguienteNumero(String serie) throws SQLException;
}
```

### 7.3 Diseño Físico — Implementaciones SQL (PostgreSQL)

Las implementaciones en `persistence/impl/` traducen el diseño lógico a sentencias SQL sobre PostgreSQL 15:

```java
// Diseño Físico: SQL real sobre NeonDB
public class DAOComprobanteImpl implements DAOComprobante {
    public void insertar(Comprobante c) throws SQLException {
        String sql = "INSERT INTO comprobantes (serie, numero, fecha_emision, " +
                     "subtotal, igv, total, id_cliente, id_tipo_comprobante) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // PreparedStatement + ConexionBD.getInstance()
    }
}
```

### 7.4 Datos Centralizados — Singleton ConexionBD

```java
// Una sola instancia para toda la aplicación → Datos Centralizados
ConexionBD.getInstance().getConnection();
```

**Transición de niveles**:
```
model/ (Java POJO)     → Diseño Conceptual (entidades del negocio)
persistence/dao/       → Diseño Lógico    (contratos CRUD abstractos)
persistence/impl/ + SQL → Diseño Físico   (PostgreSQL real)
```

---

## 8. CICLO DE VIDA DEL SOFTWARE (UNIDAD I)

### Fases del Proceso Unificado Aplicadas a SIFAC-SW

| Fase PU | Actividades Realizadas | Artefactos Producidos |
|---------|----------------------|----------------------|
| **Concepción** | Identificación del dominio (facturación PyME), definición de actores (admin, cajero, consultor), casos de uso principales | Documento de visión, lista de casos de uso |
| **Elaboración** | Diseño de arquitectura en 4 capas, selección de patrones GoF, diseño del esquema E-R | Arquitectura base, diseño de datos, selección tecnológica |
| **Construcción** | Codificación de ~87 clases Java, vistas FXML, CSS, integración con NeonDB | Código fuente funcional, sistema ejecutable |
| **Transición** | Sistema ejecutable, documentación académica, README, informe, generación de PDFs y XMLs de prueba | Sistema entregado, documentación completa |

### Ciclo de Vida del Producto de Software

```
ENTRADA DEL USUARIO (datos del negocio)
        ↓
PROCESAMIENTO POR EL SISTEMA (lógica de negocio)
        ↓
PERSISTENCIA EN BASE DE DATOS (PostgreSQL NeonDB)
        ↓
GENERACIÓN DE ARTEFACTOS (PDF, XML, Email)
        ↓
ENTREGA AL USUARIO FINAL (documento en pantalla + archivo)
```

El ciclo completo — desde la entrada del usuario hasta el artefacto final — demuestra el **ciclo de vida del software** funcionando en producción.

---

## 9. CONCLUSIONES

1. **SIFAC-SW** demuestra que la Ingeniería del Software no es solo teoría: cada concepto del sílabo tiene una implementación concreta y verificable en el código fuente del sistema.

2. Los **patrones de diseño GoF** (Abstract Factory, Builder, Strategy, Adapter, Singleton, DAO) no son adornos académicos sino soluciones a problemas reales de extensibilidad, mantenibilidad y desacoplamiento.

3. La **arquitectura en 4 capas** (presentación → negocio → datos → BD) es la manifestación física del Proceso Unificado: cada capa corresponde a una vista arquitectónica del PU (negocios, aplicación, información, tecnológica).

4. El **diseño de datos en 3 niveles** (conceptual → lógico → físico) demuestra que una buena arquitectura de información parte de las entidades del negocio y llega hasta las sentencias SQL concretas de forma coherente y trazable.

5. El uso de **tecnologías reales** (PostgreSQL en NeonDB cloud, JavaFX, iText, JavaMail) posiciona a SIFAC-SW como un proyecto con valor más allá del académico: es un sistema de facturación funcional construido con las mismas herramientas que usaría un equipo profesional.

---

## 10. REFERENCIAS

- Gamma, E., Helm, R., Johnson, R., Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
- Jacobson, I., Rumbaugh, J., Booch, G. (1999). *The Unified Software Development Process*. Addison-Wesley.
- Oracle. (2024). *Java SE 24 Documentation*. https://docs.oracle.com/en/java/
- OpenJFX. (2024). *JavaFX 24 Documentation*. https://openjfx.io/
- PostgreSQL Global Development Group. (2024). *PostgreSQL 15 Documentation*. https://www.postgresql.org/
- NeonDB. (2024). *Neon Serverless PostgreSQL Documentation*. https://neon.tech/docs/
- Pressman, R.S. (2010). *Software Engineering: A Practitioner's Approach* (7th ed.). McGraw-Hill.

---

*SIFAC-SW — Sistema de Facturación Académica para la Gestión del Ciclo de Vida del Software*
*Universidad Tecnológica del Perú (UTP) — Ingeniería del Software — 2025-2026*
