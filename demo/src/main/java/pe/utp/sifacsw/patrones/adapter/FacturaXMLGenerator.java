package pe.utp.sifacsw.patrones.adapter;

import pe.utp.sifacsw.dominio.modelos.Comprobante;

/**
 * Strategy para generar XML de Facturas
 * Implementa el patrón Strategy específico para facturas
 */
public class FacturaXMLGenerator implements XMLGenerator {

    @Override
    public String generar(Comprobante comprobante) {
        System.out
                .println("[GRASP: High Cohesion] FacturaXMLGenerator se enfoca únicamente en generar XML de facturas");

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<Factura version=\"1.0\" xmlns=\"http://www.sunat.gob.pe/cpe/see/glm\">\n");
        xml.append("  <!-- FACTURA ELECTRÓNICA - Comprobante de pago por operación con RUC -->\n");
        xml.append("  <VersionSchema>2.1</VersionSchema>\n");
        xml.append("  <TipoComprobante>Factura</TipoComprobante>\n");

        // Información de empresa (completa para facturas)
        agregarEmpresa(xml);

        // Información del cliente
        agregarCliente(xml, comprobante);

        // Referencia de la factura
        agregarReferencia(xml, comprobante);

        // Información del comprobante (campos obligatorios)
        agregarComprobanteInfo(xml, comprobante);

        // Totales y moneda
        agregarTotales(xml, comprobante);

        // Información adicional específica de factura
        agregarInformacionAdicional(xml);

        xml.append("</Factura>\n");

        return xml.toString();
    }

    private void agregarEmpresa(StringBuilder xml) {
        xml.append("  <Emisor>\n");
        xml.append("    <TipoDocumento>6</TipoDocumento>\n");
        xml.append("    <NumeroDocumento>EMPRESA_RUC</NumeroDocumento>\n");
        xml.append("    <RazonSocial>EMPRESA_NOMBRE</RazonSocial>\n");
        xml.append("    <NombreComercial>EMPRESA_NOMBRE</NombreComercial>\n");
        xml.append("    <Ubigeo>150131</Ubigeo>\n");
        xml.append("    <Direccion>EMPRESA_DIRECCION</Direccion>\n");
        xml.append("    <EstadoOrigen>1</EstadoOrigen>\n");
        xml.append("    <CodigoEstablecimiento>0000</CodigoEstablecimiento>\n");
        xml.append("  </Emisor>\n");
    }

    private void agregarCliente(StringBuilder xml, Comprobante comprobante) {
        xml.append("  <Receptor>\n");
        xml.append("    <TipoDocumento>");
        // TipoDocumento: 6=RUC, 1=DNI, etc.
        xml.append("6");
        xml.append("</TipoDocumento>\n");

        if (comprobante.getClienteDocumento() != null) {
            xml.append("    <NumeroDocumento>").append(comprobante.getClienteDocumento())
                    .append("</NumeroDocumento>\n");
        }
        if (comprobante.getNombreCliente() != null) {
            xml.append("    <RazonSocial>").append(escaparXML(comprobante.getNombreCliente()))
                    .append("</RazonSocial>\n");
        }
        if (comprobante.getDireccionEnvio() != null) {
            xml.append("    <Direccion>").append(escaparXML(comprobante.getDireccionEnvio())).append("</Direccion>\n");
        }
        xml.append("    <Ubigeo>150131</Ubigeo>\n");
        xml.append("  </Receptor>\n");
    }

    private void agregarReferencia(StringBuilder xml, Comprobante comprobante) {
        xml.append("  <Referencia>\n");
        if (comprobante.getSerie() != null) {
            xml.append("    <Serie>").append(comprobante.getSerie()).append("</Serie>\n");
        }
        if (comprobante.getIdComprobante() != null) {
            xml.append("    <Numero>").append(comprobante.getIdComprobante()).append("</Numero>\n");
        }
        xml.append("  </Referencia>\n");
    }

    private void agregarComprobanteInfo(StringBuilder xml, Comprobante comprobante) {
        xml.append("  <Comprobante>\n");
        xml.append("    <FechaEmision>")
                .append(comprobante.getFechaEmision() != null ? comprobante.getFechaEmision() : "")
                .append("</FechaEmision>\n");
        xml.append("    <HoraEmision>12:00:00</HoraEmision>\n");
        xml.append("    <Moneda>PEN</Moneda>\n");
        if (comprobante.getMedioPago() != null) {
            xml.append("    <FormaPago>").append(escaparXML(comprobante.getMedioPago())).append("</FormaPago>\n");
        }
        xml.append("    <TipoOperacion>0101</TipoOperacion>\n");
        xml.append("    <IndicadorTotales>1</IndicadorTotales>\n");
        xml.append("  </Comprobante>\n");
    }

    private void agregarTotales(StringBuilder xml, Comprobante comprobante) {
        xml.append("  <Resumen>\n");
        xml.append("    <Subtotal>\n");
        if (comprobante.getDevengado() != null) {
            xml.append("      <Importe>").append(comprobante.getDevengado()).append("</Importe>\n");
        }
        xml.append("    </Subtotal>\n");
        xml.append("    <Tributos>\n");
        xml.append("      <IGV>");
        if (comprobante.getTotalFinal() != null && comprobante.getDevengado() != null) {
            xml.append(comprobante.getTotalFinal().subtract(comprobante.getDevengado()));
        }
        xml.append("</IGV>\n");
        xml.append("    </Tributos>\n");
        xml.append("    <MontoTotal>\n");
        if (comprobante.getTotalFinal() != null) {
            xml.append("      <Importe>").append(comprobante.getTotalFinal()).append("</Importe>\n");
        }
        xml.append("      <Moneda>PEN</Moneda>\n");
        xml.append("    </MontoTotal>\n");
        xml.append("  </Resumen>\n");
    }

    private void agregarInformacionAdicional(StringBuilder xml) {
        xml.append("  <Adicional>\n");
        xml.append("    <InformacionLegal>\n");
        xml.append("      <Descripcion>Comprobante obligatorio de acuerdo al RUC del emisor</Descripcion>\n");
        xml.append("    </InformacionLegal>\n");
        xml.append("    <Firma>\n");
        xml.append("      <Estado>No Validado</Estado>\n");
        xml.append("    </Firma>\n");
        xml.append("  </Adicional>\n");
    }

    /**
     * Escapa caracteres especiales en XML
     */
    private String escaparXML(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}

