# SIFAC-SW — Objetivos

## 3. Objetivos

### 3.1 Objetivo General

Desarrollar e implementar **SIFAC-SW** (*Sistema de Facturación Académica para la Gestión del Ciclo de Vida del Software*), aplicando de forma integral los fundamentos teórico-prácticos de la **Ingeniería del Software**: ciclo de vida, paradigma orientado a objetos, proceso unificado, arquitecturas empresariales y diseño en sus niveles conceptual, lógico y físico; tomando como dominio de negocio la facturación electrónica de una PyME.

---

### 3.2 Objetivos Específicos

#### OE-01 — Ciclo de Vida del Software (Unidad I)
> Documentar y evidenciar el **ciclo de vida del software** del proyecto SIFAC-SW, identificando las fases de concepción, elaboración, construcción y transición del Proceso Unificado, y los artefactos producidos en cada fase.

**Indicador de logro**: Existencia de documentación de requisitos, diseño, código fuente funcional y artefactos de salida (PDFs, XMLs), correspondientes a cada fase del ciclo de vida.

---

#### OE-02 — Paradigma Orientado a Objetos (Unidad II)
> Implementar el **paradigma orientado a objetos** mediante la definición de clases, objetos, jerarquías de herencia, polimorfismo por interfaz, encapsulamiento de atributos y abstracción de comportamientos, aplicados al modelo de dominio de facturación.

**Indicador de logro**: Presencia verificable de:
- 17 clases de dominio con atributos privados y getters/setters (`model/`)
- 2 jerarquías de herencia (`AbstractGeneradorPDF` → `GeneradorBoletaPDF`, `GeneradorFacturaPDF`)
- 6+ interfaces polimórficas (`NavegacionStrategy`, `XMLGenerator`, `GeneradorPDFBuilder`, `DAOCliente`…)

---

#### OE-03 — Arquitectura de Capas / Proceso Unificado (Unidad III)
> Diseñar y aplicar una **arquitectura multicapa** (presentación, lógica de negocio, acceso a datos, datos) alineada con las cuatro vistas arquitectónicas del Proceso Unificado: arquitectura de negocios, de aplicación, de información y tecnológica.

**Indicador de logro**: Código organizado en paquetes Java (`controller/`, `service/`, `persistence/dao/`, `persistence/impl/`) que corresponden biunívocamente a las capas de la arquitectura documentada.

---

#### OE-04 — Patrones de Diseño GoF (Unidad III)
> Implementar un mínimo de **seis patrones de diseño** del catálogo GoF (*Gang of Four*) como evidencia de buenas prácticas en arquitectura de software, documentando el problema resuelto, la solución aplicada y el beneficio obtenido para cada patrón.

**Indicador de logro**: Abstract Factory, Builder, Strategy, Adapter, Singleton y DAO implementados en código funcional y referenciados en la documentación técnica.

---

#### OE-05 — Diseño de Datos en Tres Niveles (Unidad IV)
> Construir el modelo de datos del sistema en sus tres niveles de diseño: **conceptual** (diagrama E-R de entidades), **lógico** (esquema relacional normalizado con atributos y relaciones) y **físico** (tablas implementadas en PostgreSQL 15 sobre NeonDB cloud).

**Indicador de logro**: Tres representaciones del modelo de datos documentadas y consistentes entre sí, con la implementación física operativa y conectada al sistema.

---

#### OE-06 — Generación de Artefactos Digitales del Negocio (Unidades I, III, IV)
> Generar artefactos digitales tangibles del sistema —documentos PDF, archivos XML-UBL y correos electrónicos— como demostración de la operatividad completa del ciclo de vida del producto de software y del valor entregado al usuario final.

**Indicador de logro**: El sistema genera exitosamente archivos PDF (`PDFs/`) y XML (`xml/`) al emitir un comprobante, y envía correos mediante el Adapter de JavaMail.

---

#### OE-07 — Seguridad y Control de Acceso (Unidad III — Arquitectura de Negocios)
> Implementar un sistema de **control de acceso basado en roles** (RBAC) que garantice que cada usuario solo pueda acceder a los módulos autorizados según su perfil, aplicando el patrón Strategy para la validación de permisos en tiempo de ejecución.

**Indicador de logro**: Login funcional con validación en BD, sesión activa almacenada, y navegación condicionada por `PermisosNavegacion` y `NavegacionStrategy`.

---

### 3.3 Relación Objetivos ↔ Unidades del Sílabo

```
OE-01 ─── Unidad I   (Ciclo de Vida, Proceso de IS, Desarrollo de Software)
OE-02 ─── Unidad II  (Objetos, Clases, Paradigma OO, Beneficios OO)
OE-03 ─── Unidad III (PU, Arquitectura de Aplicación, Arquitectura de Negocios)
OE-04 ─── Unidad III (Arquitectura Tecnológica, Patrones en Arquitectura)
OE-05 ─── Unidad IV  (Diseño Conceptual, Lógico, Físico, Datos Centralizados)
OE-06 ─── Unidad I + IV (Producto de Software, Artefactos del Negocio)
OE-07 ─── Unidad III (Arquitectura de Negocios — Reglas de Autorización)
```

---

*Documento: `docs/03_objetivos.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
