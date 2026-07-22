package pe.utp.sifacsw.patrones.adapter;

/**
 * Factory Method Pattern - Crea instancias de XMLGenerator
 * según el tipo de comprobante
 */
public class XMLGeneratorFactory {

    /**
     * Obtiene el generador XML apropiado según el tipo de comprobante
     * 
     * @param idTipoComprobante ID del tipo de comprobante
     *                          1 = Factura
     *                          2 = Boleta
     * @return XMLGenerator apropiado para el tipo
     * @throws IllegalArgumentException si el tipo no es soportado
     */
    public static XMLGenerator get(long idTipoComprobante) {

        switch ((int) idTipoComprobante) {
            case 1:
                return new FacturaXMLGenerator();
            case 2:
                return new BoletaXMLGenerator();
            default:
                throw new IllegalArgumentException(
                        "Tipo de comprobante no soportado: " + idTipoComprobante);
        }
    }
}

