package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import user.Alumno;
import user.Docente;
import user.Usuario;
import data.interfaces.ContratoDeUsuario;
import data.implementaciones.*; // Asumimos un DAO/Gestor para Usuarios

public class UsuariosController {

    // Atributos (Colecciones en Memoria)
    private List<Alumno> alumnos;
    private List<Docente> docentes;
    private final ContratoDeUsuario gestorUsuarios;

    // Dependencia (Abstracción a Persistencia)
    // Se usa un Gestor de Datos para Alumnos y otro para Docentes
    //private IGestorDeDatos<Alumno, Integer> gestorAlumnos;
   // private IGestorDeDatos<Docente, Integer> gestorDocentes;

    public UsuariosController() {
        this.gestorUsuarios = new GestorDeUsuarioSQL();
        // Inicialización de los gestores
        // NOTA: Usaremos un gestor simplificado que maneja ambos, o dos archivos separados.
        // Aquí asumimos que ArchivoDeUsuarios carga y guarda una lista de todos los Usuarios,
        // y los separamos en Alumnos y Docentes. Para simplicidad, inicializaremos las listas.

        // **En un TPI real, cargarías los datos aquí usando los DAOs correspondientes**
        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();

        // Ejemplo de inicialización con DAO (asumiendo que lo implementarás)
        // this.gestorAlumnos = new ArchivoDeUsuarios<Alumno, Integer>("alumnos.json");
        // this.gestorDocentes = new ArchivoDeUsuarios<Docente, Integer>("docentes.json");

        // Por ahora, inicializamos las colecciones en memoria
        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();
    }


    public Alumno registrarAlumno(String nombre, String email, String contrasena) {
        Alumno nuevoAlumno = new Alumno(nombre, email, contrasena, new Date());
        this.alumnos.add(nuevoAlumno);
        System.out.println("Controladora Usuarios: Alumno " + nombre + " registrado.");
        return nuevoAlumno;
    }

    public Docente registrarDocente(String nombre, String email, String contrasena, String especialidad) {
        Docente nuevoDocente = new Docente(nombre, email, contrasena, especialidad);

        this.docentes.add(nuevoDocente);
        // Lógica de Persistencia: this.gestorDocentes.guardar(nuevoDocente);

        System.out.println("Controladora Usuarios: Docente " + nombre + " registrado.");
        return nuevoDocente;
    }


    public boolean login(String email, String contrasena) {

        Usuario user = alumnos.stream()
                .filter(a -> a.getEmail().equals(email) && a.getContrasenia().equals(contrasena))
                .findFirst()
                .orElse(null);

        if (user != null) return false;

        user = docentes.stream()
                .filter(d -> d.getEmail().equals(email) && d.getContrasenia().equals(contrasena))
                .findFirst()
                .orElse(null);

        return this.gestorUsuarios.buscarPorNombreYClave(email, contrasena);
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }
}