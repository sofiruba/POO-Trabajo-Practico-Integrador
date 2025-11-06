package core;

import java.time.LocalDate;
import java.util.Date; // Usaremos java.util.Date según tu diagrama original, aunque LocalDate es mejor

/**
 * Representa una prueba o tarea que debe ser calificada.
 */
public class Evaluacion {

    private int idEval;
    private String tipo; // Estereotipo: <<parcial|tp|final|quiz>>
    private Date fecha; // Usando Date
    private float puntajeMaximo;

    public static final String TIPO_FINAL = "final";
    public static final String TIPO_TP = "tp";
    public static final String TIPO_PARCIAL = "parcial";
    public static final String TIPO_QUIZ = "quiz";

    public Evaluacion(int idEval, String tipo, Date fecha, float puntajeMaximo) {
        this.idEval = idEval;
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