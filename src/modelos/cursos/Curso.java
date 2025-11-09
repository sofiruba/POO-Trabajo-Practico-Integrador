package modelos.cursos;

import java.time.LocalDate;
import java.util.*;

public abstract class Curso implements Comparable<Curso>{

    private int idCurso;
    private String nombre;
    private String descripcion;
    private int cupo;
    private String estado;
    private Date fechaInicio;
    private Date fechaFin;
    private List<Modulo> modulos = new ArrayList<>();

    public static final String ESTADO_BORRADOR = "borrador";
    public static final String ESTADO_PUBLICADO = "publicado";
    public static final String ESTADO_CERRADO = "cerrado";

    public Curso( String nombre, String descripcion, int cupo) {

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

    @Override
    public int compareTo(Curso otroCurso) {
        // Usamos String.compareTo para comparar alfabéticamente los nombres
        return this.nombre.compareTo(otroCurso.getNombre());
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }
    public void agregarModulo(Modulo modulo) {
        modulos.add(modulo);
    }

    // Buscar módulo por ID
    public Modulo buscarModuloPorId(int idModulo) {
        for (Modulo m : modulos) {
            if (m.getIdModulo() == idModulo) return m;
        }
        return null;
    }
    public Modulo buscarModuloPorIdEval(int idEval) {
        for (Modulo m : modulos) {
            if (m.buscarEvaluacionPorId(idEval) != null) {
                return m;
            }
        }
        return null;
    }

    public List<Modulo> getModulos() { return modulos; }

    @Override
    public String toString() {
        return "Curso{" + nombre + ", id=" + idCurso + "}";
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

}