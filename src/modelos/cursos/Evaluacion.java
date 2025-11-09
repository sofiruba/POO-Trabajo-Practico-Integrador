package modelos.cursos;

import java.util.Date;

public class Evaluacion {

    private int idEval;
    private String nombre;
    private String descripcion;
    private float nota_maxima;


    public static final String TIPO_FINAL = "final";
    public static final String TIPO_TP = "tp";
    public static final String TIPO_PARCIAL = "parcial";
    public static final String TIPO_QUIZ = "quiz";

    public Evaluacion(String nombre, String descripcion, float nota_maxima) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nota_maxima = nota_maxima;
    }



    public int getIdEval() { return idEval; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public float getNotaMaxima() { return nota_maxima; }

    public void setIdEval(int idEval) { this.idEval = idEval; }


    // ...
}