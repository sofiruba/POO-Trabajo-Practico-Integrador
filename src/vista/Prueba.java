import controller.CursosController;
import controller.UsuariosController;
import modelos.pago.*;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.cursos.Curso;
import exception.CupoCompletoException;

import java.util.Date;

public class Prueba {
    public static void main(String[] args) {

        // 1️⃣ Crear PagoServicio
        PagoServicio pagoServicio = new PagoServicioImp();

        UsuariosController controllerU = new UsuariosController();

        // 2️⃣ Inicializar controladora
        CursosController cursosController = new CursosController(pagoServicio, controllerU);

        // 3️⃣ Crear docente
        Docente docente = new Docente("Juan Perez", "juan@mail.com", "1234", "Programación");
        cursosController.getDocentes().add(docente);

        // 4️⃣ Crear curso online
        Curso curso = cursosController.crearCurso(docente, "Java ", "Aprendé Java desde cero", 20, "ONLINE");

        // 5️⃣ Crear alumno
        Alumno alumno = cursosController.crearAlumnoEnPlataforma("Sofi", "sofiAGAY@mail.com", "abcd");

        // 6️⃣ Inscribir alumno y pagar
   // Archivo: Prueba.java

// 6️⃣ Inscribir alumno y pagar
try {
    Recibo recibo = cursosController.inscribirYPagar(alumno, curso, 5000f, "TARJETA", 1);
    
    // 💡 CLAVE: Validar que el recibo no sea null antes de usarlo
    if (recibo != null) {
        float montoNew = Float.parseFloat(recibo.getMonto().replace(",", "."));
        System.out.printf("💳 Pago realizado por: %s | Monto: %.2f\n", alumno.getNombre(), montoNew);
    } else {
        System.out.println("⚠️ No se generó recibo porque la inscripción ya existe o el pago falló.");
    }
    
} catch (CupoCompletoException e) {
    System.err.println("❌ No se pudo inscribir al alumno: " + e.getMessage());
}
// 7️⃣ Listar curs
    }
}

        // 7️⃣ Listar curs
