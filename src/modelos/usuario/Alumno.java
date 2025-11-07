package modelos.usuario;

import modelos.cursos.Curso;
import modelos.cursos.Evaluacion;
import modelos.cursos.Modulo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Alumno extends Usuario {
    private List<Curso> cursos = new ArrayList<>();
    private Date fechaInscripcion;

    public Alumno(String nombre, String email, String contrasenia, Date fechaInscripcion) {
        super( nombre, email, contrasenia);
        this.fechaInscripcion = fechaInscripcion;
    }

    public void inscribirse(Curso curso) {
        cursos.add(curso);
        System.out.println(nombre + " se inscribió al curso " + curso.getNombre());
    }

    public void rendirEvaluacion(Curso curso, Modulo modulo, Evaluacion evaluacion) {
       // System.out.println(nombre + " rindió la evaluación " + evaluacion.getNombre());
    }

    public Date getFechaInscripcion() { return fechaInscripcion; }
    public List<Curso> getCursos() { return cursos; }

}
