# SIFAC-SW — Justificación

## 2. Justificación

### 2.1 Justificación Académica

El desarrollo de SIFAC-SW responde directamente a las exigencias del curso de **Ingeniería del Software**, que requiere demostrar competencia práctica en los siguientes ejes:

| Eje del Curso | Cómo lo Demuestra SIFAC-SW |
|--------------|---------------------------|
| Ciclo de vida del software (Unidad I) | El sistema atravesó fases formales: concepción → elaboración → construcción → transición. Cada fase dejó artefactos verificables. |
| Paradigma Orientado a Objetos (Unidad II) | 17 clases de dominio con encapsulamiento, 2 jerarquías de herencia, 6+ interfaces polimórficas. |
| Proceso Unificado (Unidad III) | Arquitectura en capas alineada con las 4 arquitecturas del PU: negocios, aplicación, información, tecnológica. |
| Diseño Conceptual / Lógico / Físico (Unidad IV) | Modelo E-R → esquema relacional normalizado → tablas físicas en PostgreSQL NeonDB. |

### 2.2 Justificación Técnica

Desde el punto de vista de la ingeniería, el proyecto justifica sus decisiones tecnológicas así:

**¿Por qué Java 24?**
Java es el lenguaje de referencia para aplicaciones empresariales. Su tipado estático, soporte de interfaces y clases abstractas, y la JVM como plataforma lo hacen ideal para demostrar conceptos OO con rigor.

**¿Por qué JavaFX?**
JavaFX implementa el patrón MVC de forma nativa mediante la separación FXML (vista) / Controller (controlador) / Model (modelo). Esto permite enseñar arquitectura MVC de forma tangible y ejecutable.

**¿Por qué el patrón DAO + Abstract Factory?**
El desacoplamiento entre la lógica de negocio y el mecanismo de persistencia es uno de los principios fundamentales de la IS (separación de concerns). El DAO hace posible que el sistema sea independiente del SGBD.

**¿Por qué PostgreSQL en NeonDB?**
Demuestra que un sistema académico puede conectarse a infraestructura real de producción (cloud serverless, SSL), cerrando la brecha entre el aula y la industria.

**¿Por qué 6 Patrones GoF?**
Los patrones de diseño son el vocabulario compartido de los ingenieros de software. Implementarlos en un dominio real (no en ejemplos de juguete) es la forma más efectiva de internalizarlos.

### 2.3 Justificación Profesional

La facturación electrónica es un requerimiento legal en Perú (SUNAT) y en la mayoría de países de América Latina. Un sistema de este tipo tiene:

- **Demanda real**: toda empresa necesita emitir comprobantes.
- **Complejidad real**: reglas tributarias, formatos XML-UBL, numeración correlativa.
- **Impacto medible**: el sistema reduce tiempo de emisión y elimina errores manuales.

El dominio de facturación convierte a SIFAC-SW en un proyecto con valor de portafolio profesional, no solo un ejercicio académico.

### 2.4 Justificación de los Patrones de Diseño Aplicados

| Patrón | Problema Resuelto | Principio IS Aplicado |
|--------|-------------------|----------------------|
| **Abstract Factory** | Crear familias de DAOs sin acoplar al cliente al tipo de BD | Principio Abierto/Cerrado (OCP) |
| **Builder** | Construir documentos PDF complejos en pasos ordenados | Separación de concerns, SRP |
| **Strategy** | Navegación polimórfica con validación de permisos | Polimorfismo / DIP |
| **Adapter** | Adaptar generadores XML heterogéneos a interfaz común | Inversión de dependencias |
| **Singleton** | Garantizar una sola instancia de conexión a la BD | Gestión de recursos críticos |
| **DAO** | Aislar lógica de acceso a datos de la lógica de negocio | Separación de capas arquitectónicas |

---

*Documento: `docs/02_justificacion.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
