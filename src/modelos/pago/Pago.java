package modelos.pago;

import java.util.*;

public class Pago {

    private int idPago;
    private Date fecha;
    private float total;
    private String tipo;
    private int cantidadCuotas;

    // Relación de Composición: Pago "1" -- "0..*" Cuota : desglosa >
    private List<Cuota> cuotas;

    // Relación de Dependencia/Agregación: Pago --> Recibo : genera >
    private Recibo recibo;
    private int contador = 0;

    public Pago( Date fecha, float total, String tipo, int cantidadCuotas) {
        contador ++;
        this.idPago = contador;
        this.fecha = fecha;
        this.total = total;
        this.tipo = tipo;
        this.cantidadCuotas = cantidadCuotas;
        this.cuotas = new ArrayList<>();
    }

    public Recibo generarRecibo(String detalle) {
        // En un modelo de composición, el Pago es responsable de crear el Recibo.
        this.recibo = new Recibo( idPago, detalle, this.total);
        System.out.println("Recibo " + recibo.getNro() + " generado para el Pago " + idPago);
        return this.recibo;
    }

    public void agregarCuota(Cuota cuota) {
        this.cuotas.add(cuota);
    }

    public int getIdPago() { return idPago; }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidadCuotas() {
        return cantidadCuotas;
    }

    public void setCantidadCuotas(int cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public List<Cuota> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<Cuota> cuotas) {
        this.cuotas = cuotas;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public Recibo getRecibo() { return recibo; }

}