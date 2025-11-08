package modelos.inscripcion;

import modelos.cursos.Curso;
import modelos.pago.Pago;
import modelos.usuario.Alumno;

import java.util.*;

public class Inscripcion {

    private int idInscripcion;
    private Date fecha;
    private String estado;

    public static final String ESTADO_ACEPTADA = "aceptada";
    public static final String ESTADO_PENDIENTE = "pendiente";

    // Relaciones clave (Agregación/Dependencia)
    private Alumno alumno;
    private Curso curso;

    // Relación de Agregación: Inscripcion "1" -- "0..*" Pago : genera >
    private List<Pago> pagos;

    private static int contador = 0;
    public Inscripcion( Alumno alumno, Curso curso) {
        contador++;
        this.idInscripcion = contador;
        this.fecha = new Date();
        this.alumno = alumno;
        this.curso = curso;
        this.estado = ESTADO_PENDIENTE;
        this.pagos = new ArrayList<>();
    }


    public void aceptar() {
        this.estado = ESTADO_ACEPTADA;
        System.out.println("Inscripción N°" + idInscripcion + " aceptada para el curso: " + curso.getNombre());
    }

    public void registrarPago(Pago nuevoPago) {
        this.pagos.add(nuevoPago);
    }

    public int getIdInscripcion() { return idInscripcion; }
    public Date getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public Alumno getAlumno() { return alumno; }
    public Curso getCurso() { return curso; }
    public List<Pago> getPagos() { return pagos; }
    @Override
    public String toString() {
        return "📘 Inscripción #" + idInscripcion +
                " | Alumno: " + alumno.getNombre() +
                " | Curso: " + curso.getNombre() +
                " | Estado: " + estado +
                " | Fecha: " + fecha;
    }


}