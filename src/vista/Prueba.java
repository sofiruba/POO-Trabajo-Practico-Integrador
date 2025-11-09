import controller.CursosController;
import controller.UsuariosController;
import modelos.pago.PagoServicioImp;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.usuario.Usuario;
import modelos.cursos.Calificacion;
import modelos.cursos.Curso;
import modelos.cursos.CursoOnline;
import modelos.cursos.Modulo;
import modelos.cursos.Evaluacion;
import modelos.pago.Recibo;
import exception.CupoCompletoException;

import java.util.Scanner;
import java.util.List;

public class Prueba {

    private static Scanner scanner = new Scanner(System.in);
    private static CursosController cursosController;
    private static UsuariosController usuariosController;

    public static void main(String[] args) {
        // 1️⃣ Inicialización de Controladoras
        usuariosController = new UsuariosController();
        cursosController = new CursosController(new PagoServicioImp(), usuariosController);
        usuariosController.setCursosController(cursosController);

        System.out.println("=========================================");
        System.out.println("📚 Plataforma de Cursos - MODO ROL");
        System.out.println("=========================================");

        Usuario usuarioLogueado = null;

        while (usuarioLogueado == null) {
            System.out.println("\n--- INICIO DE SESIÓN / REGISTRO ---");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrar Alumno");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    usuarioLogueado = loginUsuario();
                    break;
                case "2":
                    usuarioLogueado = registrarAlumno();
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }

        // 2️⃣ Menú de Navegación por Rol
        if (usuarioLogueado instanceof Alumno) {
            menuAlumno((Alumno) usuarioLogueado);
        } else if (usuarioLogueado instanceof Docente) {
            menuDocente((Docente) usuarioLogueado);
        }
        
        System.out.println("\n👋 Gracias por usar la plataforma.");
        scanner.close();
    }

    // ===================================
    // METODOS DE AUTENTICACION
    // ===================================

    private static Usuario loginUsuario() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        Usuario usuario = usuariosController.login(email, pass);
        if (usuario != null) {
            System.out.println("✅ ¡Bienvenido, " + usuario.getNombre() + "!");
        } else {
            System.out.println("❌ Login fallido. Credenciales incorrectas.");
        }
        return usuario;
    }

    private static Alumno registrarAlumno() {
        System.out.println("\n--- REGISTRO ALUMNO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        Alumno alumno = cursosController.crearAlumnoEnPlataforma(nombre, email, pass);
        if (alumno != null) {
            System.out.println("✅ Registro exitoso. ¡Inicia sesión para continuar!");
        } else {
            System.out.println("❌ Falló el registro (el email ya está en uso o error de BDD).");
        }
        // Nota: Devolvemos null para forzar un login tras el registro
        return null; 
    }

    // ===================================
    // MENÚ DE DOCENTE
    // ===================================

private static void menuDocente(Docente docente) {
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n--- MENÚ DOCENTE (" + docente.getNombre() + ") ---");
            System.out.println("1. Crear Nuevo Curso"); // Solo crea el curso
            System.out.println("2. Agregar Módulo a un Curso"); // Crea el módulo
            System.out.println("3. Agregar Evaluación a un Módulo"); // Crea la evaluación
            System.out.println("4. Ver Mis Cursos");
            System.out.println("5. Calificar Alumno (Simulación)");
            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    // 1. CREAR CURSO
                    crearCursoInteractivo(docente);
                    break;
                case "2":
                    // 2. AGREGAR MÓDULO (Requiere seleccionar Curso)
                    agregarModuloACurso(docente);
                    break;
                case "3":
                    // 3. AGREGAR EVALUACIÓN (Requiere seleccionar Curso y Módulo)
                    agregarEvaluacionAModulo(docente);
                    break;
                case "4":
                    verCursosDocente();
                    break;
                case "5":
                    simularCalificacion(docente);
                    break;
                case "0":
                    usuariosController.logout(docente);
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
    private static Curso seleccionarCurso(String mensaje) {
        System.out.println("\n--- " + mensaje + " ---");
        List<Curso> cursos = cursosController.obtenerTodos();
        if (cursos.isEmpty()) {
            System.out.println("No hay cursos creados.");
            return null;
        }
        
        cursos.forEach(c -> System.out.printf("[%d] %s (Precio: $%.2f)\n", c.getIdCurso(), c.getNombre(), c.getPrecio()));
        System.out.print("Ingrese el ID del curso: ");
        
        try {
            int idCurso = Integer.parseInt(scanner.nextLine());
            return cursos.stream()
                .filter(c -> c.getIdCurso() == idCurso)
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("ID de curso inválido.");
                    return null;
                });
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida. Debe ser un número.");
            return null;
        }
    }
    
    private static void agregarModuloACurso(Docente docente) {
        Curso curso = seleccionarCurso("AGREGAR MÓDULO");
        if (curso == null) return;
        
        // El resto de la lógica de crear el módulo
        Modulo modulo = agregarModuloInteractivo(curso);
        if (modulo != null) {
            System.out.println("✅ Módulo '" + modulo.getTitulo() + "' agregado con éxito.");
        }
    }

    private static void agregarEvaluacionAModulo(Docente docente) {
        Curso curso = seleccionarCurso("AGREGAR EVALUACIÓN");
        if (curso == null) return;
        
        List<Modulo> modulos = cursosController.obtenerModulosDeCurso(curso);
        if (modulos.isEmpty()) {
            System.out.println("El curso '" + curso.getNombre() + "' no tiene módulos.");
            return;
        }

        System.out.println("\n--- MÓDULOS DISPONIBLES ---");
        modulos.forEach(m -> System.out.printf("[%d] %s\n", m.getIdModulo(), m.getTitulo()));
        System.out.print("Ingrese el ID del módulo para añadir la evaluación: ");
        
        try {
            int idModulo = Integer.parseInt(scanner.nextLine());
            Modulo modulo = modulos.stream()
                .filter(m -> m.getIdModulo() == idModulo)
                .findFirst()
                .orElse(null);

            if (modulo == null) {
                System.out.println("ID de módulo inválido.");
                return;
            }

            // El resto de la lógica de crear la evaluación
            agregarEvaluacionInteractivo(modulo);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida. Debe ser un número.");
        }
    }

    // --- MÉTODOS AUXILIARES (SIMPLIFICADOS) ---

    private static void verCursosDocente() {
        System.out.println("\n--- CURSOS EN EL SISTEMA ---");
        List<Curso> cursos = cursosController.obtenerTodos(); 
        cursos.forEach(c -> {
            System.out.printf("📘 [%d] %s (Precio: $%.2f, Modalidad: %s)\n", 
                c.getIdCurso(), c.getNombre(), c.getPrecio(), c instanceof CursoOnline ? "Online" : "Presencial");
        });
    }
    
    private static void crearCursoYContenido(Docente docente) {
        // Usa la misma lógica interactiva de creación de curso del ejemplo anterior
        Curso curso = crearCursoInteractivo(docente);
        if (curso != null) {
            Modulo modulo = agregarModuloInteractivo(curso);
            if (modulo != null) {
                agregarEvaluacionInteractivo(modulo);
            }
        }
    }
    
    private static void verCursosDocente(Docente docente) {
        System.out.println("\n--- TUS CURSOS ---");
        // Nota: Asume que tienes un método para cargar los cursos del docente
        List<Curso> cursos = cursosController.getCursos(); 
        cursos.forEach(c -> {
            System.out.println("📘 " + c.getNombre() + " (ID: " + c.getIdCurso() + ")");
        });
    }

    private static void simularCalificacion(Docente docente) {
    System.out.println("\n--- INICIAR CALIFICACIÓN ---");

    // 1. SELECCIÓN DEL CURSO
    System.out.println("Seleccione el curso a calificar:");
    List<Curso> cursos = cursosController.obtenerTodos();
    if (cursos.isEmpty()) {
        System.out.println("No hay cursos disponibles para calificar.");
        return;
    }
    cursos.forEach(c -> System.out.printf("[%d] %s\n", c.getIdCurso(), c.getNombre()));
    
    System.out.print("Ingrese el ID del curso: ");
    int idCurso;
    try {
        idCurso = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("❌ Entrada inválida.");
        return;
    }

    Curso cursoSeleccionado = cursos.stream()
            .filter(c -> c.getIdCurso() == idCurso)
            .findFirst()
            .orElse(null);

    if (cursoSeleccionado == null) {
        System.out.println("Curso no encontrado.");
        return;
    }
    
    // 2. LISTAR ALUMNOS INSCRITOS EN EL CURSO
    List<Alumno> alumnosInscritos = cursosController.obtenerAlumnosInscritos(idCurso);
    if (alumnosInscritos.isEmpty()) {
        System.out.println("No hay alumnos inscritos en este curso para calificar.");
        return;
    }

    System.out.println("\n--- ALUMNOS INSCRITOS EN " + cursoSeleccionado.getNombre() + " ---");
    alumnosInscritos.forEach(a -> System.out.printf("[%d] %s (%s)\n", a.getId(), a.getNombre(), a.getEmail()));
    
    // 3. SELECCIÓN DE ALUMNO Y EVALUACIÓN (simplificado)
    System.out.print("Ingrese el ID del alumno a calificar: ");
    int idAlumno;
    try {
        idAlumno = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
        System.out.println("❌ Entrada inválida.");
        return;
    }

    Alumno alumnoACalificar = alumnosInscritos.stream()
            .filter(a -> a.getId() == idAlumno)
            .findFirst()
            .orElse(null);

    if (alumnoACalificar == null) {
        System.out.println("ID de alumno inválido.");
        return;
    }

    // Simplificación: Usamos la primera evaluación disponible
    List<Modulo> modulos = cursosController.obtenerModulosDeCurso(cursoSeleccionado);
    if (modulos.isEmpty() || modulos.get(0).getEvaluaciones().isEmpty()) {
        System.out.println("El curso no tiene evaluaciones cargadas.");
        return;
    }
    Evaluacion evaluacion = modulos.get(0).getEvaluaciones().get(0);


    // 4. REGISTRAR CALIFICACIÓN
    System.out.println("\nCalificando a " + alumnoACalificar.getNombre() + " en: " + evaluacion.getNombre());
    System.out.print("Nota (ej. 8.5): ");
    float nota = Float.parseFloat(scanner.nextLine());
    System.out.print("Comentario: ");
    String comentario = scanner.nextLine();

    Calificacion calificacion = cursosController.registrarCalificacion(docente, alumnoACalificar, cursoSeleccionado, evaluacion, nota, comentario);
    if (calificacion != null) {
        System.out.println("⭐ Calificación registrada con éxito para " + alumnoACalificar.getNombre());
    } else {
        System.err.println("❌ La calificación falló o ya existe.");
    }
}

    // ===================================
    // MENÚ DE ALUMNO
    // ===================================

    // Archivo: MainApp.java (Método menuAlumno)

private static void menuAlumno(Alumno alumno) {
    String opcion = "";
    while (!opcion.equals("0")) {
        System.out.println("\n--- MENÚ ALUMNO (" + alumno.getNombre() + ") ---");
        System.out.println("1. Ver Cursos Disponibles e Inscribirse");
        System.out.println("2. Ver Mis Inscripciones / Calificaciones"); // Cambiado para reflejar el contenido
        System.out.println("3. Rendir Evaluación"); 
        System.out.println("0. Cerrar Sesión");
        System.out.print("Seleccione una opción: ");
        opcion = scanner.nextLine();

        switch (opcion) {
            case "1":
                inscribirAlumnoInteractivo(alumno);
                break;
            case "2":
                verMisInscripciones(alumno); // Usamos la función de listado
                break;
            case "3":
                rendirYVerCalificaciones(alumno); // Usamos la función de rendir/ver
                break;
            case "0":
                usuariosController.logout(alumno);
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }
}
    
    private static void inscribirAlumnoInteractivo(Alumno alumno) {
        System.out.println("\n--- CURSOS DISPONIBLES ---");
        List<Curso> cursos = cursosController.obtenerTodos();
        if (cursos.isEmpty()) {
            System.out.println("No hay cursos disponibles.");
            return;
        }

        cursos.forEach(c -> System.out.println("[" + c.getIdCurso() + "] " + c.getNombre()));
        System.out.print("Ingrese el ID del curso para inscribirse: ");
        int idCurso = Integer.parseInt(scanner.nextLine());

        // Busca el curso seleccionado
        Curso cursoSeleccionado = cursos.stream()
                .filter(c -> c.getIdCurso() == idCurso)
                .findFirst()
                .orElse(null);

        if (cursoSeleccionado == null) {
            System.out.println("ID de curso inválido.");
            return;
        }
        
        System.out.println("Inscripción a: " + cursoSeleccionado.getNombre());
    
    // 1. Inscripción y Pago
    System.out.println("Costo del curso " + cursoSeleccionado.getNombre() + ": $" + String.format("%.2f", cursoSeleccionado.getPrecio())); // Muestra el costo
    float monto = cursoSeleccionado.getPrecio();
    System.out.print("Tipo de pago (TARJETA/EFECTIVO): ");
    String tipo = scanner.nextLine();

    try {
        Recibo recibo = cursosController.inscribirYPagar(alumno, cursoSeleccionado, monto, tipo, 1);
            if (recibo != null) {
                System.out.println("✅ Inscripción y Pago exitosos!");
            }
        } catch (CupoCompletoException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    private static Curso crearCursoInteractivo(Docente docente) {
        System.out.println("\n--- CREACIÓN DE CURSO ---");
        System.out.print("Nombre del Curso: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String desc = scanner.nextLine();
        System.out.print("Cupo máximo: ");
        int cupo = Integer.parseInt(scanner.nextLine());
        System.out.print("Precio: ");
        float precio = Float.parseFloat(scanner.nextLine());
        System.out.print("Modalidad (ONLINE/PRESENCIAL): ");
        String modalidad = scanner.nextLine();

        return cursosController.crearCurso(docente, nombre, desc, cupo, precio, modalidad);
    }
    private static Modulo agregarModuloInteractivo(Curso curso) {
        System.out.println("\n--- AGREGAR MÓDULO ---");
        System.out.print("Título del Módulo: ");
        String titulo = scanner.nextLine();
        System.out.print("Contenido del Módulo: ");
        String contenido = scanner.nextLine();
        
        return cursosController.agregarModulo(curso, titulo, contenido);
    }
    private static Evaluacion agregarEvaluacionInteractivo(Modulo modulo) {
        System.out.println("\n--- AGREGAR EVALUACIÓN ---");
        System.out.print("Nombre de la Evaluación: ");
        String nombre = scanner.nextLine();
        System.out.print("Nota Máxima (ej. 10.0): ");
        float notaMax = Float.parseFloat(scanner.nextLine());
        System.out.print("Descripción de la Evaluación: ");
        String desc = scanner.nextLine();
        
        return cursosController.agregarEvaluacion(modulo, nombre, notaMax, desc);
    }

    // Archivo: MainApp.java (Método verMisInscripciones corregido)

private static void verMisInscripciones(Alumno alumno) {
    String opcion = "";
    while (!opcion.equals("0")) { // Bucle principal para la navegación
        
        System.out.println("\n--- MIS INSCRIPCIONES ---");
        List<Curso> misCursos = alumno.getCursos();
        
        if (misCursos.isEmpty()) {
            System.out.println("No estás inscrito en ningún curso. Usa la opción 'Inscribirse'.");
            System.out.println("Presione ENTER para volver...");
            scanner.nextLine();
            return; // Salimos de la función si no hay cursos
        }

        // 1. Listar cursos inscritos para seleccionar
        System.out.println("Seleccione el ID de un curso para ver su contenido:");
        misCursos.forEach(c -> System.out.printf("[%d] %s\n", c.getIdCurso(), c.getNombre()));
        
        // 2. Mostrar calificaciones globales del alumno
        System.out.println("\n--- TUS CALIFICACIONES REGISTRADAS ---");
        if (alumno.getCalificaciones().isEmpty()) {
            System.out.println("Aún no tienes notas registradas.");
        } else {
            // Se asume que el objeto Calificacion tiene los getters necesarios
            alumno.getCalificaciones().forEach(c -> {
                System.out.printf(" * [Nota: %.2f] %s en el curso %s\n", 
                    c.getNota(), c.getEvaluacion().getNombre(), c.getCurso().getNombre());
            });
        }
        
        // 3. Menú de navegación
        System.out.println("\n0. Volver al menú principal");
        System.out.print("Ingrese ID del curso (o 0 para salir): ");
        
        try {
            opcion = scanner.nextLine();
            if (opcion.equals("0")) break; // Sale del bucle

            int idCurso = Integer.parseInt(opcion);
            Curso cursoSeleccionado = misCursos.stream()
                    .filter(c -> c.getIdCurso() == idCurso)
                    .findFirst()
                    .orElse(null);

            if (cursoSeleccionado == null) {
                System.out.println("ID de curso inválido.");
                continue; // Vuelve al inicio del bucle
            }

            // 4. Llamar a la función de navegación de contenido
            navegarContenido(cursoSeleccionado); 

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida. Debe ser un número.");
        }
    }
}
private static void navegarContenido(Curso curso) {
    System.out.println("\n--- ESTRUCTURA ACADÉMICA de " + curso.getNombre() + " ---");
    
    // Cargar Módulos y Evaluaciones (Asegurar la hidratación)
    List<Modulo> modulos = cursosController.obtenerModulosDeCurso(curso);
    
    if (modulos.isEmpty()) {
        System.out.println("Este curso no tiene módulos cargados.");
        return;
    }

    for (int i = 0; i < modulos.size(); i++) {
        Modulo modulo = modulos.get(i);
        System.out.printf("\n📘 MÓDULO %d: %s\n", i + 1, modulo.getTitulo());
        
        // Ver Evaluaciones dentro del Módulo
        List<Evaluacion> evaluaciones = modulo.getEvaluaciones();
        if (evaluaciones.isEmpty()) {
            System.out.println("   (No hay evaluaciones cargadas para este módulo)");
        } else {
            System.out.println("   📝 Evaluaciones:");
            for (Evaluacion eval : evaluaciones) {
                System.out.printf("    -> %s (Máx: %.1f)\n", eval.getNombre(), eval.getNotaMaxima());
            }
        }
    }
    System.out.println("\nPresione ENTER para volver...");
    scanner.nextLine(); // Espera a que el usuario presione Enter para volver al menú de Mis Cursos.
}

private static void rendirYVerCalificaciones(Alumno alumno) {
    System.out.println("\n--- RENDIR / VER NOTAS ---");

    // Lógica para RENDICIÓN (Simulación)
    System.out.println("1. Rendir una Evaluación (Simulación)");
    System.out.println("2. Ver mi Historial de Calificaciones");
    System.out.print("Seleccione una opción: ");
    String opcion = scanner.nextLine();

    if (opcion.equals("1")) {
        // Simulación: Buscamos la primera inscripción activa y la primera evaluación
        if (alumno.getCursos().isEmpty()) {
            System.out.println("Debes estar inscrito en un curso para rendir.");
            return;
        }
        Curso curso = alumno.getCursos().get(0);
        
        // Simulación de encontrar la primera evaluación del primer módulo
        List<Modulo> modulos = cursosController.obtenerModulosDeCurso(curso);
        if (modulos.isEmpty() || modulos.get(0).getEvaluaciones().isEmpty()) {
            System.out.println("El curso no tiene evaluaciones cargadas.");
            return;
        }
        Modulo modulo = modulos.get(0);
        Evaluacion evaluacion = modulo.getEvaluaciones().get(0);

        // Acción POO: Alumno rinde
        System.out.println("\n[SIMULACIÓN] Rindiendo: " + evaluacion.getNombre());
        alumno.rendirEvaluacion(curso, modulo, evaluacion);
        System.out.println("✅ Esperando calificación del docente.");

    } else if (opcion.equals("2")) {
        // Lógica para VER CALIFICACIONES
        if (alumno.getCalificaciones().isEmpty()) {
            System.out.println("Aún no tienes notas registradas.");
        } else {
            System.out.println("\n--- TUS NOTAS ---");
            alumno.getCalificaciones().forEach(c -> {
                System.out.printf("Nota: %.2f en %s (Curso: %s)\n", 
                    c.getNota(), c.getEvaluacion().getNombre(), c.getCurso().getNombre());
            });
        }
    } else {
        System.out.println("Opción inválida.");
    }
}
}