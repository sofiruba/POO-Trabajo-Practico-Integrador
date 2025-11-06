package controller;

import java.util.*;
// Importaciones ajustadas y simplificadas (asumiendo que las clases están bien ubicadas)
import data.implementaciones.*; // Usamos el Gestor Genérico para Curso
import data.interfaces.*;
import inscripcion.*;
import pago.*;
import pago.servicio.PagoServicio; // Interfaz para el pago
import user.*; // Incluye Alumno y Docente
import modelos.*; // Incluye Curso, CursoOnline, CursoPresencial
import exception.CupoCompletoException;
import java.lang.reflect.Type;


public class CursosController {

    private final List<Alumno> alumnos; // Usar final
    private final List<Docente> docentes; // Usar final
    private final List<Curso> cursos; // Usar final
    private final List<Inscripcion> inscripciones; // Usar final

    // Dependencia: PagoServicio
    private final PagoServicio pagoServicio; // Usar final

    // Gestores de persistencia (usando el modelo de listas separadas para evitar problemas de herencia)
    private final GestorDeCursoSQL gestor;
    // Constantes para el ID autoincremental de Inscripción (Si no usas la clase Inscripcion para el static ID)
    private static int CONTADOR_INSCRIPCIONES = 0;

    public CursosController(PagoServicio pagoServicio, GestorDeCursoSQL gestor) {
        this.gestor = new GestorDeCursoSQL();

        // Inicializar listas en memoria
        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
        this.inscripciones = new ArrayList<>();
        this.pagoServicio = pagoServicio;

        // Cargar los cursos del archivo al inicio (Merge de ambas listas persistidas)
        this.cursos = new ArrayList<>();
        this.cursos.addAll(this.gestor.buscarTodos()); // cambiar
        // cambiar

        // NOTA: Idealmente, también cargarías alumnos, docentes e inscripciones aquí.
    }

    /**
     * Crea un nuevo curso y lo registra en la plataforma y en persistencia.
     */
    public Curso crearCurso(Docente docente, String nombre, String desc, int cupo, String modalidad) {

        // El ID debe ser autoincremental y generado dentro del constructor de Curso/subclase,
        // no contando el tamaño de la lista. Aquí se simula la llamada a un constructor sin ID.
        Curso nuevoCurso;

        if (modalidad.equalsIgnoreCase("ONLINE")) {
            // Asumo que CursoOnline genera su propio ID autoincremental
            nuevoCurso = new CursoOnline(nombre, desc, cupo, "URL_temp", "Plataforma_X");
            this.gestor.guardar((Curso) nuevoCurso); // Persistencia específica
        } else if (modalidad.equalsIgnoreCase("PRESENCIAL")) {
            // Asumo que CursoPresencial genera su propio ID autoincremental
            nuevoCurso = new CursoPresencial(nombre, desc, cupo, "Aula 101", "Direccion_temp");
            this.gestor.guardar((CursoPresencial) nuevoCurso); // Persistencia específica
        } else {
            System.err.println("Modalidad de curso no válida.");
            return null;
        }

        cursos.add(nuevoCurso);
        System.out.println("Controladora: Curso " + nombre + " creado, registrado y persistido.");
        return nuevoCurso;
    }

    public void iniciarCurso(Curso curso, Docente docente) {
        // Validación de reglas de negocio aquí (ej: estado del curso, docente asignado)
        System.out.println("Controladora: Iniciando curso: " + curso.getNombre());
        curso.iniciar();
    }


    public Inscripcion inscribirAlumno(Alumno alumno, Curso curso) throws CupoCompletoException {
        // Lógica de negocio: Comprobar cupo
        // Se cuenta el número de inscripciones ACEPTADAS/PENDIENTES para este curso
      /*  long inscritos = this.inscripciones.stream()
                .filter(i -> i.getCurso().equals(curso) &&
                       // (i.getEstado().equals(EstadoInscripcion.PENDIENTE) ||
                                i.getEstado().equals(EstadoInscripcion.ACEPTADA)))
                .count();

        if (curso.getCupo() <= inscritos) {
            // Lanzamos la excepción si la condición no se cumple
            throw new CupoCompletoException(curso.getNombre());
        }*/

        // El ID autoincremental debe ser manejado por la clase Inscripcion
        Inscripcion nuevaInscripcion = new Inscripcion(new Date(), alumno, curso);
        inscripciones.add(nuevaInscripcion);

        // NOTA: Aquí deberías persistir la nueva inscripción usando un gestor para Inscripcion.

        System.out.println("Controladora: Alumno " + alumno.getNombre() + " pre-inscrito en " + curso.getNombre());
        return nuevaInscripcion;
    }

    public Recibo inscribirYPagar(Alumno alumno, Curso curso, float monto, String tipoPago, int cuotas) throws CupoCompletoException {
        // 1. Pre-inscribir (lanza excepción si hay cupo completo)
        Inscripcion inscripcion = inscribirAlumno(alumno, curso);

        // 2. Procesar Pago
        Recibo recibo = pagoServicio.pagar(inscripcion, monto, tipoPago, cuotas);

        // 3. Confirmar Inscripción tras el éxito
        if (recibo != null) {
            inscripcion.aceptar();
            // NOTA: Aquí deberías actualizar el objeto Inscripcion en persistencia
            // gestorInscripciones.actualizar(inscripcion);
        }

        return recibo;
    }

    public List<Curso> obtenerCursosOrdenadosPorNombre() {
        // Se usa la implementación Comparable de Curso (ordenamiento natural)
        List<Curso> listaOrdenada = new ArrayList<>(this.cursos);
        java.util.Collections.sort(listaOrdenada);
        return listaOrdenada;
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }
}