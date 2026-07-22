# SIFAC-SW — Introducción

## 1. Introducción

### 1.1 Presentación del Proyecto

**SIFAC-SW** (*Sistema de Facturación Académica para la Gestión del Ciclo de Vida del Software*) es una aplicación de escritorio desarrollada íntegramente en **Java 24** con interfaz gráfica **JavaFX** y persistencia en una base de datos **PostgreSQL** alojada en la nube (NeonDB). Su dominio funcional es la facturación electrónica para pequeñas y medianas empresas (PyME): gestión de clientes, productos, emisión de comprobantes (facturas y boletas), generación de documentos PDF y XML, y control de acceso por roles.

Sin embargo, más allá de su función operativa, SIFAC-SW ha sido concebido y documentado como un **caso de estudio integral de Ingeniería del Software**: cada módulo, cada patrón de diseño, cada decisión arquitectónica y cada capa del sistema materializa y demuestra los conceptos teóricos estudiados a lo largo de las cuatro unidades del curso.

### 1.2 Contexto Académico

El curso de **Ingeniería del Software** aborda el desarrollo de sistemas desde una perspectiva disciplinada y científica: no se trata únicamente de "escribir código", sino de aplicar un proceso sistemático, medible y controlable para producir software de calidad. SIFAC-SW encarna esa perspectiva:

- **No es un prototipo**: es un sistema funcional con arquitectura definida, capas separadas, patrones formales y persistencia real.
- **No es código suelto**: cada clase tiene una responsabilidad única (SRP), cada interfaz define un contrato (ISP), cada módulo es extensible sin modificación (OCP).
- **No es académico artificial**: el dominio de facturación es un negocio real, las tecnologías son estándares de la industria, y los patrones implementados son los del catálogo GoF (*Gang of Four*).

### 1.3 Por qué la Facturación como Dominio de Estudio

La facturación electrónica es un dominio idóneo para un caso de estudio de IS porque:

1. **Entidades bien definidas**: Clientes, Productos, Comprobantes, Detalles, Usuarios, Empresa — todas modelables como clases OO.
2. **Reglas de negocio complejas**: Cálculo de IGV, tipos de comprobante, series, numeración correlativa, tipos de afectación tributaria.
3. **Múltiples formatos de salida**: Documentos PDF (Builder), documentos XML-UBL (Adapter), correo electrónico (Adapter).
4. **Seguridad y roles**: Control de acceso basado en roles (RBAC), sesiones de usuario.
5. **Escalabilidad inherente**: La arquitectura DAO permite cambiar de PostgreSQL a cualquier otro SGBD sin tocar la lógica de negocio.

### 1.4 Alcance de este Documento

Este documento forma parte de la carpeta `docs/` del proyecto SIFAC-SW y corresponde a la **sección introductoria** del informe académico. Los documentos complementarios cubren Justificación, Objetivos, Alcance, Arquitectura, Mapeo con el Sílabo, Patrones de Diseño y Diseño de Datos.

---

*Documento: `docs/01_introduccion.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
