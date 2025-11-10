package modelos.cursos; // Cambié el paquete a 'modelos.calificacion' por convención

import modelos.usuario.Alumno;


import java.util.Date;

public class Calificacion {

    private int idCalificacion; // Renombro a idCalificacion para claridad
    private float nota;
    private String comentario;
    private Date fechaRegistro;

    // Relaciones clave
    private Alumno alumno;
    private Curso curso;
    private Evaluacion evaluacion;
    
    public Calificacion(Alumno alumno, Curso curso, Evaluacion evaluacion, float nota, String comentario) {

        this.alumno = alumno;
        this.curso = curso;
        this.evaluacion = evaluacion;
        this.nota = nota;
        this.comentario = comentario;
        this.fechaRegistro = new Date(); // Asignamos la fecha actual al crearse
    }

    public boolean estaAprobada(float notaMinima) {
        return this.nota >= notaMinima;
    }

    // Getters y Setters
    public int getIdCalificacion() { return idCalificacion; }
    public void setIdCalificacion(int idCalificacion) { this.idCalificacion = idCalificacion; } // 💡 CLAVE: Setter para sincronizar ID BDD
    
    // Renombramos Getters para mayor claridad (puedes mantener los tuyos si ya están en uso)
    public float getNota() { return nota; }
    public String getComentario() { return comentario; }
    public Date getFechaRegistro() { return fechaRegistro; }

    // Getters de las relaciones
    public Alumno getAlumno() { return alumno; }
    public Curso getCurso() { return curso; }
    public Evaluacion getEvaluacion() { return evaluacion; }
    
    @Override
    public String toString() {
        return "Calificación #" + idCalificacion + " | Alumno: " + alumno.getNombre() + " | Nota: " + nota;
    }
}