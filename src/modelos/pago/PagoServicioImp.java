package modelos.pago;

import data.*;
import modelos.inscripcion.Inscripcion;

import java.util.Date;

public class PagoServicioImp implements PagoServicio {

    private final GestorBDDPago gestorPago;

    public PagoServicioImp() {
        this.gestorPago = new GestorBDDPago();
    }

    @Override
    public Recibo pagar(Inscripcion inscripcion, float monto, String tipoPago, int cuotas) {
        if (inscripcion == null) {
            System.err.println("❌ Error: La inscripción no puede ser nula.");
            return null;
        }

        // Simulación del pago
        System.out.println("💳 Procesando pago...");
        try {
            Thread.sleep(1000); // simula demora de procesamiento
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Crear el pago
        Pago pago = new Pago(new Date(),monto, tipoPago, cuotas);
        inscripcion.registrarPago(pago);

        // Guardar en la base de datos
        boolean guardado = gestorPago.guardar(inscripcion.getIdInscripcion(), monto, tipoPago, cuotas);

        if (!guardado) {
            System.err.println("⚠ No se pudo guardar el pago en la base de datos.");
        }

        // Crear el recibo
        Recibo recibo = new Recibo(
                pago.getIdPago(),
                inscripcion.getAlumno().getNombre() + inscripcion.getCurso().getNombre(),
                monto

        );

        System.out.printf("💳 Pago realizado por: %s | Curso: %s | Monto: %.2f%n",
                inscripcion.getAlumno().getNombre(),
                inscripcion.getCurso().getNombre(),
                monto
        );

        return recibo;
    }
}
