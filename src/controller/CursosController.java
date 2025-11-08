package controller;

import java.util.*;
import data.GestorBDDCurso;
import data.GestorBDDInscripcion;
import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.CursoPresencial;
import modelos.cursos.Modulo;
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
    private final GestorBDDInscripcion gestorInscripciones;
    private UsuariosController usuariosController;

    public CursosController(PagoServicio pagoServicio, UsuariosController usuariosController) {
        this.pagoServicio = pagoServicio;
        this.usuariosController = usuariosController;
        this.gestorCurso = new GestorBDDCurso();
        this.gestorInscripciones = new GestorBDDInscripcion();

        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
        this.inscripciones = new ArrayList<>();

        // Cargar cursos desde la base de datos al iniciar
        this.cursos = new ArrayList<>(gestorCurso.buscarTodos());

        System.out.println("✅ Controladora inicializada. Se cargaron " + cursos.size() + " cursos desde la base.");
    }

    // --- CREAR CURSO ---
    public Curso crearCurso(Docente docente, String nombre, String desc, int cupo, String modalidad) {
        Curso nuevoCurso = null;

        switch (modalidad.toUpperCase()) {
            case "ONLINE" -> {
                nuevoCurso = new CursoOnline(nombre, desc, cupo, "https://plataforma-temp.com", "Zoom");
                gestorCurso.guardar(nuevoCurso, docente);
            }
            case "PRESENCIAL" -> {
                nuevoCurso = new CursoPresencial(nombre, desc, cupo, "Aula 101", "Av. Siempre Viva 123");
                gestorCurso.guardar(nuevoCurso, docente);
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

    // --- INICIAR CURSO ---
    public void iniciarCurso(Curso curso, Docente docente) {
        if (!docentes.contains(docente)) {
            System.out.println("⚠️ El docente no pertenece a la plataforma.");
            return;
        }

        System.out.println("🎓 Iniciando curso: " + curso.getNombre() + " dictado por " + docente.getNombre());
        curso.iniciar();
    }

    // --- INSCRIPCIONES ---
    public Inscripcion inscribirAlumno(Alumno alumno, Curso curso) throws CupoCompletoException {
        Inscripcion nuevaInscripcion = new Inscripcion(alumno, curso);
        inscripciones.add(nuevaInscripcion);

        // 🔹 Guardar en BDD
        gestorInscripciones.guardar(nuevaInscripcion);

        System.out.println("📝 Alumno " + alumno.getNombre() + " preinscripto en " + curso.getNombre());
        return nuevaInscripcion;
    }
    public Alumno crearAlumnoEnPlataforma(String nombre, String email, String contrasenia) {
        // Delegamos la creación al UsuariosController
        Alumno nuevoAlumno = new Alumno(nombre, email, contrasenia, new Date());
        usuariosController.registrarAlumno(nuevoAlumno);

        // Lo agregamos a la lista local si queremos tenerlo sincronizado
        if (nuevoAlumno != null) {
            alumnos.add(nuevoAlumno);
            System.out.println("✅ Alumno agregado a la plataforma desde CursosController: " + nombre);
        }

        return nuevoAlumno;
    }

    public Recibo inscribirYPagar(Alumno alumno, Curso curso, float monto, String tipoPago, int cuotas)
            throws CupoCompletoException {

        Inscripcion inscripcion = inscribirAlumno(alumno, curso);
        Recibo recibo = pagoServicio.pagar(inscripcion, monto, tipoPago, cuotas);

        if (recibo != null) {
            inscripcion.aceptar();
            // 🔹 Actualizar en la BDD
            gestorInscripciones.actualizarEstado(
                    alumno.getId(),
                    curso.getIdCurso(), // 👈 corregido
                    Inscripcion.ESTADO_ACEPTADA

            );
            float montoNew = Float.parseFloat(recibo.getMonto().replace(",", ".")); // si viene con coma
            System.out.printf("💳 Pago realizado por %s | Monto: %.2f\n", alumno.getNombre(), montoNew);

        }

        return recibo;
    }

    // --- CONSULTAS ---
    public List<Curso> obtenerCursosOrdenadosPorNombre() {
        List<Curso> listaOrdenada = new ArrayList<>(cursos);
        Collections.sort(listaOrdenada);
        return listaOrdenada;
    }

    public Modulo buscarModuloPorIdEval(Curso curso, int idEval) {
        if (curso == null) return null;
        return curso.buscarModuloPorIdEval(idEval);
    }

    // --- GETTERS ---
    public List<Alumno> getAlumnos() { return alumnos; }
    public List<Docente> getDocentes() { return docentes; }
    public List<Curso> getCursos() { return cursos; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }
}
