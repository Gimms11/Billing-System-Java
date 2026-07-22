package pe.utp.sifacsw.patrones.builder;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import pe.utp.sifacsw.patrones.builder.GeneradorPDFBuilder;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class AbstractGeneradorPDF implements GeneradorPDFBuilder {
    protected Document document;
    protected PdfWriter writer;
    protected Font fontTitle = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    protected Font fontNormal = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    protected Font fontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    protected NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("es", "PE")); // ajusta locale si hace
                                                                                                // falta
    protected DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    protected void abrirDocumento(String rutaSalida) throws Exception {
        this.document = new Document(PageSize.A4, 36, 36, 54, 36);
        this.writer = PdfWriter.getInstance(document, new FileOutputStream(rutaSalida));
        document.open();
    }

    @Override
    public void iniciarDocumento(String rutaSalida) throws Exception {
        abrirDocumento(rutaSalida);
    }

    @Override
    public void finalizarDocumento() throws Exception {
        if (document != null && document.isOpen()) {
            document.close();
        }
        if (writer != null) {
            writer.close();
        }
    }

    protected PdfPTable crearTabla(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100f);
        return table;
    }

    protected PdfPCell celda(String text, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setBorder(PdfPCell.NO_BORDER);
        return c;
    }

    protected PdfPCell celdaConBorde(String text, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setPadding(6);
        return c;
    }
}

