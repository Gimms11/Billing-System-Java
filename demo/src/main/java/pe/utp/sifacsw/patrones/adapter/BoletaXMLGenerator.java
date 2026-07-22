package pe.utp.sifacsw.patrones.adapter;

import pe.utp.sifacsw.dominio.modelos.Comprobante;

/**
 * Strategy para generar XML de Boletas
 * Implementa el patrón Strategy específico para boletas
 */
public class BoletaXMLGenerator implements XMLGenerator {

    @Override
    public String generar(Comprobante comprobante) {

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<Boleta version=\"1.0\" xmlns=\"http://www.empresa.local/boleta\">\n");
        xml.append("  <!-- BOLETA - Comprobante de pago simplificado (operaciones sin RUC) -->\n");
        xml.append("  <VersionSchema>1.0</VersionSchema>\n");
        xml.append("  <TipoComprobante>Boleta</TipoComprobante>\n");

        // Información de empresa (simplificada para boletas)
        agregarEmpresa(xml);

        // Información del cliente (simplificada)
        agregarCliente(xml, comprobante);

        // Serie y número
        agregarReferencia(xml, comprobante);

        // Información del comprobante (campos básicos)
        agregarComprobanteInfo(xml, comprobante);

        // Totales (forma simplificada)
        agregarTotales(xml, comprobante);

        xml.append("</Boleta>\n");

        return xml.toString();
    }

    private void agregarEmpresa(StringBuilder xml) {
        xml.append("  <Empresa>\n");
        xml.append("    <RUC>EMPRESA_RUC</RUC>\n");
        xml.append("    <Nombre>EMPRESA_NOMBRE</Nombre>\n");
        xml.append("  </Empresa>\n");
    }

    private void agregarCliente(StringBuilder xml, Comprobante comprobante) {
        xml.append("  <Cliente>\n");
        xml.append("    <Tipo>Natural</Tipo>\n");

        if (comprobante.getClienteDocumento() != null) {
            xml.append("    <Documento>").append(comprobante.getClienteDocumento()).append("</Documento>\n");
        }
        if (comprobante.getNombreCliente() != null) {
            xml.append("    <Nombre>").append(escaparXML(comprobante.getNombreCliente())).append("</Nombre>\n");
        }
        xml.append("  </Cliente>\n");
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
        xml.append("  <Datos>\n");
        xml.append("    <Fecha>").append(comprobante.getFechaEmision() != null ? comprobante.getFechaEmision() : "")
                .append("</Fecha>\n");
        if (comprobante.getMedioPago() != null) {
            xml.append("    <MedioPago>").append(escaparXML(comprobante.getMedioPago())).append("</MedioPago>\n");
        }
        xml.append("  </Datos>\n");
    }

    private void agregarTotales(StringBuilder xml, Comprobante comprobante) {
        xml.append("  <Total>\n");
        if (comprobante.getTotalFinal() != null) {
            xml.append("    <Monto>").append(comprobante.getTotalFinal()).append("</Monto>\n");
        }
        xml.append("    <Moneda>PEN</Moneda>\n");
        xml.append("  </Total>\n");
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

