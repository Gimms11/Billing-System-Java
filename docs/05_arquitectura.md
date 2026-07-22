# SIFAC-SW — Arquitectura del Sistema

## 5. Arquitectura del Sistema

> *"La arquitectura de software es el conjunto de decisiones significativas sobre la organización de un sistema de software, la selección de los elementos estructurales y sus interfaces, su comportamiento como se especifica en las colaboraciones entre esos elementos."*
> — Philippe Kruchten, RUP/Proceso Unificado

---

### 5.1 Arquitectura de Negocios (Unidad III)

La arquitectura de negocios describe los **procesos de negocio** que el sistema automatiza.

#### Procesos de Negocio Principales

```
PROCESO 1: Autenticación
  Usuario ingresa credenciales → Sistema valida en BD → Carga perfil y permisos → Accede al menú

PROCESO 2: Gestión de Catálogos
  Actor autorizado → Registra/Edita Clientes o Productos → BD actualizada

PROCESO 3: Emisión de Comprobante (Proceso Central)
  Seleccionar cliente → Agregar productos → Calcular totales con IGV →
  Confirmar → Guardar en BD → Generar PDF → Generar XML → Enviar por email

PROCESO 4: Consulta de Historial
  Actor → Filtra por fechas/tipo → Visualiza lista → Descarga PDF

PROCESO 5: Dashboard
  Actor → Visualiza métricas de facturación
```

#### Reglas de Negocio Clave

| Regla | Implementación en Código |
|-------|-------------------------|
| IGV = 18% sobre base imponible | `ComprobanteService.calcularIGV()` |
| Numeración correlativa por serie | `DAOComprobanteImpl.obtenerSiguienteNumero()` |
| Solo usuarios con permiso facturan | `PermisosNavegacion.puedeFacturar()` |
| Boleta → persona natural / Factura → empresa | `TipoComprobante` + validación en UI |
| Un comprobante tiene 1..N detalles | Relación `Comprobante` ↔ `DetalleComprobante` |

---

### 5.2 Arquitectura de Aplicación (Unidad III)

SIFAC-SW implementa el patrón **MVC (Model-View-Controller)** combinado con una **arquitectura en 4 capas**:

```
┌──────────────────────────────────────────────────────────┐
│  CAPA 1: PRESENTACIÓN (View + Controller)                │
│                                                          │
│  View:       login.fxml, Menu.fxml, GenClientes.fxml,   │
│              GenFactures.fxml, History.fxml, ...         │
│  Controller: ControllerLogin, ControllerFacturas,        │
│              ControllerClientes, ControllerProducts,     │
│              ControllerHistorial, ControllerMenu,        │
│              DashboardController, ControllerTopMenu      │
│                                                          │
│  Patrón: MVC | Tecnología: JavaFX 24 + FXML             │
└──────────────────────┬───────────────────────────────────┘
                       │ llamadas a servicios
┌──────────────────────▼───────────────────────────────────┐
│  CAPA 2: LÓGICA DE NEGOCIO (Service Layer)               │
│                                                          │
│  ClienteService      → CRUD y validación de clientes     │
│  ProductoService     → CRUD y validación de productos    │
│  ComprobanteService  → Emisión y cálculo de comprobantes │
│  genPDFService       → Orquesta generación PDF (Builder) │
│  XMLService          → Orquesta generación XML (Adapter) │
│  AutenticacioService → Login y gestión de sesión         │
│  PermisosNavegacion  → Control de acceso RBAC            │
│  ManejadorService    → Fachada de servicios              │
│                                                          │
│  Patrones: Strategy, Builder, Adapter, Fachada implícita │
└──────────────────────┬───────────────────────────────────┘
                       │ operaciones CRUD
┌──────────────────────▼───────────────────────────────────┐
│  CAPA 3: ACCESO A DATOS (DAO + Abstract Factory)         │
│                                                          │
│  dao/DAOFactory      → Fábrica abstracta de DAOs         │
│  dao/DAOCliente      → Interfaz: CRUD clientes           │
│  dao/DAOProducto     → Interfaz: CRUD productos          │
│  dao/DAOComprobante  → Interfaz: CRUD comprobantes       │
│  dao/DAOUsuario      → Interfaz: autenticación           │
│  impl/PostgresDAOFactory → Implementación concreta PG    │
│  impl/DAOClienteImpl → SQL queries PostgreSQL            │
│  persistence/ConexionBD → Singleton de conexión BD       │
│                                                          │
│  Patrones: DAO, Abstract Factory, Singleton              │
└──────────────────────┬───────────────────────────────────┘
                       │ JDBC / SSL
┌──────────────────────▼───────────────────────────────────┐
│  CAPA 4: DATOS (PostgreSQL en NeonDB)                    │
│                                                          │
│  Tablas: clientes, productos, comprobantes,              │
│          detalle_comprobantes, usuarios, empresa,        │
│          ubigeo (dpto/prov/distrito), catálogos          │
│                                                          │
│  Tecnología: PostgreSQL 15 | NeonDB Serverless | SSL     │
└──────────────────────────────────────────────────────────┘
```

#### Flujo MVC — Ejemplo: Emitir una Factura

```
1. Usuario interactúa con GenFactures.fxml (VIEW)
2. ControllerFacturas captura el evento @FXML (CONTROLLER)
3. ControllerFacturas llama ComprobanteService.crear() (SERVICE)
4. ComprobanteService llama DAOComprobante.insertar() (DAO)
5. DAOComprobanteImpl ejecuta SQL INSERT via JDBC (BD)
6. ComprobanteService llama genPDFService.generarFactura() (SERVICE)
7. genPDFService usa GeneradorPDFDirector + GeneradorFacturaPDF (BUILDER)
8. PDF guardado en resources/PDFs/
9. ComprobanteService llama XMLService.generar() (SERVICE)
10. XMLService usa XMLGeneratorFactory + FacturaXMLGenerator (ADAPTER)
11. XML guardado en resources/xml/
12. Controller actualiza la vista con el resultado (VIEW)
```

---

### 5.3 Arquitectura de Información (Unidad III)

La arquitectura de información describe cómo fluye y se transforma la **información** dentro del sistema.

#### Flujos de Información

```
ENTRADA                    TRANSFORMACIÓN              SALIDA
─────────                  ──────────────              ──────
Datos del cliente     →    Validación + CRUD      →   Registro en BD
Datos del producto    →    Validación + CRUD      →   Catálogo actualizado
Selección factura     →    Cálculo IGV/totales    →   Comprobante en BD
Comprobante en BD     →    Builder (iText)        →   Documento PDF
Comprobante en BD     →    Adapter (DOM XML)      →   Archivo XML-UBL
Comprobante PDF       →    Adapter (JavaMail)     →   Email al cliente
Comprobantes BD       →    Consulta filtrada      →   Historial en pantalla
```

#### Artefactos de Información Generados

| Artefacto | Formato | Generado Por | Destino |
|-----------|---------|--------------|---------|
| Comprobante de pago | PDF | Builder (iText 5) | `resources/PDFs/` |
| Documento electrónico | XML-UBL 2.1 | Adapter (DOM API) | `resources/xml/` |
| Notificación | Email HTML | Adapter (JavaMail) | Correo del cliente |
| Sesión de usuario | JSON | ManejadorSesion | `sesionActual.json` |
| Config empresa | JSON | ManejadorEmpresa | `empresa.json` |

---

### 5.4 Arquitectura Tecnológica (Unidad III)

#### Stack Tecnológico Completo

```
┌─────────────────────────────────────────────────────────┐
│                   CLIENTE (Desktop)                      │
│   Sistema Operativo: Windows / Linux / macOS            │
│   JVM: Java 24 (OpenJDK)                                │
│   UI Framework: JavaFX 24.0.1                           │
│   Iconos: iKonli 12.4.0 (FontAwesome 5, Ant Design)     │
│   Fuentes: Roboto (Google Fonts, embebidas)             │
└──────────────────────┬──────────────────────────────────┘
                       │ JDBC / TCP / SSL
┌──────────────────────▼──────────────────────────────────┐
│                   SERVIDOR DE DATOS (Cloud)              │
│   SGBD: PostgreSQL 15                                    │
│   Hosting: NeonDB (Serverless, AWS us-east-1)           │
│   Protocolo: JDBC + sslmode=require                     │
│   Driver: postgresql-42.7.3.jar                         │
└──────────────────────────────────────────────────────────┘
                       │ SMTP
┌──────────────────────▼──────────────────────────────────┐
│               SERVIDOR DE EMAIL (Externo)                │
│   Servicio: MailTrap (sandbox SMTP para desarrollo)     │
│   Librería: JavaMail API 1.6.2 (javax.mail)             │
└──────────────────────────────────────────────────────────┘
```

#### Herramientas de Desarrollo

| Herramienta | Versión | Rol |
|-------------|---------|-----|
| Apache Maven | 3.8.1+ | Gestión de dependencias y ciclo de build |
| JavaFX Maven Plugin | 0.0.6 | Ejecución con módulos JavaFX |
| iText | 5.5.13.3 | Generación de documentos PDF |
| JOL Core | 0.16 | Análisis de memory layout (utilidad) |

---

*Documento: `docs/05_arquitectura.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
