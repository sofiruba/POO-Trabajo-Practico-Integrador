package controller;

import java.util.*;
import data.GestorBDDCurso;
import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.CursoPresencial;
import modelos.inscripcion.Inscripcion;
import modelos.pago.PagoServicio;
import modelos.pago.Recibo;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import exception.CupoCompletoException;

public class CursosController {

    private final List<Alumno> alumnos;
    private final List<Docente> docentes;
    private final List<Curso> cursos;
    private final List<Inscripcion> inscripciones;

    private final PagoServicio pagoServicio;
    private final GestorBDDCurso gestorCurso;

    // Autoincremento local para inscripciones (si no lo maneja la clase Inscripcion)
    private static int CONTADOR_INSCRIPCIONES = 0;

    public CursosController(PagoServicio pagoServicio, GestorBDDCurso gestorCurso) {
        this.pagoServicio = pagoServicio;
        this.gestorCurso = gestorCurso;

        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
        this.inscripciones = new ArrayList<>();

        // Cargar cursos desde la base de datos al iniciar
        this.cursos = new ArrayList<>(gestorCurso.buscarTodos());

        System.out.println("✅ Controladora inicializada. Se cargaron " + cursos.size() + " cursos desde la base.");
    }

    /**
     * Crea un curso y lo persiste en la base de datos.
     */
    public Curso crearCurso(Docente docente, String nombre, String desc, int cupo, String modalidad) {
        Curso nuevoCurso = null;

        switch (modalidad.toUpperCase()) {
            case "ONLINE" -> {
                nuevoCurso = new CursoOnline(nombre, desc, cupo, "https://plataforma-temp.com", "Zoom");
                gestorCurso.guardar(nuevoCurso);
            }
            case "PRESENCIAL" -> {
                nuevoCurso = new CursoPresencial(nombre, desc, cupo, "Aula 101", "Av. Siempre Viva 123");
                gestorCurso.guardar(nuevoCurso);
            }
            default -> {
                System.err.println("⚠️ Modalidad inválida. Usa 'ONLINE' o 'PRESENCIAL'.");
                return null;
            }
        }

        cursos.add(nuevoCurso);
        System.out.println("🟢 Curso '" + nombre + "' creado y persistido correctamente.");
        return nuevoCurso;
    }

    /**
     * Inicia un curso (solo para docentes).
     */
    public void iniciarCurso(Curso curso, Docente docente) {
        if (!docentes.contains(docente)) {
            System.out.println("⚠️ El docente no pertenece a la plataforma.");
            return;
        }

        System.out.println("🎓 Iniciando curso: " + curso.getNombre() + " dictado por " + docente.getNombre());
        curso.iniciar();
    }

    /**
     * Inscribe a un alumno en un curso.
     */
    public Inscripcion inscribirAlumno(Alumno alumno, Curso curso) throws CupoCompletoException {
        // Validar cupos (comentado si aún no usás el enum EstadoInscripcion)
        /*
        long inscritos = this.inscripciones.stream()
                .filter(i -> i.getCurso().equals(curso)
                        && (i.getEstado().equals(EstadoInscripcion.PENDIENTE)
                        || i.getEstado().equals(EstadoInscripcion.ACEPTADA)))
                .count();

        if (curso.getCupo() <= inscritos) {
            throw new CupoCompletoException(curso.getNombre());
        }
        */

        Inscripcion inscripcion = new Inscripcion(new Date(), alumno, curso);
        inscripciones.add(inscripcion);

        System.out.println("📝 Alumno " + alumno.getNombre() + " preinscripto en " + curso.getNombre());
        return inscripcion;
    }

    /**
     * Inscribe al alumno y procesa el pago asociado.
     */
    public Recibo inscribirYPagar(Alumno alumno, Curso curso, float monto, String tipoPago, int cuotas)
            throws CupoCompletoException {

        Inscripcion inscripcion = inscribirAlumno(alumno, curso);

        Recibo recibo = pagoServicio.pagar(inscripcion, monto, tipoPago, cuotas);

        if (recibo != null) {
            inscripcion.aceptar();
            System.out.println("💳 Inscripción confirmada y pago procesado con éxito.");
        } else {
            System.out.println("⚠️ Fallo al procesar el pago. Inscripción pendiente.");
        }

        return recibo;
    }

    /**
     * Devuelve los cursos ordenados alfabéticamente.
     */
    public List<Curso> obtenerCursosOrdenadosPorNombre() {
        List<Curso> listaOrdenada = new ArrayList<>(cursos);
        Collections.sort(listaOrdenada);
        return listaOrdenada;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }
}
