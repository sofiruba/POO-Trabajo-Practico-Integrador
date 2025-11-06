package core;

import java.time.LocalDate;

public abstract class Curso {

    private int idCurso;
    private String nombre;
    private String descripcion;
    private int cupo;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public static final String ESTADO_BORRADOR = "borrador";
    public static final String ESTADO_PUBLICADO = "publicado";
    public static final String ESTADO_CERRADO = "cerrado";

    public Curso(int idCurso, String nombre, String descripcion, int cupo) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cupo = cupo;
        this.estado = ESTADO_BORRADOR;
    }

    public void abrirInscripcion() {
        if (this.estado.equals(ESTADO_BORRADOR)) {
            this.estado = ESTADO_PUBLICADO;
            System.out.println("El curso '" + nombre + "' ha sido publicado y abre inscripciones.");
        } else {
            System.out.println("Error: El curso ya está en estado " + this.estado + ".");
        }
    }

    public void cerrarInscripcion() {
        if (this.estado.equals(ESTADO_PUBLICADO)) {
            // El curso puede estar 'publicado' sin haber iniciado.
            this.estado = ESTADO_CERRADO;
            System.out.println("Las inscripciones para el curso '" + nombre + "' han sido cerradas.");
        } else {
            System.out.println("Error: No se pueden cerrar inscripciones en el estado actual.");
        }
    }

    public abstract void iniciar();

    public abstract void finalizar();

    public String obtenerDetalle() {
        return "ID: " + idCurso +
                "\nNombre: " + nombre +
                "\nDescripción: " + descripcion +
                "\nCupo: " + cupo +
                "\nEstado: " + estado;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCupo() {
        return cupo;
    }

    public String getEstado() {
        return estado;
    }


}