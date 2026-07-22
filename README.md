# 🎓 SIFAC-SW — Sistema de Facturación Académica para la Gestión del Ciclo de Vida del Software

![Banner del Proyecto](images/banner.png)

---

## 📌 Descripción del Proyecto

**SIFAC-SW** es un sistema de información de escritorio desarrollado íntegramente en **Java 24** con interfaz gráfica **JavaFX** y persistencia en **PostgreSQL (NeonDB cloud)**. Su dominio funcional es la **facturación electrónica para una PyME**: gestión de clientes, productos, emisión de comprobantes (facturas y boletas), generación de documentos PDF y XML, historial de transacciones y control de acceso por roles.

El proyecto está diseñado y documentado como **caso de estudio integral de Ingeniería del Software**, donde cada módulo, patrón y capa arquitectónica evidencia los conceptos del curso:

| Unidad IS | Concepto | Evidencia en SIFAC-SW |
|-----------|---------|----------------------|
| **Unidad I** | Ciclo de Vida del Software | Fases de concepción → elaboración → construcción → transición del PU |
| **Unidad II** | Paradigma Orientado a Objetos | 17 clases de dominio, herencia, polimorfismo, encapsulamiento, abstracción |
| **Unidad III** | Proceso Unificado / Arquitecturas | MVC + capas + 4 arquitecturas del PU + 6 patrones GoF |
| **Unidad IV** | Diseño Conceptual / Lógico / Físico | model/ → dao/ → impl/ → PostgreSQL |

---

## 🎯 Objetivo General

Desarrollar e implementar **SIFAC-SW**, aplicando los fundamentos de la Ingeniería del Software — ciclo de vida, paradigma OO, proceso unificado, arquitecturas empresariales y diseño de datos en tres niveles — tomando la facturación electrónica como dominio de negocio.

---

## 🏗️ Stack Tecnológico

![Java](https://img.shields.io/badge/Java-24-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-24.0.1-0596D8?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![NeonDB](https://img.shields.io/badge/NeonDB%20Cloud-Active-00D084?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![iText](https://img.shields.io/badge/iText-5.5.13-003D8F?style=for-the-badge)

| Componente | Versión | Propósito |
|-----------|---------|----------|
| **Java JDK** | 24 | Runtime y compilación |
| **JavaFX** | 24.0.1 | Framework de interfaz gráfica (MVC — Capa Vista) |
| **PostgreSQL** | 15+ | SGBD relacional (Diseño Físico — Unidad IV) |
| **NeonDB** | Cloud | Hosting PostgreSQL serverless (Arquitectura Tecnológica — Unidad III) |
| **Maven** | 3.8.1+ | Gestor de dependencias y ciclo de build |
| **iText** | 5.5.13 | Generación de PDFs — patrón Builder |
| **iKonli** | 12.4.0 | Librería de iconos (FontAwesome, Ant Design) |
| **JavaMail** | 1.6.2 | Envío de correos — patrón Adapter |

---

## 🎨 Patrones de Diseño GoF Implementados

### 1️⃣ ABSTRACT FACTORY — Familia de Objetos DAO
**Ubicación**: `persistence/dao/DAOFactory` + `persistence/impl/PostgresDAOFactory`
**Relación IS**: Arquitectura de Aplicación (Unidad III) — Principio Abierto/Cerrado

```java
DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
DAOCliente daoCliente = factory.getClienteDAO();
```

Permite cambiar de PostgreSQL a cualquier otro SGBD sin tocar la lógica de negocio.

---

### 2️⃣ BUILDER — Construcción de Documentos PDF
**Ubicación**: `patterns/builder/` (Director, Builder, GeneradorBoletaPDF, GeneradorFacturaPDF)
**Relación IS**: Arquitectura de Información (Unidad III) — SRP, pasos ordenados

```
GeneradorPDFDirector → iniciarDocumento() → agregarEncabezado() → agregarCuerpo() → finalizarDocumento()
```

Construye documentos PDF en pasos controlados, reutilizando el Director para boletas y facturas.

---

### 3️⃣ STRATEGY — Navegación con Control de Permisos
**Ubicación**: `patterns/strategy/NavegacionStrategy` + 6 implementaciones
**Relación IS**: Polimorfismo / Beneficios OO (Unidad II) + Arquitectura de Aplicación (Unidad III)

```java
NavegacionStrategy estrategia = new NavFacturacion();
if (estrategia.tienePermiso()) { estrategia.navegar(); }
```

Cada módulo encapsula su lógica de validación de permisos. El menú es agnóstico al tipo de módulo.

---

### 4️⃣ ADAPTER — Unificación de Generadores XML y Email
**Ubicación**: `patterns/adapter/XMLGenerator` + implementaciones + `MailTrapEmailAdapter`
**Relación IS**: Arquitectura Tecnológica (Unidad III) — DIP, integración de servicios externos

```java
XMLGenerator gen = XMLGeneratorFactory.getXMLGenerator(tipoComprobante);
gen.generar(comprobante); // Retorna BoletaXMLGenerator o FacturaXMLGenerator
```

---

### 5️⃣ SINGLETON — Conexión Única a Base de Datos
**Ubicación**: `persistence/ConexionBD`
**Relación IS**: Datos Centralizados (Unidad IV)

```java
ConexionBD.getInstance().getConnection(); // Siempre la misma instancia
```

---

### 6️⃣ DAO PATTERN — Abstracción de Persistencia
**Ubicación**: `persistence/dao/` (interfaces) + `persistence/impl/` (implementaciones PostgreSQL)
**Relación IS**: Diseño Lógico → Diseño Físico (Unidad IV)

```
DAOCliente (interfaz — Diseño Lógico) → DAOClienteImpl (SQL — Diseño Físico)
```

---

## 🏛️ Arquitectura en 4 Capas (Unidad III)

```
┌──────────────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN (View + Controller)            │
│  JavaFX FXML + ControllerLogin, ControllerFacturas…  │
│  [Arquitectura de Aplicación — MVC]                  │
└──────────────────────┬───────────────────────────────┘
                       │ delegación
┌──────────────────────▼───────────────────────────────┐
│  CAPA DE LÓGICA DE NEGOCIO (Service Layer)           │
│  ClienteService, ComprobanteService, genPDFService…  │
│  [Arquitectura de Negocios — Reglas del negocio]     │
└──────────────────────┬───────────────────────────────┘
                       │ CRUD
┌──────────────────────▼───────────────────────────────┐
│  CAPA DE ACCESO A DATOS (DAO + Abstract Factory)     │
│  DAOFactory → DAOClienteImpl, DAOComprobanteImpl…    │
│  [Diseño Lógico → Diseño Físico — Unidad IV]         │
└──────────────────────┬───────────────────────────────┘
                       │ JDBC / SSL
┌──────────────────────▼───────────────────────────────┐
│  CAPA DE DATOS (PostgreSQL en NeonDB)                │
│  Tablas: clientes, productos, comprobantes, ubigeo…  │
│  [Diseño Físico — Datos Centralizados — Unidad IV]   │
└──────────────────────────────────────────────────────┘
```

---

## 📁 Estructura del Proyecto

```
SIFAC-SW (Patrones/)
├── README.md                        ← Este documento
├── INFORME_ACADEMICO.md             ← Informe para sustentación
│
├── docs/                            ← Documentación académica modular
│   ├── 01_introduccion.md           ← Unidad I: Contexto y presentación
│   ├── 02_justificacion.md          ← Justificación académica y técnica
│   ├── 03_objetivos.md              ← OG + 7 Objetivos Específicos
│   ├── 04_alcance.md                ← Funcionalidades incluidas/excluidas
│   ├── 05_arquitectura.md           ← 4 Arquitecturas del PU (Unidad III)
│   ├── 06_silabo_mapping.md         ← Tabla módulo ↔ unidad ↔ concepto IS
│   ├── 07_patrones_diseno.md        ← 6 patrones GoF con código y análisis
│   └── 08_diseno_datos.md           ← Diseño conceptual/lógico/físico
│
├── images/                          ← Capturas de pantalla del sistema
│
└── demo/                            ← Módulo Maven ejecutable
    ├── pom.xml                      ← Java 24, JavaFX 24, PostgreSQL driver
    └── src/main/
        ├── java/pe/utp/facturacion/
        │   ├── core/                [Núcleo — Arranque del Sistema — Unidad I]
        │   ├── controller/          [Capa Presentación — MVC — Unidad III]
        │   ├── model/               [Modelo de Dominio — POO — Unidad II]
        │   ├── patterns/
        │   │   ├── adapter/         [Patrón Adaptador — Unidad III]
        │   │   ├── builder/         [Patrón Constructor — Unidad III]
        │   │   └── strategy/        [Patrón Estrategia — Unidad II-III]
        │   ├── persistence/
        │   │   ├── dao/             [Diseño Lógico — Unidad IV]
        │   │   └── impl/            [Diseño Físico — Unidad IV]
        │   ├── service/             [Arquitectura de Negocios — Unidad III]
        │   └── ui/loading/          [Componente de Arranque — Unidad I]
        │
        └── resources/pe/utp/facturacion/
            ├── *.fxml               [Vistas JavaFX — Capa V del MVC]
            ├── styles/              [Sistema de Diseño Visual]
            ├── PDFs/                [Artefactos PDF Generados — Salida del Sistema]
            └── xml/                 [Artefactos XML-UBL Generados]
```

---

## 🔄 Flujos de Procesos Principales (Proceso Unificado — Unidad III)

### Flujo 1: Autenticación
```
Login UI (ControllerLogin)
    ↓ credenciales
AutenticacioService.validar()
    ↓
DAOUsuario.obtenerPorCredenciales()  ← Diseño Físico (Unidad IV)
    ↓ éxito
ManejadorSesion.guardarSesion()     ← Strategy + permisos
    ↓
Menu principal con NavegacionStrategy
```

### Flujo 2: Emisión de Comprobante (Proceso Central)
```
GenFactures.fxml (View)
    ↓ evento @FXML
ControllerFacturas (Controller)
    ↓ llama
ComprobanteService.crear()          ← Regla de negocio (Unidad III)
    ↓ persiste
DAOComprobanteImpl.insertar()       ← SQL INSERT (Unidad IV)
    ↓ genera artefactos
genPDFService → Builder → PDF       ← Arquitectura de Información
XMLService → Adapter → XML          ← Arquitectura Tecnológica
```

### Flujo 3: Navegación con Permisos
```
ControllerMenu.navegar()
    ↓ selecciona estrategia
NavegacionStrategy strategy = new NavFacturacion()
    ↓ verifica
strategy.tienePermiso() → PermisosNavegacion.puedeFacturar()
    ↓ ejecuta
strategy.navegar() → App.setRoot("GenFactures")
```

---

## 📊 Aplicación de Pilares OOP (Unidad II)

### Herencia
```java
AbstractGeneradorPDF (Clase Base Abstracta)
    ├── GeneradorBoletaPDF  extends AbstractGeneradorPDF
    └── GeneradorFacturaPDF extends AbstractGeneradorPDF
```

### Polimorfismo
```java
NavegacionStrategy estrategia = new NavFacturacion(); // o NavClientes, NavProductos...
estrategia.navegar(); // comportamiento diferente según implementación concreta
```

### Encapsulamiento
```java
public class Cliente {
    private Long idCliente;    // atributos privados
    private String nombres;
    public String getNombres() { return nombres; }     // acceso controlado
    public void setNombres(String n) { if (n!=null) this.nombres = n; }
}
```

### Abstracción
```java
public abstract class DAOFactory {
    public abstract DAOCliente getClienteDAO();    // contrato abstracto
    // la implementación concreta es PostgresDAOFactory
}
```

---

## 🖼️ Interfaz de Usuario (JavaFX)

### Pantallas Principales

#### 1. Login
![Captura de pantalla - Login](images/login.png)

#### 2. Dashboard
![Captura de pantalla - Dashboard](images/dashboard.png)

#### 3. Gestión de Clientes
![Captura de pantalla - Clientes](images/clientes.png)

#### 4. Gestión de Productos
![Captura de pantalla - Productos](images/productos.png)

#### 5. Generación de Comprobantes
![Captura de pantalla - Facturación](images/comprobante.png)

#### 6. Historial de Transacciones
![Captura de pantalla - Historial](images/historial.png)

---

## 💻 Configuración y Ejecución

### Prerrequisitos
- **Java JDK 24** o superior
- **Maven 3.8.1+**
- **Conexión a internet** (acceso a NeonDB cloud)

### Ejecutar el Proyecto
```bash
cd demo
mvn javafx:run
```

---

## 📋 Estado del Proyecto

| Aspecto | Estado | Descripción |
|--------|--------|-------------|
| **Compilación** | ✅ Operacional | Java 24, Maven limpio |
| **Ejecución** | ✅ Operacional | JavaFX UI funcional |
| **Patrones GoF** | ✅ 6 implementados | Abstract Factory, Builder, Strategy, Adapter, Singleton, DAO |
| **Persistencia** | ✅ Conectado | NeonDB PostgreSQL activo |
| **Documentación IS** | ✅ Completa | README + 8 documentos en `docs/` |
| **Informe Académico** | ✅ Generado | `INFORME_ACADEMICO.md` |

---

## 📚 Documentación Académica

| Documento | Contenido | Unidad IS |
|-----------|-----------|-----------|
| [01_introduccion.md](docs/01_introduccion.md) | Presentación del proyecto y contexto IS | I |
| [02_justificacion.md](docs/02_justificacion.md) | Justificación académica, técnica y profesional | I-IV |
| [03_objetivos.md](docs/03_objetivos.md) | Objetivo general + 7 objetivos específicos | I-IV |
| [04_alcance.md](docs/04_alcance.md) | Funcionalidades incluidas y excluidas | I-III |
| [05_arquitectura.md](docs/05_arquitectura.md) | Arquitecturas de negocios, aplicación, información y tecnológica | III |
| [06_silabo_mapping.md](docs/06_silabo_mapping.md) | Tabla de mapeo módulo ↔ unidad ↔ concepto | I-IV |
| [07_patrones_diseno.md](docs/07_patrones_diseno.md) | 6 patrones GoF con código y análisis IS | II-III |
| [08_diseno_datos.md](docs/08_diseno_datos.md) | Diseño conceptual, lógico y físico de datos | IV |

---

## 🎓 Créditos Académicos

Desarrollado como caso de estudio de Ingeniería del Software:

- **Universidad**: Universidad Tecnológica del Perú (UTP)
- **Curso**: Ingeniería del Software
- **Período Académico**: 2025-2026

---

<div align="center">

```
╔══════════════════════════════════════════════════════════════════╗
║   SIFAC-SW — Sistema de Facturación Académica                   ║
║   Gestión del Ciclo de Vida del Software — Ingeniería del SW    ║
╚══════════════════════════════════════════════════════════════════╝
```

**Desarrollado con ❤️ en Java | Universidad Tecnológica del Perú**

</div>
