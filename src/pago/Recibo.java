package pago;

/**
 * Documento que certifica la realización de un Pago.
 */
public class Recibo {

    private String nro;
    private String detalle;
    private float total;

    public Recibo(String nro, String detalle, float total) {
        this.nro = nro;
        this.detalle = detalle;
        this.total = total;
    }

    public void imprimir() {
        System.out.println("--- RECIBO N° " + nro + " ---");
        System.out.println("Detalle: " + detalle);
        System.out.println("Total Pagado: $" + total);
        System.out.println("-------------------------");
    }

    public String getNro() { return nro; }

}