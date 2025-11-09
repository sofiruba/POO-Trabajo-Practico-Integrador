package modelos.usuario;
import modelos.cursos.Calificacion;
import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.*;

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

    public Calificacion calificar(Alumno alumno, Curso curso, Evaluacion eval, float nota, String comentario) {
        
        Calificacion nuevaCalificacion = new Calificacion(
            alumno, 
            curso, 
            eval, 
            nota, 
            comentario
        );
        
        System.out.println("Docente " + nombre + " creó la Calificación para " + alumno.getNombre());
        return nuevaCalificacion;
    }

    public String getEspecialidad() { return especialidad; }

    public void setId(int id){
        this.id = id;
    }
}
