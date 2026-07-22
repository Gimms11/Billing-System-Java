package pe.utp.sifacsw.patrones.builder;

import pe.utp.sifacsw.dominio.modelos.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.util.List;
import java.math.BigDecimal;

public class GeneradorBoletaPDF extends AbstractGeneradorPDF {

    @Override
    public void agregarEncabezado(Empresa empresa) throws Exception {

        // === Encabezado moderno (similar al de la factura) ===
        PdfPTable tabla = crearTabla(new float[] { 3f, 1.2f });
        tabla.setWidthPercentage(100);

        // Izquierda: datos de la empresa
        PdfPCell izquierda = new PdfPCell();
        izquierda.setBorder(PdfPCell.NO_BORDER);
        izquierda.addElement(new Paragraph(empresa.getNombre(),
                new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(180, 0, 0))));
        izquierda.addElement(new Paragraph("RUC: " + empresa.getRuc(), fontNormal));
        if (empresa.getDireccion() != null)
            izquierda.addElement(new Paragraph(empresa.getDireccion(), fontNormal));
        if (empresa.getCorreo() != null)
            izquierda.addElement(new Paragraph(empresa.getCorreo(), fontNormal));
        izquierda.setPaddingBottom(5);
        tabla.addCell(izquierda);

        // Derecha: rectángulo rojo con texto "BOLETA"
        PdfPCell derecha = new PdfPCell();
        derecha.setBorder(PdfPCell.NO_BORDER);
        derecha.setBackgroundColor(new BaseColor(139, 0, 0)); // rojo oscuro
        Paragraph titulo = new Paragraph("BOLETA",
                new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, BaseColor.WHITE));
        titulo.setAlignment(Element.ALIGN_CENTER);
        derecha.addElement(titulo);
        derecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
        derecha.setPaddingTop(5);
        derecha.setPaddingBottom(5);
        derecha.setPaddingLeft(10);
        derecha.setPaddingRight(10);
        tabla.addCell(derecha);

        document.add(tabla);
        document.add(Chunk.NEWLINE);
    }

    @Override
    public void agregarDatosCliente(Cliente cliente, Comprobante comprobante) throws Exception {
        // === Sección de datos cliente y comprobante ===
        PdfPTable tbl = crearTabla(new float[] { 1f, 1f });
        tbl.setWidthPercentage(100);
        tbl.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        // Cliente
        PdfPCell left = new PdfPCell();
        left.setBorder(PdfPCell.NO_BORDER);
        left.setPadding(8);
        left.addElement(new Paragraph("Cliente:", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK)));
        left.addElement(new Paragraph(cliente.getNombres() + " " +
                (cliente.getApellidos() != null ? cliente.getApellidos() : ""), fontNormal));
        left.addElement(new Paragraph("Documento: " + cliente.getNumDocumento(), fontNormal));
        if (cliente.getDireccion() != null)
            left.addElement(new Paragraph("Dirección: " + cliente.getDireccion(), fontNormal));
        tbl.addCell(left);

        // Comprobante
        PdfPCell right = new PdfPCell();
        right.setBorder(PdfPCell.NO_BORDER);
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);
        right.setPadding(8);
        right.addElement(new Paragraph("N° de boleta: " + comprobante.getSerie(),
                new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(180, 0, 0))));
        right.addElement(new Paragraph("Fecha: " + comprobante.getFechaEmision().format(dateFmt), fontNormal));
        tbl.addCell(right);

        document.add(tbl);
        document.add(Chunk.NEWLINE);
    }

    @Override
    public void agregarCuerpo(Comprobante comprobante, List<DetalleComprobante> detalles) throws Exception {
        // === Tabla principal ===
        PdfPTable table = crearTabla(new float[] { 1f, 6f, 2f });
        table.setWidthPercentage(100);

        BaseColor rojoEncabezado = new BaseColor(180, 0, 0);
        BaseColor grisFondo = new BaseColor(245, 245, 245);

        // Encabezado
        String[] encabezados = { "CANT.", "DESCRIPCIÓN", "IMPORTE" };
        for (String enc : encabezados) {
            PdfPCell c = new PdfPCell(new Paragraph(enc,
                    new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE)));
            c.setBackgroundColor(rojoEncabezado);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setPadding(6);
            table.addCell(c);
        }

        // Detalles
        boolean fondoAlterno = false;
        for (DetalleComprobante d : detalles) {
            BaseColor fondo = fondoAlterno ? grisFondo : BaseColor.WHITE;
            fondoAlterno = !fondoAlterno;

            table.addCell(celdaConColor(String.valueOf(d.getCantidadProductos()), fontNormal, fondo));
            table.addCell(
                    celdaConColor(d.getNombreProducto() != null ? d.getNombreProducto() : "-", fontNormal, fondo));
            table.addCell(celdaConColor(currency.format(d.getTotal()), fontNormal, fondo));
        }

        document.add(table);

        // === Total ===
        BigDecimal total = comprobante.getTotalFinal() != null ? comprobante.getTotalFinal() : BigDecimal.ZERO;

        PdfPTable resumen = crearTabla(new float[] { 6f, 2f });
        resumen.setWidthPercentage(100);
        resumen.setSpacingBefore(10f);

        resumen.addCell(celda("", fontNormal));
        PdfPCell totalCell = celdaConBorde("TOTAL: " + currency.format(total),
                new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, new BaseColor(180, 0, 0)));
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        resumen.addCell(totalCell);

        document.add(resumen);
    }

    @Override
    public void agregarPie(Comprobante comprobante) throws Exception {
        // === Espacio antes del pie ===
        Paragraph espacio = new Paragraph(" ");
        espacio.setSpacingBefore(60f);
        document.add(espacio);

        // === Línea + mensaje ===
        PdfPTable pie = new PdfPTable(1);
        pie.setWidthPercentage(100);

        PdfPCell celdaPie = new PdfPCell();
        celdaPie.setBorder(PdfPCell.TOP);
        celdaPie.setBorderColor(new BaseColor(180, 180, 180));
        celdaPie.setBorderWidthTop(1f);
        celdaPie.setPaddingTop(12f);
        celdaPie.setPaddingBottom(20f);

        Paragraph mensaje = new Paragraph("Gracias por su compra.",
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY));
        mensaje.setAlignment(Element.ALIGN_LEFT);
        celdaPie.addElement(mensaje);

        pie.addCell(celdaPie);
        document.add(pie);

        document.add(Chunk.NEWLINE);
    }

    // === Utilidad ===
    private PdfPCell celdaConColor(String texto, Font fuente, BaseColor fondo) {
        PdfPCell cell = new PdfPCell(new Paragraph(texto, fuente));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(fondo);
        cell.setPadding(5);
        return cell;
    }
}

