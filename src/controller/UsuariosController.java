package controller;

import data.GestorBDDUsuario;
import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;


public class UsuariosController {

    private final List<Alumno> alumnos;
    private final List<Docente> docentes;
    private final GestorBDDUsuario gestor;
    private CursosController cursosController;

    public UsuariosController() {
        this.gestor = new GestorBDDUsuario();
        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();

        // Al iniciar, cargamos los usuarios desde la base
        this.alumnos.addAll(gestor.buscarTodosAlumnos());
        this.docentes.addAll(gestor.buscarTodosDocentes());
    }


    public void setCursosController(CursosController cursosController) {
        this.cursosController = cursosController;
    }
    public Alumno registrarAlumno(Alumno alumno) {
        // Guardar y sincronizar ID
        Alumno alumnoConId = gestor.guardarAlumno(alumno);
        // Agregamos el objeto sincronizado a la lista en memoria
        alumnos.add(alumnoConId);
        System.out.println("Alumno registrado: " + alumnoConId.getNombre());
        return alumnoConId; // Devolvemos el objeto con el ID correcto
    }

public Docente registrarDocente(Docente docente) {
    // 1. Guardar y sincronizar ID en la base de datos
    Docente docenteConId = gestor.guardarDocente(docente); // 👈 Asume que GestorBDDUsuario.guardarDocente devuelve Docente
    
    // 2. Agregamos el objeto sincronizado a la lista en memoria
    if (docenteConId != null) {
        docentes.add(docenteConId);
        System.out.println("Docente registrado: " + docenteConId.getNombre());
    } else {
        System.out.println("⚠️ No se pudo registrar el docente en la BDD.");
    }
    return docenteConId; // Devolvemos el objeto con el ID correcto
}

    public Usuario login(String email, String contrasenia) {
    // 1. Buscar el usuario básico en la BDD
    Usuario usuario = gestor.buscarPorEmail(email);

    if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
        usuario.login();
        
        // 2. HIDRATACIÓN COMPLETA (Cursos y Calificaciones)
        if (usuario instanceof Alumno alumno) {
            if (this.cursosController != null) {
                // ✅ CLAVE: Cargar Cursos y Calificaciones del Alumno
                this.cursosController.cargarCursosInscritos(alumno); // Método de la respuesta anterior
                this.cursosController.cargarCalificaciones(alumno); // Asume que este método existe
            } else {
                System.err.println("⚠️ Error: CursosController no fue inyectado.");
            }
        }
        return usuario;
    } else {
        System.out.println("❌ Credenciales inválidas.");
        return null;
    }
}
public Alumno buscarAlumnoPorId(int id) {
    // 1. (Opcional) Buscar en la lista de objetos en memoria (si ya fue cargado al inicio)
    for (Alumno a : alumnos) {
        if (a.getId() == id) {
            return a;
        }
    }
    
    // 2. Buscar en la BDD
    Alumno alumno = gestor.buscarAlumnoPorId(id);
    
    if (alumno == null) {
        System.out.println("⚠️ No se encontró alumno con ID: " + id);
    }
    
    return alumno;
}

    public void logout(Usuario usuario) {
        if (usuario != null) {
            usuario.logout();
        }
    }

    public Usuario buscarPorEmail(String email) {
        // Primero busca en memoria, luego en la base si no lo encuentra
        for (Alumno a : alumnos) {
            if (a.getEmail().equalsIgnoreCase(email)) return a;
        }
        for (Docente d : docentes) {
            if (d.getEmail().equalsIgnoreCase(email)) return d;
        }
        Docente docente = gestor.buscarDocentePorEmail(email);
        if (docente != null) return docente;
        return gestor.buscarPorEmail(email);
    }

    public Alumno buscarAlumnoPorEmail(String email) {
        Alumno alumno = gestor.buscarAlumnoPorEmail(email);
        if (alumno == null) {
            System.out.println("⚠️ No se encontró alumno con email: " + email);
        }
        return alumno;
    }

    public Docente buscarDocentePorEmail(String email) {
        Docente doc = gestor.buscarDocentePorEmail(email);
        if (doc == null) {
            System.out.println("⚠️ No se encontró alumno con email: " + email);
        }
        return doc;
    }
    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }
        public List<Alumno> obtenerTodosAlumnos() {
        return new ArrayList<>(alumnos);
    }
    
    public List<Docente> obtenerTodosDocentes() {
        return new ArrayList<>(docentes);
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> todosLosUsuarios = new ArrayList<>();
        todosLosUsuarios.addAll(alumnos);
        todosLosUsuarios.addAll(docentes);
        return todosLosUsuarios;
    }
}
