package modelos.cursos;

import java.util.Date;

public class Evaluacion {

    private int idEval;
    private String tipo; // Estereotipo: <<parcial|tp|final|quiz>>
    private Date fecha; // Usando Date
    private float puntajeMaximo;

    private int contador = 0;

    public static final String TIPO_FINAL = "final";
    public static final String TIPO_TP = "tp";
    public static final String TIPO_PARCIAL = "parcial";
    public static final String TIPO_QUIZ = "quiz";

    public Evaluacion(String tipo, Date fecha, float puntajeMaximo) {
        contador++;
        this.idEval = contador;
        this.tipo = tipo;
        this.fecha = fecha;
        this.puntajeMaximo = puntajeMaximo;
    }

    public void cambiarFecha(Date nuevaFecha) {
        this.fecha = nuevaFecha;
    }

    public int getIdEval() { return idEval; }
    public String getTipo() { return tipo; }
    public Date getFecha() { return fecha; }
    public float getPuntajeMaximo() { return puntajeMaximo; }



    // ...
}