package controller;

import java.util.*;

import data.GestorBDDCalificacion;
import data.GestorBDDCurso;
import data.GestorBDDEvaluacion;
import data.GestorBDDInscripcion;
import data.GestorBDDModulo;
import modelos.cursos.*;
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
    private final GestorBDDModulo gestorModulo;
    private final GestorBDDEvaluacion gestorEvaluacion;
    private final GestorBDDCalificacion gestorCalificacion;

    public CursosController(PagoServicio pagoServicio, UsuariosController usuariosController) {
        this.pagoServicio = pagoServicio;
        this.usuariosController = usuariosController;
        this.gestorCurso = new GestorBDDCurso();
        this.gestorInscripciones = new GestorBDDInscripcion();
        this.gestorModulo = new GestorBDDModulo();
        this.gestorEvaluacion = new GestorBDDEvaluacion();
        this.gestorCalificacion = new GestorBDDCalificacion();

        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
        this.inscripciones = new ArrayList<>();

        // Cargar cursos desde la base de datos al iniciar
        this.cursos = new ArrayList<>(gestorCurso.buscarTodos());
        

        System.out.println("✅ Controladora inicializada. Se cargaron " + cursos.size() + " cursos desde la base.");
    }

    // --- CREAR CURSO ---
    public Curso crearCurso(Docente docente, String nombre, String desc, int cupo, float precio, String modalidad) {
        Curso cursoExistente = gestorCurso.buscarPorNombre(nombre);
            if (cursoExistente != null) {
                System.out.println("✅ El curso '" + nombre + "' ya existe en la BDD (ID: " + cursoExistente.getIdCurso() + "). Usando el existente.");
                return cursoExistente;
            }
        Curso nuevoCurso = null;

        switch (modalidad.toUpperCase()) {
            case "ONLINE" -> {
                nuevoCurso = new CursoOnline(nombre, desc, cupo, precio, "https://plataforma-temp.com", "Zoom");
                
                nuevoCurso=gestorCurso.guardar(nuevoCurso, docente);
            }
            case "PRESENCIAL" -> {
                nuevoCurso = new CursoPresencial(nombre, desc, cupo, precio, "Aula 101", "Av. Siempre Viva 123");
                nuevoCurso=gestorCurso.guardar(nuevoCurso, docente);
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
        if (gestorInscripciones.existeInscripcion(alumno.getId(), curso.getIdCurso())) {
        System.err.println("❌ ERROR: El alumno " + alumno.getNombre() + 
                           " ya se encuentra inscrito en el curso " + curso.getNombre() + ".");
        return null; // No creamos ni persistimos la inscripción
    }
        Inscripcion nuevaInscripcion = new Inscripcion(alumno, curso);
        
        nuevaInscripcion=gestorInscripciones.guardar(nuevaInscripcion);
        if (nuevaInscripcion != null) {
        alumno.inscribirse(curso); // ✅ Agrega el curso a la lista interna del Alumno
    }

        System.out.println("📝 Alumno " + alumno.getNombre() + " preinscripto en " + curso.getNombre());
        return nuevaInscripcion;
    }
// Archivo: CursosController.java

// Cambio el tipo de retorno a Alumno
// Archivo: CursosController.java

public Alumno crearAlumnoEnPlataforma(String nombre, String email, String contrasenia) {
    // 1. 🔍 BUSCAR SI EL ALUMNO YA EXISTE EN LA BDD
    Alumno alumnoExistente = usuariosController.buscarAlumnoPorEmail(email);

    if (alumnoExistente != null) {
        System.out.println("✅ El alumno ya existe en la plataforma (ID BDD: " + alumnoExistente.getId() + "), se usará el existente.");
        return alumnoExistente; // 👈 Retorna el objeto ya sincronizado con el ID de la BDD
    }
    
    // 2. 📝 SI NO EXISTE, CREARLO Y REGISTRARLO
    Alumno nuevoAlumno = new Alumno(nombre, email, contrasenia, new Date());

    // El metodo registrarAlumno ahora guarda en BDD y sincroniza el ID (por la solucion anterior)
    Alumno alumnoConIdBDD = usuariosController.registrarAlumno(nuevoAlumno);

    // Lo agregamos a la lista local si queremos tenerlo sincronizado
    if (alumnoConIdBDD != null) {
        alumnos.add(alumnoConIdBDD);
        System.out.println("✅ Alumno agregado a la plataforma desde CursosController: " + alumnoConIdBDD.getNombre());
    }

    return alumnoConIdBDD;
}

// Archivo: CursosController.java (Método crearDocenteEnPlataforma)

public Docente crearDocenteEnPlataforma(String nombre, String email, String contrasenia, String especialidad) {
    // 1. 🔍 BUSCAR SI EL DOCENTE YA EXISTE EN LA BDD
    Docente docenteExistente = usuariosController.buscarDocentePorEmail(email);

    if (docenteExistente != null) { // Mayor que 0 garantiza ID de BDD
        System.out.println("✅ Docente encontrado en BDD: " + docenteExistente.getNombre() + " (ID: " + docenteExistente.getId() + "). Se usa el existente.");
        return docenteExistente; // 👈 RETORNO INMEDIATO: Evita la creación/inserción duplicada
    }

    // 2. 📝 SI NO EXISTE, CREARLO Y REGISTRARLO
    // Creamos el objeto (esto le asigna el ID temporal de Java)
    Docente nuevoDocente = new Docente(nombre, email, contrasenia, especialidad); 

    // 3. Registrar el docente (persiste en BDD y sincroniza el ID)
    Docente docenteConIdBDD = usuariosController.registrarDocente(nuevoDocente);

    return docenteConIdBDD;
}
    public Recibo inscribirYPagar(Alumno alumno, Curso curso, float monto, String tipoPago, int cuotas)
            throws CupoCompletoException {

        Inscripcion inscripcion = inscribirAlumno(alumno, curso);
        if (inscripcion == null) {
        System.out.println("⚠️ Proceso de pago abortado. Inscripción no realizada.");
        return null;
    }
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

    public Modulo agregarModulo(Curso curso, String titulo, String contenido) {
        if (curso == null || curso.getIdCurso() == 0) {
            System.err.println("❌ Error: No se puede agregar módulo. El curso no existe o no tiene ID de BDD.");
            return null;
        }
        Modulo moduloExistente = gestorModulo.buscarModuloPorTituloYCurso(titulo, curso.getIdCurso());
    
    if (moduloExistente != null) {
        System.err.println("❌ ERROR: El módulo '" + titulo + "' ya existe para el curso " + curso.getNombre() + ".");
        return moduloExistente; // Devolvemos el módulo existente en lugar de crear uno nuevo.
    }

        // 1. Crear el objeto Java
        Modulo nuevoModulo = new Modulo(titulo, contenido);
        
        // 2. Persistir y sincronizar el ID
        nuevoModulo = gestorModulo.guardar(nuevoModulo, curso.getIdCurso());
        
        // 3. Agregar al objeto Curso en memoria
        if (nuevoModulo.getIdModulo() > 0) {
            curso.agregarModulo(nuevoModulo); // Asumiendo que Curso.java tiene agregarModulo(Modulo)
            System.out.println("✅ Módulo '" + titulo + "' agregado al curso '" + curso.getNombre() + "'.");
            return nuevoModulo;
        } else {
            System.err.println("❌ No se pudo persistir el módulo en la base de datos.");
            return null;
        }
    }
    public List<Modulo> obtenerModulosDeCurso(Curso curso) {
    if (curso == null || curso.getIdCurso() == 0) {
        System.err.println("❌ Error: El curso es nulo o no tiene ID de BDD.");
        return Collections.emptyList();
    }
    
    // 1. Obtener la lista de módulos de la BDD
    List<Modulo> modulosBdd = gestorModulo.obtenerModulosPorCurso(curso.getIdCurso());
    
    // 2. Opcional: Sincronizar la lista de módulos del objeto Curso en memoria
    // Esto asegura que si el objeto Curso se usa más tarde, tenga todos sus módulos cargados.
    curso.getModulos().clear(); // Asumo que getModulos() devuelve la lista interna.
    curso.getModulos().addAll(modulosBdd);
    
    System.out.println("Cargados " + modulosBdd.size() + " módulos para el curso '" + curso.getNombre() + "'.");
    
    return modulosBdd;
}

public Evaluacion agregarEvaluacion(Modulo modulo, String nombre, float notaMaxima, String descripcion) {
    if (modulo == null || modulo.getIdModulo() == 0) {
        System.err.println("❌ Error: No se puede agregar evaluación. El módulo no existe o no tiene ID de BDD.");
        return null;
    }
    Evaluacion evaluacionExistente = gestorEvaluacion.buscarEvaluacionPorNombreYModulo(nombre, modulo.getIdModulo());
    
    if (evaluacionExistente != null) {
        System.err.println("❌ ERROR: La evaluación '" + nombre + "' ya existe para el módulo " + modulo.getTitulo() + ".");
        return evaluacionExistente; // Devolvemos la evaluación existente en lugar de crear una nueva.
    }
    
    // 1. Crear el objeto Java
    Evaluacion nuevaEvaluacion = new Evaluacion(nombre, descripcion, notaMaxima);
    
    // 2. Persistir y sincronizar el ID
    nuevaEvaluacion = gestorEvaluacion.guardar(nuevaEvaluacion, modulo.getIdModulo());
    
    // 3. Agregar al objeto Modulo en memoria
    if (nuevaEvaluacion != null && nuevaEvaluacion.getIdEval() > 0) {
        modulo.agregarEvaluacion(nuevaEvaluacion); // Asumiendo que Modulo.java tiene agregarEvaluacion(Evaluacion)
        System.out.println("✅ Evaluación '" + nombre + "' agregada al módulo '" + modulo.getTitulo() + "'.");
        return nuevaEvaluacion;
    } else {
        System.err.println("❌ No se pudo persistir la evaluación.");
        return null;
    }
}



public Calificacion registrarCalificacion(Docente docente, Alumno alumno, Curso curso, Evaluacion evaluacion, float nota, String comentario) {
    
    // 1. VALIDACIÓN: Evitar calificación duplicada (1 x Evaluación x Alumno)
    if (gestorCalificacion.existeCalificacion(alumno.getId(), evaluacion.getIdEval())) {
        System.err.println("❌ ERROR: El alumno " + alumno.getNombre() + 
                           " ya tiene una calificación registrada para la evaluación '" + evaluacion.getNombre() + "'.");
        return null; 
    }
    
    Calificacion calificacion = docente.calificar(alumno, curso, evaluacion, nota, comentario);
    
    if (calificacion == null) return null;

    Calificacion calificacionSincronizada = gestorCalificacion.guardarCalificacion(calificacion); 
    
    if (calificacionSincronizada != null) {
        // 4. POO: Asignar la calificación sincronizada al Alumno
        alumno.agregarCalificacion(calificacionSincronizada);
        System.out.println("✅ Calificación de " + calificacionSincronizada.getNota() + 
                           " asignada al Alumno " + alumno.getNombre() + ".");
    }
    
    return calificacionSincronizada;
}

public void cargarCursosInscritos(Alumno alumno) {
    if (alumno == null) return;
    
    // 1. Obtener los IDs de cursos del alumno desde la tabla 'inscripcion'
    List<Integer> idsCursosInscritos = gestorInscripciones.obtenerCursosInscritosIds(alumno.getId());
    
    // 2. Limpiar la lista actual (solo si vas a re-cargar el mismo objeto)
    alumno.getCursos().clear(); 
    
    // 3. Buscar el objeto Curso completo por cada ID y agregarlo al Alumno
    for (Integer idCurso : idsCursosInscritos) {
        // Usamos el gestor de cursos para obtener el objeto completo
        Curso curso = gestorCurso.buscarCursoPorId(idCurso); 
        if (curso != null) {
            // Agregamos directamente a la lista del Alumno
            alumno.getCursos().add(curso); 
        }
    }
    
    // Aquí también iría la carga de calificaciones si tienes un Gestor para ellas.
    // Ej: gestorCalificaciones.cargarCalificaciones(alumno);

    System.out.println("🎓 Cursos cargados para " + alumno.getNombre() + ": " + alumno.getCursos().size());
}

public void cargarCalificaciones(Alumno alumno) {
    if (alumno == null) return;
    
    // 1. Obtener la data básica desde el gestor de Calificación
    List<Map.Entry<Integer, Float>> calificacionesBase = gestorCalificacion.obtenerCalificacionesBase(alumno.getId());
    
    // 2. Limpiar la lista de calificaciones actual del Alumno antes de cargar
    alumno.getCalificaciones().clear(); 
    
    // 3. Reconstruir cada objeto Calificacion
    for (Map.Entry<Integer, Float> entry : calificacionesBase) {
        int idEvaluacion = entry.getKey();
        float nota = entry.getValue();
        
        // 3.1. Buscar la Evaluación completa (y, por ende, su Módulo y Curso)
        // Nota: Asumo que tienes un método que busca la Evaluación completa por ID
        // y que este método puede inferir el Módulo y Curso. (Simplificación del flujo POO)
        
        Evaluacion evaluacion = gestorEvaluacion.buscarEvaluacionPorId(idEvaluacion); // Asumo que este método existe
        
        if (evaluacion != null) {
            // Nota: Aquí se necesitaría lógica compleja para saber el CURSO y el COMENTARIO.
            // Por simplicidad, asumiremos que el primer curso inscrito es el curso asociado.
            
            Curso cursoAsociado = alumno.getCursos().stream().findFirst().orElse(null); 
            
            if (cursoAsociado != null) {
                // Creamos la instancia de Calificación
                Calificacion calificacion = new Calificacion(
                    alumno, 
                    cursoAsociado, // Usamos el curso asociado
                    evaluacion, 
                    nota, 
                    "Cargada desde BDD" // Asumo un comentario por defecto
                );
                
                // 3.2. Agregar al objeto Alumno en memoria
                alumno.agregarCalificacion(calificacion);
            }
        }
    }
    
    System.out.println("⭐ Calificaciones cargadas para " + alumno.getNombre() + ": " + alumno.getCalificaciones().size());
}

public List<Alumno> obtenerAlumnosInscritos(int idCurso) {
    List<Integer> idsAlumnos = gestorInscripciones.obtenerAlumnosInscritosIds(idCurso);
    List<Alumno> alumnos = new ArrayList<>();
    
    for (Integer id : idsAlumnos) {
        // Asumo que tienes un método en UsuariosController o GestorBDDUsuario:
        // public Alumno buscarAlumnoPorId(int id)
        Alumno alumno = usuariosController.buscarAlumnoPorId(id); // ⚠️ Asumo que este método existe o lo creas.
        if (alumno != null) {
            alumnos.add(alumno);
        }
    }
    System.out.println("Cargados " + alumnos.size() + " alumnos inscritos para el curso ID " + idCurso);
    return alumnos;
}
    // --- GETTERS ---
    public List<Alumno> getAlumnos() { return alumnos; }
    public List<Curso> obtenerTodos() {
        return new ArrayList<>(cursos);
    }
    public List<Docente> getDocentes() { return docentes; }
    public List<Curso> getCursos() { return cursos; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }
}
