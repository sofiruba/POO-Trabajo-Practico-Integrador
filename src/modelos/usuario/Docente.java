package modelos.usuario;
import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.Evaluacion;

import java.util.ArrayList;
import java.util.List;


public class Docente extends Usuario {
    private String especialidad;
    private List<Curso> cursos = new ArrayList<>();

    public Docente( String nombre, String email, String contrasenia, String especialidad) {
        super( nombre, email, contrasenia);
        this.especialidad = especialidad;
    }

    public Curso crearCurso(String nombre, String desc, int cupo, String linkPlat, String plataforma) {
        Curso curso = new CursoOnline(nombre, desc, cupo, linkPlat, plataforma);
        cursos.add(curso);
        return curso;
    }

    public void calificar(Alumno alumno, Evaluacion eval, float nota, String comentario) {

        System.out.println("Docente " + nombre + " calificó a " + alumno.getNombre() +
                " con nota " + nota);
    }

    public String getEspecialidad() { return especialidad; }

    public void setId(int id){
        this.id = id;
    }
}
