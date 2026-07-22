# SIFAC-SW — Alcance

## 4. Alcance del Sistema

### 4.1 Alcance Funcional (Lo que el sistema HACE)

#### Módulo 1 — Autenticación y Seguridad
- ✅ Inicio de sesión con validación de credenciales contra base de datos PostgreSQL
- ✅ Control de acceso basado en roles (RBAC): administrador, cajero, consultor
- ✅ Gestión de sesión activa (almacenamiento en `sesionActual.json`)
- ✅ Cierre de sesión controlado
- ✅ Navegación condicionada por permisos (patrón Strategy)

#### Módulo 2 — Gestión de Clientes
- ✅ Registro de nuevos clientes (persona natural y persona jurídica)
- ✅ Edición de datos de clientes existentes
- ✅ Eliminación lógica de clientes
- ✅ Búsqueda y filtrado por nombre, DNI/RUC, tipo de documento
- ✅ Integración con ubigeo peruano (departamento → provincia → distrito)
- ✅ Validación de datos en tiempo real

#### Módulo 3 — Gestión de Productos
- ✅ Registro y actualización de productos con categoría, precio y stock
- ✅ Gestión de categorías de productos
- ✅ Asignación de tipo de afectación tributaria (gravado, exonerado, inafecto)
- ✅ Búsqueda y filtrado de catálogo

#### Módulo 4 — Emisión de Comprobantes
- ✅ Selección de cliente y tipo de comprobante (Factura / Boleta)
- ✅ Agregado de productos al detalle del comprobante
- ✅ Cálculo automático de subtotales, IGV (18%) y total
- ✅ Selección de medio de pago
- ✅ Generación de PDF (patrón Builder — iText 5)
- ✅ Generación de XML-UBL (patrón Adapter — DOM API)
- ✅ Envío por correo electrónico (patrón Adapter — JavaMail)
- ✅ Numeración correlativa automática por serie

#### Módulo 5 — Historial de Transacciones
- ✅ Listado de comprobantes emitidos con filtro por rango de fechas
- ✅ Búsqueda por número de comprobante, cliente o tipo
- ✅ Visualización del detalle de comprobantes históricos
- ✅ Re-descarga de PDFs generados

#### Módulo 6 — Dashboard
- ✅ Vista de resumen de métricas de facturación
- ✅ Datos de la empresa (`empresa.json`)

---

### 4.2 Alcance Técnico-Académico

| Aspecto | Incluido | No Incluido |
|---------|----------|-------------|
| Lenguaje | Java 24 | Kotlin, Scala, etc. |
| UI | JavaFX 24 desktop | Web, móvil |
| Persistencia | PostgreSQL (NeonDB) | MySQL, Oracle, MongoDB |
| Documentos | PDF (iText) + XML-UBL | DOCX, HTML report |
| Comunicación | Email SMTP (MailTrap) | SMS, WhatsApp |
| Seguridad | RBAC básico + SSL BD | Oauth2, JWT, bcrypt |
| Pruebas | Verificación manual | JUnit automatizado |
| Despliegue | Ejecución local (Maven) | Docker, CI/CD, cloud deploy |

---

### 4.3 Fuera del Alcance Académico

Las siguientes funcionalidades están identificadas pero **excluidas** del alcance del ciclo académico actual:

1. **Integración SUNAT/SRI**: No se realizan envíos reales de comprobantes a la autoridad tributaria (los XML generados son estructuralmente compatibles con UBL 2.1).
2. **Módulo de contabilidad**: No incluye mayor, balance general ni estado de resultados.
3. **Versión web/móvil**: El sistema es exclusivamente de escritorio (JavaFX).
4. **Pruebas automatizadas**: No hay suite JUnit implementada (identificado como trabajo futuro).
5. **Encriptación de contraseñas**: Las contraseñas no usan bcrypt (mejora de seguridad pendiente).
6. **Multiempresa**: El sistema gestiona una única empresa por instancia.

---

### 4.4 Actores del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    ACTORES SIFAC-SW                      │
├───────────────┬─────────────────────────────────────────┤
│ Administrador │ Acceso total: clientes, productos,       │
│               │ facturación, historial, configuración    │
├───────────────┼─────────────────────────────────────────┤
│ Cajero        │ Facturación, consulta de clientes        │
│               │ y productos (sin CRUD de productos)      │
├───────────────┼─────────────────────────────────────────┤
│ Consultor     │ Solo historial y dashboard               │
│               │ (lectura sin modificación)               │
└───────────────┴─────────────────────────────────────────┘
```

---

*Documento: `docs/04_alcance.md` | Proyecto: SIFAC-SW | Curso: Ingeniería del Software | UTP*
