package core;

import java.util.Date;

public class Calificacion {

    private int idCalif;
    private float nota;
    private String comentario;
    private Date fechaRegistro;

    public Calificacion(int idCalif, float nota, String comentario, Date fechaRegistro) {
        this.idCalif = idCalif;
        this.nota = nota;
        this.comentario = comentario;
        this.fechaRegistro = fechaRegistro;
    }

    public boolean estaAprobada(float notaMinima) {
        return this.nota >= notaMinima;
    }

    public int getIdCalif() { return idCalif; }
    public float getNota() { return nota; }
    public String getComentario() { return comentario; }
    public Date getFechaRegistro() { return fechaRegistro; }

}