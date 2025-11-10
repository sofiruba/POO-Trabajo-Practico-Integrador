package modelos;

public final class ConstantesDeNegocio {

    // Constantes de Módulos (Ejemplo de uso de static final)
    public static final String TIPO_EVALUACION_PARCIAL = "parcial";
    public static final String TIPO_EVALUACION_FINAL = "final";
    public static final String TIPO_EVALUACION_TP = "tp";

    // Constantes de Roles (Si no usas Enum)
    public static final String ROL_ALUMNO = "ALUMNO";
    public static final String ROL_DOCENTE = "DOCENTE";

    // Regla de Negocio
    public static final float NOTA_MINIMA_APROBACION = 6.0f;

    // Constructor privado para evitar instanciación (clase utilitaria)
    private ConstantesDeNegocio() {
        // No se permite crear instancias de esta clase de utilidad.
    }
}