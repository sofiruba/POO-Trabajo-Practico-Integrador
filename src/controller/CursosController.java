package controller;

import java.util.*;
import data.GestorBDDCurso;
import data.GestorBDDInscripcion;
import data.GestorBDDModulo;
import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.CursoPresencial;
import modelos.cursos.Modulo;
import modelos.inscripcion.Inscripcion;
import modelos.pago.PagoServicio;
import modelos.pago.Recibo;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.usuario.Usuario;
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

    public CursosController(PagoServicio pagoServicio, UsuariosController usuariosController) {
        this.pagoServicio = pagoServicio;
        this.usuariosController = usuariosController;
        this.gestorCurso = new GestorBDDCurso();
        this.gestorInscripciones = new GestorBDDInscripcion();
        this.gestorModulo = new GestorBDDModulo();

        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
        this.inscripciones = new ArrayList<>();

        // Cargar cursos desde la base de datos al iniciar
        this.cursos = new ArrayList<>(gestorCurso.buscarTodos());
        

        System.out.println("✅ Controladora inicializada. Se cargaron " + cursos.size() + " cursos desde la base.");
    }

    // --- CREAR CURSO ---
    public Curso crearCurso(Docente docente, String nombre, String desc, int cupo, String modalidad) {
        Curso cursoExistente = gestorCurso.buscarPorNombre(nombre);
            if (cursoExistente != null) {
                System.out.println("✅ El curso '" + nombre + "' ya existe en la BDD (ID: " + cursoExistente.getIdCurso() + "). Usando el existente.");
                return cursoExistente;
            }
        Curso nuevoCurso = null;

        switch (modalidad.toUpperCase()) {
            case "ONLINE" -> {
                nuevoCurso = new CursoOnline(nombre, desc, cupo, "https://plataforma-temp.com", "Zoom");
                
                nuevoCurso=gestorCurso.guardar(nuevoCurso, docente);
            }
            case "PRESENCIAL" -> {
                nuevoCurso = new CursoPresencial(nombre, desc, cupo, "Aula 101", "Av. Siempre Viva 123");
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
    // --- GETTERS ---
    public List<Alumno> getAlumnos() { return alumnos; }
    public List<Curso> obtenerTodos() {
        return new ArrayList<>(cursos);
    }
    public List<Docente> getDocentes() { return docentes; }
    public List<Curso> getCursos() { return cursos; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }
}
