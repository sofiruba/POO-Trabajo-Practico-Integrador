package exception;

public class CupoCompletoException extends Exception {

    public CupoCompletoException(String cursoNombre) {
        super("No es posible inscribir al alumno. El curso '" + cursoNombre + "' ha alcanzado su cupo máximo.");
    }
}