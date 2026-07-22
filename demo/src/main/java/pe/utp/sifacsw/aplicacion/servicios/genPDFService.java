package pe.utp.sifacsw.aplicacion.servicios;

import java.util.List;

import pe.utp.sifacsw.infraestructura.persistencia.DAODetalleComprobanteImpl;
import pe.utp.sifacsw.dominio.modelos.Cliente;
import pe.utp.sifacsw.dominio.modelos.Comprobante;
import pe.utp.sifacsw.dominio.modelos.DetalleComprobante;
import pe.utp.sifacsw.dominio.modelos.Empresa;
import pe.utp.sifacsw.patrones.builder.GeneradorPDFBuilder;
import pe.utp.sifacsw.patrones.builder.GeneradorBoletaPDF;
import pe.utp.sifacsw.patrones.builder.GeneradorFacturaPDF;
import pe.utp.sifacsw.patrones.builder.GeneradorPDFDirector;
import java.awt.Desktop;
import java.io.File;

public class genPDFService {
    
    private ManejadorService lectorServicio = new ManejadorService();
    private Empresa emp = lectorServicio.leerEmpresa();

    public void generarPDF(Comprobante comp) {
        try {
            ClienteService clienteService = new ClienteService();
            DAODetalleComprobanteImpl dao = new DAODetalleComprobanteImpl();
            List<DetalleComprobante> detalles = dao.listarDetallesPorComprobante(comp.getIdComprobante());
            Cliente cli = clienteService.obtenerPorId(comp.getIdCliente());

            GeneradorPDFDirector director = new GeneradorPDFDirector();

            String rutaArchivo = "";

            if(comp.getIdTipoComprobante() == 1){
                // FACTURA
                GeneradorPDFBuilder facturaBuilder = new GeneradorFacturaPDF();
                director.setBuilder(facturaBuilder);
                String rutaFactura = "demo\\src\\main\\resources\\com\\example\\PDFs\\factura_F001-000123.pdf";
                rutaArchivo = rutaFactura;
                director.generarComprobantePDF(emp, cli, comp, detalles, rutaFactura);
            } else {
                // BOLETA
                GeneradorPDFBuilder boletaBuilder = new GeneradorBoletaPDF();
                director.setBuilder(boletaBuilder);
                String rutaBoleta = "demo\\src\\main\\resources\\com\\example\\PDFs\\boleta_B001-000123.pdf";
                rutaArchivo = rutaBoleta;
                director.generarComprobantePDF(emp, cli, comp, detalles, rutaBoleta);
            }


                // ✅ Abre el PDF en el navegador por defecto
            File archivo = new File(rutaArchivo);
            if (archivo.exists()) {
                Desktop.getDesktop().browse(archivo.toURI());
                // o: Desktop.getDesktop().open(archivo); // abre con lector PDF predeterminado
            } else {
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

