package data;

import modelos.usuario.Alumno;
import modelos.usuario.Docente;
import modelos.usuario.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GestorBDDUsuario {

    private static final String URL = "jdbc:mysql://localhost:3306/plataforma_cursos";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql"; // tu clave

    // 🔹 Guardar Alumno
    public void guardarAlumno(Alumno alumno) {
        String sql = "INSERT INTO usuario (nombre, email, contrasenia, fecha_inscripcion, tipo) VALUES (?, ?, ?, ?, 'ALUMNO')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, alumno.getNombre());
            ps.setString(2, alumno.getEmail());
            ps.setString(3, alumno.getContrasenia());
            ps.setDate(4, new java.sql.Date(alumno.getFechaInscripcion().getTime()));
            ps.executeUpdate();

            System.out.println("Alumno guardado correctamente en la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al guardar alumno: " + e.getMessage());
        }
    }

    // 🔹 Guardar Docente
    public void guardarDocente(Docente docente) {
        String sql = "INSERT INTO usuario (nombre, email, contrasenia, especialidad, tipo) VALUES (?, ?, ?, ?, 'DOCENTE')";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, docente.getNombre());
            ps.setString(2, docente.getEmail());
            ps.setString(3, docente.getContrasenia());
            ps.setString(4, docente.getEspecialidad());
            ps.executeUpdate();

            System.out.println("Docente guardado correctamente en la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al guardar docente: " + e.getMessage());
        }
    }

    // 🔹 Buscar por email
    public Usuario buscarPorEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");
                if ("alumno".equalsIgnoreCase(rol)) {
                    usuario = new Alumno(
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("contrasenia"),
                            rs.getDate("fecha_inscripcion")
                    );
                } else if ("docente".equalsIgnoreCase(rol)) {
                    usuario = new Docente(
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("contrasenia"),
                            ""
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return usuario;
    }

    // 🔹 Buscar todos alumnos
    public List<Alumno> buscarTodosAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE tipo = 'ALUMNO'";

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

    // 🔹 Buscar todos docentes
    public List<Docente> buscarTodosDocentes() {
        List<Docente> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario WHERE tipo = 'DOCENTE'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Docente(
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("contrasenia"),
                       ""
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
        }

        return lista;
    }
}
