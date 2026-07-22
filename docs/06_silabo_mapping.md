# SIFAC-SW — Mapeo con el Sílabo de Ingeniería del Software

## 6. Relación con el Sílabo

### 6.1 Tabla de Mapeo Completo: Módulo ↔ Unidad ↔ Concepto IS

| # | Módulo / Componente del Sistema | Paquete / Archivo | Unidad IS | Concepto Específico Aplicado |
|---|-------------------------------|-------------------|-----------|------------------------------|
| 1 | **Arranque de la aplicación** | `core/App.java` | Unidad I | Ciclo de Vida: fase de despliegue y puesta en marcha del sistema |
| 2 | **Pantalla de carga** | `ui/loading/LoadingStage.java` | Unidad I | Proceso de Ingeniería del Software: inicialización como fase controlada |
| 3 | **Ejecución asíncrona de tareas** | `ui/loading/TaskRunner.java` | Unidad I | Desarrollo de Software: gestión de procesos en segundo plano |
| 4 | **Entidades de dominio** | `model/Cliente.java`, `model/Producto.java`, etc. (17 clases) | Unidad II | Clases y Objetos: representación de entidades del mundo real como clases |
| 5 | **Encapsulamiento** | `model/*.java` (atributos `private` + getters/setters) | Unidad II | Paradigma OO: encapsulamiento como pilar fundamental |
| 6 | **Herencia** | `AbstractGeneradorPDF` → `GeneradorBoletaPDF`, `GeneradorFacturaPDF` | Unidad II | Herencia: reutilización de código entre clases hija |
| 7 | **Polimorfismo por interfaz** | `NavegacionStrategy` + 6 implementaciones | Unidad II | Polimorfismo: comportamiento intercambiable sin modificar código cliente |
| 8 | **Abstracción** | `DAOFactory` (clase abstracta), `GeneradorPDFBuilder` (interfaz) | Unidad II | Abstracción: ocultar detalles de implementación, exponer contratos |
| 9 | **Beneficios del enfoque OO** | Toda la arquitectura MVC + DAO | Unidad II | Beneficios OO: mantenibilidad, extensibilidad, reusabilidad |
| 10 | **Emisión de comprobantes** | `service/ComprobanteService.java` + `controller/ControllerFacturas.java` | Unidad III | Proceso Unificado: flujo del caso de uso principal del negocio |
| 11 | **Capa de Presentación** | `controller/*.java` + `resources/*.fxml` | Unidad III | Arquitectura de Aplicación: capa View+Controller del MVC |
| 12 | **Capa de Lógica de Negocio** | `service/*.java` (15 servicios) | Unidad III | Arquitectura de Negocios: reglas del negocio encapsuladas y aisladas |
| 13 | **Control de acceso por roles** | `service/PermisosNavegacion.java` | Unidad III | Arquitectura de Negocios: política de autorización empresarial |
| 14 | **Patrón Strategy — Navegación** | `patterns/strategy/NavegacionStrategy.java` + impls | Unidad III | Arquitectura de Aplicación: navegación polimórfica basada en permisos |
| 15 | **Patrón Adapter — XML** | `patterns/adapter/XMLGenerator.java`, `BoletaXMLGenerator`, `FacturaXMLGenerator` | Unidad III | Arquitectura Tecnológica: adaptación de generadores XML heterogéneos |
| 16 | **Patrón Adapter — Email** | `patterns/adapter/MailTrapEmailAdapter.java` | Unidad III | Arquitectura Tecnológica: adaptación de servicio externo SMTP |
| 17 | **Patrón Builder — PDF** | `patterns/builder/GeneradorPDFDirector.java` + builders | Unidad III | Arquitectura de Información: construcción progresiva de documentos |
| 18 | **Stack tecnológico real** | `pom.xml` (Java 24, JavaFX, PostgreSQL, iText, JavaMail) | Unidad III | Arquitectura Tecnológica: selección y justificación del stack |
| 19 | **Entidades como diseño conceptual** | `model/*.java` → diagrama E-R | Unidad IV | Diseño Conceptual: identificación de entidades, atributos y relaciones |
| 20 | **Interfaces DAO — diseño lógico** | `persistence/dao/*.java` (14 interfaces) | Unidad IV | Diseño Lógico: modelo relacional abstracto con operaciones CRUD |
| 21 | **Implementaciones SQL — diseño físico** | `persistence/impl/*.java` (14 implementaciones) | Unidad IV | Diseño Físico: consultas SQL sobre PostgreSQL real |
| 22 | **Singleton de conexión** | `persistence/ConexionBD.java` | Unidad IV | Datos Centralizados: instancia única que centraliza el acceso a datos |
| 23 | **Abstract Factory de DAOs** | `persistence/dao/DAOFactory.java` + `impl/PostgresDAOFactory.java` | Unidad IV | Diseño Lógico: fábrica que abstrae la creación de objetos de acceso a datos |
| 24 | **Artefactos PDF generados** | `resources/PDFs/*.pdf` | Unidad I + IV | Producto de Software: salida tangible del sistema; Diseño Físico: resultado del proceso |
| 25 | **Artefactos XML generados** | `resources/xml/*.xml` | Unidad I + IV | Ciclo de Vida: el producto final entregado al usuario del negocio |

---

### 6.2 Cobertura por Unidad

#### Unidad I — Conceptos de Ingeniería del Software

| Concepto IS (Unidad I) | Evidencia en SIFAC-SW |
|------------------------|----------------------|
| **Conceptos de IS** | El proyecto es un sistema de información real con requisitos, diseño, implementación y verificación. |
| **Proceso de IS** | Se siguió un proceso disciplinado: análisis de requisitos → diseño → codificación → prueba → documentación. |
| **Desarrollo de Software** | El código fuente Java es el artefacto central del proceso de desarrollo. Maven gestiona el ciclo de build. |
| **Ciclo de Vida del Software** | Fases: Concepción (definir el sistema de facturación) → Elaboración (arquitectura DAO+MVC) → Construcción (codificación de 80+ clases) → Transición (sistema ejecutable con BD productiva). |

---

#### Unidad II — Paradigma Orientado a Objetos

| Concepto OO (Unidad II) | Evidencia en SIFAC-SW |
|--------------------------|----------------------|
| **Objetos** | Instancias de `Cliente`, `Producto`, `Comprobante` manipuladas en tiempo de ejecución. |
| **Clases** | 17 clases de dominio, 10 controllers, 15 services, 14 DAO interfaces, 14 DAO implementations. |
| **Paradigma OO** | Todo el sistema está construido sobre clases e interfaces, sin funciones sueltas. |
| **Beneficios del enfoque OO** | Extensibilidad: agregar un nuevo tipo de comprobante solo requiere una nueva clase Builder. Mantenibilidad: cambiar PostgreSQL por MySQL solo requiere una nueva `DAOFactory`. Reusabilidad: `AbstractGeneradorPDF` evita duplicación de código entre boleta y factura. |

---

#### Unidad III — Proceso Unificado y Arquitecturas

| Concepto PU (Unidad III) | Evidencia en SIFAC-SW |
|--------------------------|----------------------|
| **Proceso Unificado** | La arquitectura resultante refleja las decisiones del PU: casos de uso (módulos), arquitectura en capas, iteraciones de construcción. |
| **Arquitectura de Negocios** | `service/` encapsula las reglas de negocio: cálculo de IGV, numeración de comprobantes, autorización por rol. |
| **Arquitectura de Aplicación** | Patrón MVC: FXML (View) + Controllers + Services. Separación clara View-Controller-Service-DAO. |
| **Arquitectura de Información** | Flujo: datos de entrada → transformación en servicios → artefactos de salida (PDF, XML, email). |
| **Arquitectura Tecnológica** | Stack definido: Java 24 + JavaFX 24 + PostgreSQL 15/NeonDB + Maven + iText + JavaMail. |

---

#### Unidad IV — Diseño de Datos

| Concepto Diseño (Unidad IV) | Evidencia en SIFAC-SW |
|-----------------------------|----------------------|
| **Diseño Conceptual** | Las clases en `model/` representan el modelo conceptual: entidades con identidad, atributos y relaciones (1:N Comprobante-Detalle, N:1 Cliente-Comprobante). |
| **Diseño Lógico** | Las interfaces en `persistence/dao/` definen las operaciones CRUD abstractas sobre cada entidad, correspondiendo a las relaciones del esquema lógico. |
| **Diseño Físico** | Las implementaciones en `persistence/impl/` contienen las sentencias SQL reales ejecutadas sobre PostgreSQL 15 en NeonDB. |
| **Datos Centralizados** | `ConexionBD` (Singleton) garantiza que toda la aplicación use una única conexión centralizada a la base de datos. |

---

### 6.3 Diagrama de Cobertura

```
SÍLABO DE INGENIERÍA DEL SOFTWARE
══════════════════════════════════════════════════════════

UNIDAD I                    EVIDENCIA EN SIFAC-SW
─────────────────           ──────────────────────────────
Conceptos IS          ──►   Sistema real, no prototipo
Proceso IS            ──►   Análisis→Diseño→Código→Doc
Desarrollo Software   ──►   80+ clases Java, Maven, BD
Ciclo de Vida         ──►   App.java (arranque) + fases PU

UNIDAD II                   EVIDENCIA EN SIFAC-SW
─────────────────           ──────────────────────────────
Objetos               ──►   Instancias Cliente, Comprobante
Clases                ──►   17 model + 10 ctrl + 15 svc + 28 DAO
Paradigma OO          ──►   Sin código procedural, todo OO
Beneficios OO         ──►   Extensible, mantenible, reusable

UNIDAD III                  EVIDENCIA EN SIFAC-SW
─────────────────           ──────────────────────────────
Proceso Unificado     ──►   PU guía la arquitectura
Arq. Negocios         ──►   service/ (reglas negocio)
Arq. Aplicación       ──►   MVC: fxml + controller + service
Arq. Información      ──►   PDF + XML + email como outputs
Arq. Tecnológica      ──►   Java+JavaFX+PostgreSQL+Maven

UNIDAD IV                   EVIDENCIA EN SIFAC-SW
─────────────────           ──────────────────────────────
Diseño Conceptual     ──►   model/ → E-R conceptual
Diseño Lógico         ──►   persistence/dao/ → esquema lógico
Diseño Físico         ──►   persistence/impl/ → SQL en PG
Datos Centralizados   ──►   ConexionBD Singleton
```

---

*Documento: `docs/06_silabo_mapping.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
