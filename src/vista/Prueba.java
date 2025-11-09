import controller.CursosController;
import controller.UsuariosController;
import modelos.pago.*;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.cursos.Curso;
import modelos.cursos.Modulo;
import modelos.cursos.*; // Asegúrate de que el paquete es correcto
import exception.CupoCompletoException;

import java.util.Date;
import java.util.List;

public class Prueba {
    public static void main(String[] args) {

        // 1️⃣ Crear PagoServicio
        PagoServicio pagoServicio = new PagoServicioImp();
        UsuariosController controllerU = new UsuariosController();

        // 2️⃣ Inicializar controladora
        CursosController cursosController = new CursosController(pagoServicio, controllerU);

        // 3️⃣ Crear docente (Se guarda en BDD y sincroniza ID)
        Docente docente = cursosController.crearDocenteEnPlataforma("Juan Perez", "juan@mail.com", "1234", "Programación");
        
        // --- 4️⃣ CREACIÓN DE CURSO Y CONTENIDO ---
        
        // 4.1️⃣ Crear curso online (Se guarda en BDD y sincroniza ID)
        Curso curso = cursosController.crearCurso(docente, "Java Inicial", "Curso base de Java", 20, "ONLINE");

        // 4.2️⃣ Agregar Módulos
        Modulo modulo1 = cursosController.agregarModulo(curso, "Introducción a Java", "Variables y estructuras de control.");
        Modulo modulo2 = cursosController.agregarModulo(curso, "Programación Orientada a Objetos", "Clases, objetos, herencia y polimorfismo.");

        // TEST UNICIDAD: Intentar agregar el mismo módulo (debería fallar)
        cursosController.agregarModulo(curso, "Introducción a Java", "Variables y estructuras de control."); 

        // 4.3️⃣ Agregar Evaluaciones (Hecho por el Docente)
        Evaluacion eval1 = null;
        Evaluacion eval2 = null;
        if (modulo1 != null) {
            System.out.println("\n--- Creación de Evaluaciones por Docente ---");
            // Evaluación 1 para Módulo 1
            eval1 = cursosController.agregarEvaluacion(
                modulo1, 
                "Quiz Inicial", 
                10.0f, 
                "Cuestionario sobre variables y bucles básicos."
            );
            // TEST UNICIDAD: Intentar agregar la misma evaluación (debería fallar)
            cursosController.agregarEvaluacion(modulo1, "Quiz Inicial", 10.0f, "Cuestionario sobre variables y bucles básicos.");
        }

        if (modulo2 != null) {
            // Evaluación 2 para Módulo 2
            eval2 = cursosController.agregarEvaluacion(
                modulo2, 
                "Proyecto Final POO", 
                100.0f, 
                "Implementar un sistema con herencia y polimorfismo."
            );
        }

        // 4.4️⃣ Listar Módulos y Evaluaciones
        System.out.println("\n--- Módulos y Evaluaciones del curso " + (curso != null ? curso.getNombre() : "N/A") + " ---");
        List<Modulo> listaModulos = cursosController.obtenerModulosDeCurso(curso);
        for (Modulo m : listaModulos) {
            System.out.println("  [ID: " + m.getIdModulo() + "] " + m.getTitulo());
            for (Evaluacion e : m.getEvaluaciones()) {
                System.out.println("    -> Evaluación: " + e.getNombre() + " (Max: " + e.getNotaMaxima() + ")");
            }
        }
        
        // --- 5️⃣ ALUMNO Y CALIFICACIÓN ---

        // 5.1️⃣ Crear alumno (Se guarda en BDD y sincroniza ID)
        Alumno alumno = cursosController.crearAlumnoEnPlataforma("Sofi", "sofia@mail.com", "abcd");

        // 5.2️⃣ Alumno rinde la evaluación (Simulación de la acción POO)
        if (modulo1 != null && eval1 != null) {
            System.out.println("\n--- Alumno Rinde Evaluación ---");
            alumno.rendirEvaluacion(curso, modulo1, eval1);
        }
        
        // 5.3️⃣ Docente registra Calificación (Se guarda en BDD y sincroniza ID)
        System.out.println("\n--- Docente Registra Calificación ---");
        Calificacion calificacion1 = null;
        if (alumno != null && curso != null && eval1 != null) {
            calificacion1 = cursosController.registrarCalificacion(
                docente, 
                alumno, 
                curso, 
                eval1, 
                8.5f, 
                "Buen entendimiento de bucles."
            );
            System.out.println("⭐ " + (calificacion1 != null ? calificacion1 : "Error al registrar calificación."));
        }

        // TEST UNICIDAD: Intentar calificar al mismo alumno en la misma evaluación (debería fallar)
        cursosController.registrarCalificacion(docente, alumno, curso, eval1, 9.0f, "Intento duplicado.");


        // --- 6️⃣ INSCRIPCIÓN Y PAGO ---

        // 6.1️⃣ Inscribir alumno y pagar (con validación de unicidad)
        try {
            System.out.println("\n--- Inscripción y Pago ---");
            Recibo recibo = cursosController.inscribirYPagar(alumno, curso, 5000f, "TARJETA", 1);
            
            // CLAVE: Validar que el recibo no sea null
            if (recibo != null) {
                // Se asume que getMonto() devuelve un String que necesita limpieza
                float montoNew = Float.parseFloat(recibo.getMonto().replace(",", "."));
                System.out.printf("💳 Pago realizado por: %s | Monto: %.2f\n", alumno.getNombre(), montoNew);
            } else {
                System.out.println("⚠️ Proceso de pago abortado. Inscripción ya existe o pago falló.");
            }
            
        } catch (CupoCompletoException e) {
            System.err.println("❌ No se pudo inscribir al alumno: " + e.getMessage());
        }
    }
}