import controller.CursosController;
import controller.UsuariosController;
import modelos.pago.PagoServicio;
import modelos.pago.*;
import modelos.pago.Recibo;
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
        Alumno alumno = new Alumno("Sofi", "sofiAGAY@mail.com", "abcd", new Date());
        cursosController.crearAlumnoEnPlataforma(alumno.getNombre(), alumno.getEmail(), alumno.getContrasenia());


        // 6️⃣ Inscribir alumno y pagar
        try {
            Recibo recibo = cursosController.inscribirYPagar(alumno, curso, 5000f, "TARJETA", 1);
            System.out.println("💳 Pago realizado por: " + alumno.getNombre() + " | Monto: " + recibo.getMonto());
        } catch (CupoCompletoException e) {
            System.err.println("❌ No se pudo inscribir al alumno: " + e.getMessage());
        }
    }
}

        // 7️⃣ Listar curs
