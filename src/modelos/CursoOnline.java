package modelos;

public class CursoOnline extends Curso {

    private String linkPlataforma;
    private String plataforma;

    public CursoOnline( String nombre, String descripcion, int cupo,
                       String linkPlataforma, String plataforma) {
        // Llamada al constructor de la clase padre (core.Curso)
        super( nombre, descripcion, cupo);
        this.linkPlataforma = linkPlataforma;
        this.plataforma = plataforma;
    }

    @Override
    public void iniciar() {
        if (getEstado().equals(Curso.ESTADO_PUBLICADO)) {
            System.out.println("core.Curso Online '" + getNombre() + "' iniciado.");
            System.out.println("Los alumnos pueden acceder a través de: " + linkPlataforma);
        } else {
            System.out.println("Error: El curso debe estar publicado para iniciar.");
        }
    }

    @Override
    public void finalizar() {
        System.out.println("core.Curso Online '" + getNombre() + "' ha finalizado.");
    }

    @Override
    public String obtenerDetalle() {
        return super.obtenerDetalle() +
                "\nModalidad: Online" +
                "\nPlataforma: " + plataforma +
                "\nLink de Acceso: " + linkPlataforma;
    }

    public String getLinkPlataforma() {
        return linkPlataforma;
    }

    public String getPlataforma() {
        return plataforma;
    }
}