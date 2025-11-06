package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import user.Alumno;
import user.Docente;
import core.Curso;
import core.CursoOnline;
import core.CursoPresencial;
import inscripcion.Inscripcion;
import pago.Recibo;
import pago.servicio.PagoServicio;

public class CursosController {

    private List<Alumno> alumnos;
    private List<Docente> docentes;
    private List<Curso> cursos;
    private List<Inscripcion> inscripciones;

    // Dependencia: CursosController --> PagoServicio : usa para pagar >
    private PagoServicio pagoServicio;

    public CursosController(PagoServicio pagoServicio) {
        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
        this.cursos = new ArrayList<>();
        this.inscripciones = new ArrayList<>();
        this.pagoServicio = pagoServicio;
    }

    public Curso crearCurso(Docente docente, String nombre, String desc, int cupo, String modalidad) {
        int nuevoId = cursos.size() + 1;
        Curso nuevoCurso;

        if (modalidad.equalsIgnoreCase("ONLINE")) {
            nuevoCurso = new CursoOnline(nuevoId, nombre, desc, cupo, "URL_temp", "Plataforma_X");
        } else if (modalidad.equalsIgnoreCase("PRESENCIAL")) {
            nuevoCurso = new CursoPresencial(nuevoId, nombre, desc, cupo, "Aula 101", "Direccion_temp");
        } else {
            System.err.println("Modalidad de curso no válida.");
            return null;
        }

        cursos.add(nuevoCurso);
        System.out.println("Controladora: Curso " + nombre + " creado y registrado.");
        return nuevoCurso;
    }

    public void iniciarCurso(Curso curso, Docente docente) {
        //  validar primero
        System.out.println("Controladora: Iniciando curso: " + curso.getNombre());
        curso.iniciar();
    }


    public Inscripcion inscribirAlumno(Alumno alumno, Curso curso) {
        // Lógica de negocio: Comprobar cupo, estado del curso, si ya está inscrito, etc.

        int idInscripcion = inscripciones.size() + 1;
        Inscripcion nuevaInscripcion = new Inscripcion(idInscripcion, new Date(), alumno, curso);
        inscripciones.add(nuevaInscripcion);

        System.out.println("Controladora: Alumno " + alumno.getNombre() + " pre-inscrito en " + curso.getNombre());
        return nuevaInscripcion;
    }

    public Recibo inscribirYPagar(Alumno alumno, Curso curso, float monto, String tipoPago, int cuotas) {
        Inscripcion inscripcion = inscribirAlumno(alumno, curso);

        Recibo recibo = pagoServicio.pagar(inscripcion, monto, tipoPago, cuotas);

        if (recibo != null) {
            inscripcion.aceptar();
        }

        return recibo;
    }


    public List<Alumno> getAlumnos() {
        return alumnos;
    }


}