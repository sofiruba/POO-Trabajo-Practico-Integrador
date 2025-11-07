package data;

import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.usuario.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestorBDDUsuario {

    // Conexión a la base de datos
    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; // Cambialo si tenés otra clave

    // ===============================
    // 🔹 Guardar usuarios
    // ===============================
    public void guardarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos (nombre, email, contrasenia, fecha_inscripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getEmail());
            ps.setString(3, alumno.getContrasenia());
            ps.setDate(4, new java.sql.Date(alumno.getFechaInscripcion().getTime()));
            ps.executeUpdate();
            System.out.println("Alumno guardado en la base de datos correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al guardar alumno: " + e.getMessage());
        }
    }

    public void guardarDocente(Docente docente) {
        String sql = "INSERT INTO docentes (nombre, email, contrasenia, especialidad) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, docente.getNombre());
            ps.setString(2, docente.getEmail());
            ps.setString(3, docente.getContrasenia());
            ps.setString(4, docente.getEspecialidad());
            ps.executeUpdate();
            System.out.println("Docente guardado en la base de datos correctamente.");

        } catch (SQLException e) {
            System.err.println("Error al guardar docente: " + e.getMessage());
        }
    }

    // ===============================
    // 🔹 Buscar usuarios
    // ===============================
    public Usuario buscarPorEmail(String email) {
        Usuario usuario = null;

        // Busco primero en la tabla de alumnos
        String sqlAlumno = "SELECT * FROM alumnos WHERE email = ?";
        String sqlDocente = "SELECT * FROM docentes WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            // Buscar alumno
            try (PreparedStatement ps = conn.prepareStatement(sqlAlumno)) {
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    usuario = new Alumno(
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("contrasenia"),
                            rs.getDate("fecha_inscripcion")
                    );
                }
            }

            // Buscar docente si no se encontró alumno
            if (usuario == null) {
                try (PreparedStatement ps = conn.prepareStatement(sqlDocente)) {
                    ps.setString(1, email);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        usuario = new Docente(
                                rs.getString("nombre"),
                                rs.getString("email"),
                                rs.getString("contrasenia"),
                                rs.getString("especialidad")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return usuario;
    }

    // ===============================
    // 🔹 Obtener todos
    // ===============================
    public List<Alumno> buscarTodosAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Alumno(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getDate("fecha_inscripcion")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener alumnos: " + e.getMessage());
        }

        return lista;
    }

    public List<Docente> buscarTodosDocentes() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT * FROM docentes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Docente(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                        rs.getString("especialidad")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
        }

        return lista;
    }
}
