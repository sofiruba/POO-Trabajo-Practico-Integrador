package exception;

public class InscripcionNoAceptadaException extends Exception {

    public InscripcionNoAceptadaException(int inscripcionId, String estadoActual) {
        super("La operación no puede completarse. La inscripción ID " + inscripcionId + " no está aceptada, estado actual: " + estadoActual);
    }
}
