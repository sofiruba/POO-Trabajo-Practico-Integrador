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

    public UsuariosController() {
        this.gestor = new GestorBDDUsuario();
        this.alumnos = new ArrayList<>();
        this.docentes = new ArrayList<>();

        // Al iniciar, cargamos los usuarios desde la base
        this.alumnos.addAll(gestor.buscarTodosAlumnos());
        this.docentes.addAll(gestor.buscarTodosDocentes());
    }

    public void registrarAlumno(Alumno alumno) {
        gestor.guardarAlumno(alumno);
        alumnos.add(alumno);
        System.out.println("Alumno registrado: " + alumno.getNombre());
    }

    public void registrarDocente(Docente docente) {
        gestor.guardarDocente(docente);
        docentes.add(docente);
        System.out.println("Docente registrado: " + docente.getNombre());
    }

    public Usuario login(String email, String contrasenia) {
        Usuario usuario = gestor.buscarPorEmail(email);

        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            usuario.login();
            return usuario;
        } else {
            System.out.println("Credenciales incorrectas.");
            return null;
        }
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
        return gestor.buscarPorEmail(email);
    }

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public List<Docente> getDocentes() {
        return docentes;
    }
}
