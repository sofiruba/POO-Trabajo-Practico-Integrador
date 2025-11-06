package core;

public class CursoPresencial extends Curso {

    private String aula;
    private String direccion;

    public CursoPresencial(int idCurso, String nombre, String descripcion, int cupo,
                           String aula, String direccion) {
        super(idCurso, nombre, descripcion, cupo);
        this.aula = aula;
        this.direccion = direccion;
    }

    @Override
    public void iniciar() {
        if (getEstado().equals(Curso.ESTADO_PUBLICADO)) {
            System.out.println("core.Curso Presencial '" + getNombre() + "' iniciado.");
            System.out.println("La primera sesión se lleva a cabo en el aula: " + aula);
        } else {
            System.out.println("Error: El curso debe estar publicado para iniciar.");
        }
    }

    @Override
    public void finalizar() {
        System.out.println("core.Curso Presencial '" + getNombre() + "' ha finalizado.");
    }

    @Override
    public String obtenerDetalle() {
        return super.obtenerDetalle() +
                "\nModalidad: Presencial" +
                "\nAula: " + aula +
                "\nDirección: " + direccion;
    }

    public String getAula() {
        return aula;
    }

    public String getDireccion() {
        return direccion;
    }
}