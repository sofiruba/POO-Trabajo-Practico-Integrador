package pago;

import java.util.*;

public class Pago {

    private String idPago;
    private Date fecha;
    private float total;
    private String tipo;
    private int cantidadCuotas;

    // Relación de Composición: Pago "1" -- "0..*" Cuota : desglosa >
    private List<Cuota> cuotas;

    // Relación de Dependencia/Agregación: Pago --> Recibo : genera >
    private Recibo recibo;

    public Pago(String idPago, Date fecha, float total, String tipo, int cantidadCuotas) {
        this.idPago = idPago;
        this.fecha = fecha;
        this.total = total;
        this.tipo = tipo;
        this.cantidadCuotas = cantidadCuotas;
        this.cuotas = new ArrayList<>();
    }

    public Recibo generarRecibo(String detalle) {
        // En un modelo de composición, el Pago es responsable de crear el Recibo.
        this.recibo = new Recibo("REC-" + idPago, detalle, this.total);
        System.out.println("Recibo " + recibo.getNro() + " generado para el Pago " + idPago);
        return this.recibo;
    }

    public void agregarCuota(Cuota cuota) {
        this.cuotas.add(cuota);
    }

    public String getIdPago() { return idPago; }
    public Recibo getRecibo() { return recibo; }

}