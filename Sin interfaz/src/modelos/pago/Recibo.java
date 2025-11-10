package modelos.pago;

public class Recibo {

    private int nro;
    private String detalle;
    private float total;

    public Recibo(int nro, String detalle, float total) {
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

    public int getNro() { return nro; }

    public String getMonto() {
        return String.format("%.2f", total);
    }
}