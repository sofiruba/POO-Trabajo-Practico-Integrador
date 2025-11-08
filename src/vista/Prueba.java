import controller.CursosController;
import modelos.cursos.Curso;
import modelos.cursos.Modulo;
import modelos.cursos.Evaluacion;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.pago.PagoServicio;
import modelos.pago.Recibo;

import java.util.Date;

public class Prueba {
    public static void main(String[] args) {
        // Simulamos un servicio de pago fake
        PagoServicio pagoFake = (inscripcion, monto, tipo, cuotas) -> new Recibo(1,"", monto);

        CursosController controller = new CursosController(pagoFake);

        Docente docente = new Docente("Laura", "laura@mail.com", "1234", "Programación");
        Alumno alumno = new Alumno("Sofi", "sofi@mail.com", "abcd", new Date());

        Curso curso = controller.crearCurso(docente, "Java Inicial", "Intro a Java", 20, "online");

        // Crear módulos
        Modulo m1 = new Modulo("Fundamentos", "Variables y tipos de datos");
        Modulo m2 = new Modulo("POO", "Clases y objetos");
        curso.agregarModulo(m1);
        curso.agregarModulo(m2);

        // Crear evaluaciones y agregarlas a los módulos
        Evaluacion e1 = new Evaluacion(Evaluacion.TIPO_TP, new Date(), 10);
        Evaluacion e2 = new Evaluacion(Evaluacion.TIPO_FINAL, new Date(), 100);
        m1.agregarEvaluacion(e1);
        m2.agregarEvaluacion(e2);

        // Buscar módulo por ID de evaluación
        Modulo buscado = controller.buscarModuloPorIdEval(curso, e2.getIdEval());
        if (buscado != null) {
            System.out.println("✅ Evaluación " + e2.getTipo() + " está en el módulo: " + buscado.getTitulo());
        } else {
            System.out.println("❌ No se encontró el módulo.");
        }

        // Inscribir y pagar
        try {
            Recibo recibo = controller.inscribirYPagar(alumno, curso, 5000f, "tarjeta", 1);
            System.out.println("💳 Pago realizado por: " + alumno.getNombre() + " | Monto: " + recibo.getMonto());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
