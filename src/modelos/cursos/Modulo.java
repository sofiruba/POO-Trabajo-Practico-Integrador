package modelos.cursos;

import java.util.ArrayList;
import java.util.List;

public class Modulo {

    private int idModulo;
    private String titulo;
    private String contenido; // Tipo Text que especificaste, usando String

    private List<Evaluacion> evaluaciones; // Colección de Evaluaciones

    private int contador = 0;
    public Modulo(int idModulo, String titulo, String contenido) {
        contador++;
        this.idModulo = contador;
        this.titulo = titulo;
        this.contenido = contenido;
        this.evaluaciones = new ArrayList<>(); // Inicializamos la colección
    }

    public void agregarEvaluacion(Evaluacion evaluacion) {
        this.evaluaciones.add(evaluacion);
    }


    public int getIdModulo() { return idModulo; }
    public String getTitulo() { return titulo; }
    public String getContenido() { return contenido; }
    public List<Evaluacion> getEvaluaciones() { return evaluaciones; }


}