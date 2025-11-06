package pago;

import java.util.Date;

/**
 * Representa un pago parcial que compone un Pago a cuotas.
 */
public class Cuota {

    private int nro;
    private Date vencimiento;
    private float importe;
    private boolean pagada;

    public Cuota(int nro, Date vencimiento, float importe) {
        this.nro = nro;
        this.vencimiento = vencimiento;
        this.importe = importe;
        this.pagada = false; // Por defecto, no pagada
    }

    public void marcarComoPagada() {
        this.pagada = true;
        System.out.println("Cuota N°" + nro + " marcada como pagada.");
    }

    public boolean estaVencida(Date fechaActual) {
        return !pagada && fechaActual.after(vencimiento);
    }

    public int getNro() { return nro; }
    public boolean isPagada() { return pagada; }
}